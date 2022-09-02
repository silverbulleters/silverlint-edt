/**
 * Copyright (C) 2022, SilverBulleters LLC
 */
package org.silverbulleters.dt.silverlint.sonarlint;

import java.nio.charset.Charset;
import java.nio.file.Path;

public final class LintHelper {

	private LintHelper() {
		throw new UnsupportedOperationException();
	}

	public static DefaultClientInputFile getInputFile(Path path) {
		return new DefaultClientInputFile(path.toString(), path.toString(), false, Charset.defaultCharset(),
				path.toUri());
	}
}
