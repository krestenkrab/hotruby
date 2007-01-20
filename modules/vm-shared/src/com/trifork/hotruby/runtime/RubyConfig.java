package com.trifork.hotruby.runtime;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class RubyConfig {

	private String[] args = new String[0];
	private InputStream in = System.in;
	private OutputStream out = System.out;
	private OutputStream err = System.err;

	public void setArguments(String[] args) {
		this.args = args;
	}

	public void setInputStream(InputStream in) {
		this.in = in;
	}

	public void setOutputStream(OutputStream out) {
		this.out = out;
	}

	public void setErrorStream(OutputStream err) {
		this.err = err;
	}

}
