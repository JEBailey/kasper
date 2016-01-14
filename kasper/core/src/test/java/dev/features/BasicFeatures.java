package dev.features;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org._24601.kasper.error.KasperException;
import org._24601.kasper.scripting.KasperScriptEngine;
import org.junit.Before;
import org.junit.Test;

public class BasicFeatures {

    @Before
    public void setUp() throws KasperException {

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
	public void conditionalIf() throws KasperException {
		assertEquals("<a></a>", eval("if 'true' {a}"));
	}
	
	@Test
	public void conditionalIfBool() throws KasperException {
		assertEquals("<a></a>", eval("if true {a}"));
	}
	
	private Object eval(String expression) throws KasperException {
		ScriptEngine engine = new KasperScriptEngine();
		ScriptContext context = new SimpleScriptContext();
		context.setAttribute("foo", 1, ScriptContext.ENGINE_SCOPE);
		context.setAttribute("bar", "this is sparta", ScriptContext.ENGINE_SCOPE);
		context.setAttribute("foobar", new String[]{"this", "is", "a", "test"}, ScriptContext.ENGINE_SCOPE);
		StringWriter sw = new StringWriter();
		context.setWriter(sw);
		try {
			engine.eval(expression, context);
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return sw.toString();
    }

}
