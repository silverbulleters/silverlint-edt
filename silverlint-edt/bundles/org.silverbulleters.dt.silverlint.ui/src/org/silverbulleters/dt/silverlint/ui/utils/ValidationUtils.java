/**
 * Copyright (C) 2020, SilverBulleters LLC
 */
package org.silverbulleters.dt.silverlint.ui.utils;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.sonarsource.sonarlint.core.client.api.common.analysis.Issue;

import com._1c.g5.v8.dt.bsl.model.Module;
import com._1c.g5.v8.dt.bsl.validation.CustomValidationMessageAcceptor;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationUtils {
	private final String ISSUE_PREFIX = "[SonarLint]";

	public void acceptIssue(Module module, CustomValidationMessageAcceptor messageAcceptor, Issue issue,
			Document document) {

		int[] offsetParams;
		try {
			offsetParams = getOffsetByRange(issue, document);
		} catch (BadLocationException e) {
			System.out.println(e.getMessage());
			return;
		}

		var message = ISSUE_PREFIX + " " + issue.getMessage();
		var type = issue.getType();
		var ruleKey = issue.getRuleKey();
		if (type.equals("BUG") || type.equals("VULNERABILITY")) {
			messageAcceptor.acceptError(message, module, offsetParams[0], offsetParams[1], ruleKey);
		} else if (type.equals("SECURITY_HOTSPOT") || type.equals("CODE_SMELL")) {
			messageAcceptor.acceptWarning(message, module, offsetParams[0], offsetParams[1], ruleKey);
		} else {
			messageAcceptor.acceptInfo(message, module, offsetParams[0], offsetParams[1], ruleKey);
		}

	}

	private int[] getOffsetByRange(Issue issue, Document document) throws BadLocationException {
		var offset = 0;
		var lenght = 0;
		if (issue.getStartLine() == null) {
			offset = 0;
			lenght = 1;
		} else {
			offset = document.getLineOffset(issue.getStartLine() - 1) + issue.getStartLineOffset();
			lenght = document.getLineOffset(issue.getEndLine() - 1) + issue.getEndLineOffset() - offset;
		}
		return new int[] { offset, lenght };
	}

}
