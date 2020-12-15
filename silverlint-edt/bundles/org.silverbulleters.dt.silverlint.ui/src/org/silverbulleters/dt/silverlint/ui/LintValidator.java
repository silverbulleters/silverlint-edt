/**
 * Copyright (C) 2020, SilverBulleters LLC
 */
package org.silverbulleters.dt.silverlint.ui;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Path;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.validation.CheckType;
import org.silverbulleters.dt.silverlint.ProjectHelper;
import org.silverbulleters.dt.silverlint.SilverCore;
import org.silverbulleters.dt.silverlint.sonarlint.DefaultClientInputFile;
import org.sonarsource.sonarlint.core.client.api.common.analysis.Issue;

import com._1c.g5.v8.dt.bsl.model.Module;
import com._1c.g5.v8.dt.bsl.validation.CustomValidationMessageAcceptor;
import com._1c.g5.v8.dt.bsl.validation.IExternalBslValidator;

public class LintValidator implements IExternalBslValidator {

	@Override
	@Check(CheckType.EXPENSIVE)
	public void validate(EObject object, CustomValidationMessageAcceptor messageAcceptor, CancelIndicator monitor) {
		var core = SilverCore.getCore();
		var service = core.getLintService();
		
		var module = (Module) object;
		var node = NodeModelUtils.findActualNodeFor(module);
		var content = node.getText();
		if (content == null) {
			content = "";
		}
		var document = new Document(content);
		var moduleFile = ResourcesPlugin.getWorkspace().getRoot()
				.getFile(new org.eclipse.core.runtime.Path(EcoreUtil.getURI(module).toPlatformString(true)));
		var uri = uri(moduleFile.getLocationURI());
		
		var project = ProjectHelper.getProjectByUri(Path.of(uri));
		if (project.isEmpty()) {
			return;
		}
		
		var basePath = ProjectHelper.getProjectPath(project.get());

		var inputFile = getInputFile(Path.of(uri));
		var list = service.getDiagnostics(inputFile, basePath);
		list.forEach(issue -> {
			acceptIssue(module, messageAcceptor, issue, document);
		});

	}

	@Override
	public boolean needValidation(EObject object) {
		return object instanceof Module;
	}

	private URI uri(URI uri) {
		return uri;
	}

	private static DefaultClientInputFile getInputFile(Path path) {
		return new DefaultClientInputFile(path.toString(), path.toString(), false, Charset.defaultCharset(),
				path.toUri());
	}

	private void acceptIssue(Module module, CustomValidationMessageAcceptor messageAcceptor, Issue issue,
			Document document) {

		int[] offsetParams;
		try {
			offsetParams = getOffsetByRange(issue, document);
		} catch (BadLocationException e) {
			System.out.println(e.getMessage());
			return;
		}
	
		var message = "[SonarLint] " + issue.getMessage();
		var type = issue.getType();
		if (type.equals("BUG") || type.equals("VULNERABILITY")) {
			messageAcceptor.acceptError(message, module, offsetParams[0], offsetParams[1], issue.getRuleKey());
		} else if (type.equals("SECURITY_HOTSPOT") || type.equals("CODE_SMELL")) {
			messageAcceptor.acceptWarning(message, module, offsetParams[0], offsetParams[1], issue.getRuleKey());
		} else {
			messageAcceptor.acceptInfo(message, module, offsetParams[0], offsetParams[1], issue.getRuleKey());
		}

	}

	public int[] getOffsetByRange(Issue issue, Document document) throws BadLocationException {
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
