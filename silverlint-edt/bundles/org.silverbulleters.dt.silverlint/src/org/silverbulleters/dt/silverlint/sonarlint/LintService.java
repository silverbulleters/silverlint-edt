/**
 * Copyright (C) 2022, SilverBulleters LLC
 */
package org.silverbulleters.dt.silverlint.sonarlint;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.silverbulleters.dt.silverlint.project.ProjectSetting;
import org.sonarsource.sonarlint.core.ConnectedSonarLintEngineImpl;
import org.sonarsource.sonarlint.core.client.api.common.Language;
import org.sonarsource.sonarlint.core.client.api.common.LogOutput;
import org.sonarsource.sonarlint.core.client.api.common.RuleDetails;
import org.sonarsource.sonarlint.core.client.api.common.analysis.Issue;
import org.sonarsource.sonarlint.core.client.api.connected.ConnectedAnalysisConfiguration;
import org.sonarsource.sonarlint.core.client.api.connected.ConnectedGlobalConfiguration;
import org.sonarsource.sonarlint.core.client.api.connected.ServerConfiguration;

public class LintService {
	public static final String SOURCE = "sonarlint";
	private static final String SERVER_ID = SOURCE;
	private static final int DEFAULT_TIMEOUT = 30;

	private ConnectedGlobalConfiguration configuration;
	private ConnectedSonarLintEngineImpl connection;
	private LogOutput logOutput = new CustomLogOutput();
	private ProjectSetting projectSetting;

	public LintService(ProjectSetting projectSetting) {
		this.projectSetting = projectSetting;
		configuration = ConnectedGlobalConfiguration.builder().setLogOutput(logOutput).addEnabledLanguage(Language.BSL)
				.setServerId(SERVER_ID).build();
	}
	
	public boolean projectSettingIsValid() {	
		return !projectSetting.getProjectKey().isEmpty();
	}

	public void start() {
		var serverConfiguration = ServerConfiguration.builder().token(projectSetting.getToken())
				.url(projectSetting.getServerUrl()).userAgent(SOURCE).build();

		ConnectedSonarLintEngineImpl currentConnection;
		try {
			currentConnection = new ConnectedSonarLintEngineImpl(configuration);
			currentConnection.update(serverConfiguration, null);
			currentConnection.updateProject(serverConfiguration, projectSetting.getProjectKey(), null);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return;
		}
		this.connection = currentConnection;

		try {
			connection.start();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			connection = null;
		}
	}

	public void stop() {
		if (connection != null) {
			try {
				connection.stop(false);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			connection = null;
		}
	}

	public void restart() {
		stop();
		start();
	}

	public boolean isAlive() {
		return connection != null;
	}

	private boolean isActive() {
		if (!isAlive()) {
			start();
		}
		return isAlive();
	}

	public List<Issue> getDiagnostics(DefaultClientInputFile inputFile, Path basePath) {
		if (!isActive()) {
			return Collections.emptyList();
		}

		var issueListener = new StoreIssueListener(new ArrayList<>());
		runFutureTask(() -> fillDiagnostics(inputFile, basePath, issueListener));
		return issueListener.getIssues();
	}

	private List<Issue> fillDiagnostics(DefaultClientInputFile inputFile, Path basePath,
			StoreIssueListener issueListener) {
		var analysisConfiguration = ConnectedAnalysisConfiguration.builder().setBaseDir(basePath)
				.addInputFile(inputFile).setProjectKey(projectSetting.getProjectKey()).build();
		connection.analyze(analysisConfiguration, issueListener, logOutput, null);
		return issueListener.getIssues();
	}

	public void runFutureTask(Runnable runnable) {
		runFutureTask(runnable, DEFAULT_TIMEOUT);
	}

	public void runFutureTask(Runnable runnable, int timeoutInSeconds) {
		var threadpool = Executors.newCachedThreadPool();
		var futureTask = threadpool.submit(runnable);
		try {
			futureTask.get(timeoutInSeconds, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
			futureTask.cancel(true);
		}
		threadpool.shutdown();
	}
	
	public RuleDetails getRuleDescription(String ruleKey) {
		return connection.getRuleDetails(ruleKey);
	}
}
