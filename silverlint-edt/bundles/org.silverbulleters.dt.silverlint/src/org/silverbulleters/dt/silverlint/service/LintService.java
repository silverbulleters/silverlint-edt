/**
 * Copyright (C) 2020, SilverBulleters LLC
 */
package org.silverbulleters.dt.silverlint.service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.silverbulleters.dt.silverlint.ProjectSetting;
import org.silverbulleters.dt.silverlint.sonarlint.CustomLogOutput;
import org.silverbulleters.dt.silverlint.sonarlint.DefaultClientInputFile;
import org.silverbulleters.dt.silverlint.sonarlint.StoreIssueListener;
import org.sonarsource.sonarlint.core.ConnectedSonarLintEngineImpl;
import org.sonarsource.sonarlint.core.client.api.common.Language;
import org.sonarsource.sonarlint.core.client.api.common.LogOutput;
import org.sonarsource.sonarlint.core.client.api.common.analysis.Issue;
import org.sonarsource.sonarlint.core.client.api.connected.ConnectedAnalysisConfiguration;
import org.sonarsource.sonarlint.core.client.api.connected.ConnectedGlobalConfiguration;
import org.sonarsource.sonarlint.core.client.api.connected.ServerConfiguration;

public class LintService {
	public static final String SOURCE = "sonarlint";
	private static final String SERVER_ID = SOURCE;
	
//	private String token = "11cc533e056fc23e2782fbb39a4e82e478061453";
//	private String projectKey = "demo-edt";
//	private String serverUrl = "https://open.checkbsl.org/";

	private ConnectedGlobalConfiguration configuration;
	private ConnectedSonarLintEngineImpl connection;
	private LogOutput logOutput = new CustomLogOutput();
	private ProjectSetting projectSetting;

	public LintService(ProjectSetting projectSetting) {
		this.projectSetting = projectSetting;
		configuration = ConnectedGlobalConfiguration.builder().setLogOutput(logOutput).addEnabledLanguage(Language.BSL)
				.setServerId(SERVER_ID).build();
	}

	public void start() {
		var serverConfiguration = ServerConfiguration.builder()
				.token(projectSetting.getToken())
				.url(projectSetting.getServerUrl())
				.userAgent(SOURCE)
				.build();

		connection = new ConnectedSonarLintEngineImpl(configuration);
		try {
			connection.update(serverConfiguration, null);
			connection.updateProject(serverConfiguration, projectSetting.getProjectKey(), null);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			connection = null;
			return;
		}
		connection.start();
	}

	public void stop() {
		if (connection != null) {
			connection.stop(false);
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

	public List<Issue> getDiagnostics(DefaultClientInputFile inputFile, Path basePath) {	
		var analysisConfiguration = ConnectedAnalysisConfiguration.builder()
			      .setBaseDir(basePath)
			      .addInputFile(inputFile)
			      .setProjectKey(projectSetting.getProjectKey())
			      .build();
		
		var issueListener = new StoreIssueListener(new ArrayList<>());	
		connection.analyze(analysisConfiguration, issueListener, logOutput, null);
		return issueListener.getIssues();
	}
}
