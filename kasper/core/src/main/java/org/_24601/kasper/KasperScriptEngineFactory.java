package org._24601.kasper;

import java.util.Arrays;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

import org.kohsuke.MetaInfServices;

@MetaInfServices
public class KasperScriptEngineFactory implements ScriptEngineFactory {

	@Override
	public String getEngineName() {
		return "Kasper Engine";
	}

	@Override
	public String getEngineVersion() {
		return "0.0.1";
	}

	@Override
	public List<String> getExtensions() {
		return Arrays.asList("ksp");
	}

	@Override
	public List<String> getMimeTypes() {
		return Arrays.asList("text/x-kasper");
	}

	@Override
	public List<String> getNames() {
		return Arrays.asList("kasper");
	}

	@Override
	public String getLanguageName() {
		return "Kasper";
	}

	@Override
	public String getLanguageVersion() {
		return "0.5";
	}

	@Override
	public Object getParameter(String key) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getMethodCallSyntax(String obj, String m, String... args) {
		return "";
	}

	@Override
	public String getOutputStatement(String toDisplay) {
		return "";
	}

	@Override
	public String getProgram(String... statements) {
		return null;
	}

	@Override
	public ScriptEngine getScriptEngine() {
		return new KasperScriptEngine(this);
	}

}
