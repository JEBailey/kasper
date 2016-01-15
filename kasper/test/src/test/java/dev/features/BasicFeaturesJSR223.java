package dev.features;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org._24601.kasper.error.KasperException;
import org.junit.Before;
import org.junit.Test;

public class BasicFeaturesJSR223 {
	
	private ScriptContext context;

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
		assertEquals("1", eval("${foo}"));
	}
	
	@Test
	public void testAccessorNonbean() throws KasperException {
		assertEquals("14", eval("${bar.length()}"));
	}
	
	@Test
	public void testAccessorForBean() throws KasperException {
		assertEquals("false", eval("${bar.empty}"));
	}
	
	@Test
	public void testAccessorForMultipleBeanLevels() throws KasperException {
		assertEquals("false", eval("${bar.empty.toString()}"));
	}
	
	@Test
	public void testForEachExtended() throws KasperException {
		assertEquals("<a>this</a><a>is</a><a>a</a><a>test</a>", eval("${foobar} { a ${this} }"));
	}
	
	@Test
	public void testForEachExtendedOgnl() throws KasperException {
		assertEquals("<a>4</a><a>2</a><a>1</a><a>4</a>", eval("${foobar} { a ${this.length()}}"));
	}
	
	@Test
	public void testForIf() throws KasperException {
		assertEquals("<a>fish</a>", eval("if true { a {'fish'} } "));
	}
	
	private Object eval(String expression) throws KasperException {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByExtension("ksp");
		ScriptContext context = engine.getContext();
		context.setAttribute("foo", 1, ScriptContext.ENGINE_SCOPE);
		context.setAttribute("bar", "this is sparta", ScriptContext.ENGINE_SCOPE);
		context.setAttribute("foobar", new String[]{"this", "is", "a", "test"}, ScriptContext.ENGINE_SCOPE);
		StringWriter sw = new StringWriter();
		context.setWriter(sw);
        try {
			engine.eval(expression, context);
			return sw.toString();
		} catch (Throwable e) {
			return e.getMessage();
		}
    }

}
