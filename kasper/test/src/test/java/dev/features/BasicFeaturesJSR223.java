package dev.features;

import static org.junit.Assert.assertEquals;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org._24601.kasper.core.KasperContext;
import org._24601.kasper.error.KasperException;
import org.junit.Before;
import org.junit.Test;

public class BasicFeaturesJSR223 {
	
	private KasperContext context;

    @Before
    public void setUp() throws KasperException {
		//context = PoslProvider.getDefaultContext();
    }

	@Test
	public void testTagCreation() throws KasperException {
		assertEquals("<a></a>", eval("a"));
	}
	
	@Test
	public void testChildElements() throws KasperException {
		assertEquals("<a><b></b></a>", eval("a() {b}"));
	}
	
	@Test
	public void testDiscreteElements() throws KasperException {
		assertEquals("<a><b></b></a>", eval("a {b}"));
	}
	
	@Test
	public void testline() throws KasperException {
		assertEquals("<a>some content</a>", eval("a \"some content\""));
	}
	
	@Test
	public void testLink() throws KasperException {
		assertEquals("<a href='./foo' src='http://gotohere'></a>", eval("a(href = \"./foo\", src=\"http://gotohere\")"));
	}
	
	@Test
	public void testDoctype() throws KasperException {
		assertEquals("<!doctype html>", eval("doctype(html)"));
	}
	
	@Test
	public void testComment() throws KasperException {
		assertEquals("<!--this is a child <br>this is another-->", eval("comment{\"this is a child \"\nbr\n\"this is another\"}"));
	}
	
	@Test
	public void testBindings() throws KasperException {
		assertEquals(1, eval("${foo}"));
	}
	
	private Object eval(String expression) throws KasperException {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByExtension("ksp");
		Bindings bindings = engine.createBindings();
		bindings.put("foo", 1);
		engine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
        try {
			return engine.eval(expression);
		} catch (ScriptException e) {
			return e.getMessage();
		}
    }

}
