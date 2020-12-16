/**
 * Copyright (C) 2020, SilverBulleters LLC
 */
package org.silverbulleters.dt.silverlint.ui;

import java.nio.file.Path;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.text.Document;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.validation.CheckType;
import org.silverbulleters.dt.silverlint.SilverCore;
import org.silverbulleters.dt.silverlint.project.ProjectHelper;
import org.silverbulleters.dt.silverlint.sonarlint.LintHelper;
import org.silverbulleters.dt.silverlint.ui.utils.ValidationUtils;

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
		var uri = moduleFile.getLocationURI();

		var project = ProjectHelper.getProjectByUri(Path.of(uri));
		if (project.isEmpty()) {
			return;
		}

		var basePath = ProjectHelper.getProjectPath(project.get());

		var inputFile = LintHelper.getInputFile(Path.of(uri));
		var list = service.getDiagnostics(inputFile, basePath);
		list.forEach(issue -> {
			ValidationUtils.acceptIssue(module, messageAcceptor, issue, document);
		});

	}

	@Override
	public boolean needValidation(EObject object) {
		return object instanceof Module;
	}

}
