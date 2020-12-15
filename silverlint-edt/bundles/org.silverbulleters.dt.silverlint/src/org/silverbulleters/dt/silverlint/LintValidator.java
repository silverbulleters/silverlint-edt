/**
 * Copyright (C) 2020, SilverBulleters LLC
 */
package org.silverbulleters.dt.silverlint;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.validation.CheckType;

import com._1c.g5.v8.dt.bsl.model.Module;
import com._1c.g5.v8.dt.bsl.validation.CustomValidationMessageAcceptor;
import com._1c.g5.v8.dt.bsl.validation.IExternalBslValidator;

public class LintValidator implements IExternalBslValidator {

	@Override
	@Check(CheckType.EXPENSIVE)
	public void validate(EObject object, CustomValidationMessageAcceptor messageAcceptor, CancelIndicator monitor) {
		var message = "Пример срабатывания";
		var code = "silverlint";
		var module = (Module) object;
		messageAcceptor.acceptError(message, module, 0, 1, code);
	}

	@Override
	public boolean needValidation(EObject object) {
		return object instanceof Module;
	}

}
