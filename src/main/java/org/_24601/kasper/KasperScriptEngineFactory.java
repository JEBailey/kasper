package org._24601.kasper;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

import org._24601.kasper.scripting.KasperScriptEngine;
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

	@SuppressWarnings("serial")
	@Override
	public List<String> getExtensions() {
		return new ArrayList<String>() {
			{
				add("ksp");
			}
		};
	}

	@Override
	public List<String> getMimeTypes() {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}

	@Override
	public List<String> getNames() {
		List<String> names = new ArrayList<String>();
		names.add("kasper");
		return names;
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
		return new KasperScriptEngine();
	}

}
