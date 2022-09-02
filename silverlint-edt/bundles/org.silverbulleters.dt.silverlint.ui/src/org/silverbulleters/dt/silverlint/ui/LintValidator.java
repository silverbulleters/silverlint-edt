/**
 * Copyright (C) 2022, SilverBulleters LLC
 */
package org.silverbulleters.dt.silverlint.ui;

import java.net.URI;
import java.nio.file.Path;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.text.Document;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.validation.CheckType;
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
		if (monitor.isCanceled()) {
		    return;
		}
		
		PlatformUI.getWorkbench().getWorkbenchWindows();
		
		var core = Activator.getDefault().getCore();
		var module = (Module) object;		
		var uri = getUriFromModule(module);
		
		var projectOpt = ProjectHelper.getProjectByUri(Path.of(uri));
		if (projectOpt.isEmpty()) {
			return;
		}
		var project = projectOpt.get();
		
		var service = core.getLintManager().getService(project);
		if (!service.projectSettingIsValid()) {
			return;
		}
		
		if (monitor.isCanceled()) {
		    return;
		}
		
		var content = getContent(module);
		var document = new Document(content);
		
		var inputFile = LintHelper.getInputFile(Path.of(uri));
		var list = service.getDiagnostics(inputFile, ProjectHelper.getProjectPath(project));
		
		list.forEach(issue -> {
			if (monitor.isCanceled()) {
			    return;
			}
			
			ValidationUtils.acceptIssue(module, messageAcceptor, issue, document);
		});
	}

	@Override
	public boolean needValidation(EObject object) {
		return object instanceof Module;
	}
	
	private String getContent(Module module) {
		var node = NodeModelUtils.findActualNodeFor(module);
		var content = node.getText();
		if (content == null) {
			content = "";
		}
		return content;
	}
	
	private URI getUriFromModule(Module module) {
		var moduleFile = ResourcesPlugin.getWorkspace().getRoot()
				.getFile(new org.eclipse.core.runtime.Path(EcoreUtil.getURI(module).toPlatformString(true)));
		return moduleFile.getLocationURI();
	}

}
