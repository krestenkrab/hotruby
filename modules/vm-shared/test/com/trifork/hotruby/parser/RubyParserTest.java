package com.trifork.hotruby.parser;

import java.io.StringReader;

import org.junit.Test;

import com.trifork.hotruby.ast.AssignmentExpression;
import com.trifork.hotruby.ast.ClassCode;
import com.trifork.hotruby.ast.ClassExpression;
import com.trifork.hotruby.ast.IdentifierExpression;
import com.trifork.hotruby.ast.IntegerExpression;
import com.trifork.hotruby.ast.SelfExpression;
import com.trifork.hotruby.ast.TopLevelCode;
import static org.junit.Assert.*;

public class RubyParserTest {
	TopLevelCode result;
	
	@Test
	public void emptyClass() throws Exception {
		parse("class C\n"
				+ "end");
		ClassExpression classExpression = (ClassExpression)result.getBody();
		assertEquals(1, classExpression.line());
		ClassCode classCode = classExpression.getCode();
		SelfExpression selfExpression = (SelfExpression)classCode.getBody();
		// SelfExpression is a singleton, so the line number makes no sense.
		assertNotNull(selfExpression);
	}
	
	@Test
	public void variableAssignment() throws Exception {
		parse("a = 5");
		AssignmentExpression assignmentExpression = (AssignmentExpression)result.getBody();
		assertEquals(1, assignmentExpression.line());
		IdentifierExpression left = (IdentifierExpression)assignmentExpression.getLeft();
		IntegerExpression right = (IntegerExpression)assignmentExpression.getRight();
		assertEquals("a", left.getName());
		assertEquals(1, left.line());
		assertEquals("5", right.getText());
		assertEquals(1, right.line());
	}
	
	@Test
	public void arrayIndexing() throws Exception {
		parse("a[5]");
	}
	
	@Test
	public void methodCallWithArrayLiteralAndNoParentheses() throws Exception {
		parse("a [5]");
	}
	
	private void parse(String s) throws Exception {
		StringReader reader = new StringReader(s);
		RubyParser parser = new RubyParser(reader, "");
		result = parser.program();
	}
}
