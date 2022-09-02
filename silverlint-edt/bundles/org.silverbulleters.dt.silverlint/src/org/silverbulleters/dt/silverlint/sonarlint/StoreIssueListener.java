/**
 * Copyright (C) 2022, SilverBulleters LLC
 */
package org.silverbulleters.dt.silverlint.sonarlint;

import java.util.List;

import org.sonarsource.sonarlint.core.client.api.common.analysis.Issue;
import org.sonarsource.sonarlint.core.client.api.common.analysis.IssueListener;

import lombok.Getter;

public class StoreIssueListener implements IssueListener {
	@Getter
	private final List<Issue> issues;

	public StoreIssueListener(List<Issue> issues) {
		this.issues = issues;
	}

	@Override
	public void handle(Issue issue) {
		issues.add(issue);
	}
}
