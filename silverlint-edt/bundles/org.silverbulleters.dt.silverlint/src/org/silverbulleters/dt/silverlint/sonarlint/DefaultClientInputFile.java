/**
 * Copyright (C) 2020, SilverBulleters LLC
 */
package org.silverbulleters.dt.silverlint.sonarlint;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Files;

import org.sonarsource.sonarlint.core.client.api.common.analysis.ClientInputFile;

public class DefaultClientInputFile implements ClientInputFile{
	private final String path;
	private final String relativePath;
	private final boolean test;
	private final Charset charset;
	private final URI uri;

	public DefaultClientInputFile(String path, String relativePath, boolean test, Charset charset, URI uri) {
		this.path = path;
		this.relativePath = relativePath;
		this.test = test;
		this.charset = charset;
		this.uri = uri;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public boolean isTest() {
		return test;
	}

	@Override
	public Charset getCharset() {
		return charset;
	}

	@Override
	public <G> G getClientObject() {
		return null;
	}

	@Override
	public InputStream inputStream() throws IOException {
		return Files.newInputStream(Path.of(path));
	}

	@Override
	public String contents() throws IOException {
		return new String(Files.readAllBytes(Path.of(path)));
	}

	@Override
	public String relativePath() {
		return relativePath;
	}

	@Override
	public URI uri() {
		return uri;
	}

}