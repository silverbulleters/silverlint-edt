/**
 * Copyright (C) 2020, SilverBulleters LLC
 */
package org.silverbulleters.dt.silverlint.sonarlint;

import java.nio.charset.Charset;
import java.nio.file.Path;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LintHelper {

	public DefaultClientInputFile getInputFile(Path path) {
		return new DefaultClientInputFile(path.toString(), path.toString(), false, Charset.defaultCharset(),
				path.toUri());
	}

}
