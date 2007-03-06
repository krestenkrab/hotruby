package com.trifork.hotruby.parser;

import java.io.StringReader;

import org.junit.Ignore;
import org.junit.Test;

import com.trifork.hotruby.ast.ArrayExpression;
import com.trifork.hotruby.ast.AssignmentExpression;
import com.trifork.hotruby.ast.ClassCode;
import com.trifork.hotruby.ast.ClassExpression;
import com.trifork.hotruby.ast.FloatExpression;
import com.trifork.hotruby.ast.FunctionExpression;
import com.trifork.hotruby.ast.IdentifierExpression;
import com.trifork.hotruby.ast.IntegerExpression;
import com.trifork.hotruby.ast.MethodCallExpression;
import com.trifork.hotruby.ast.SelfExpression;
import com.trifork.hotruby.ast.SequenceExpression;
import com.trifork.hotruby.ast.TopLevelCode;
import com.trifork.hotruby.ast.UnaryExpression;

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
		MethodCallExpression methodCallExpression = (MethodCallExpression)result.getBody();
		assertEquals(1, methodCallExpression.line());

		// a is being indexed
		FunctionExpression functionExpression = (FunctionExpression) methodCallExpression.getExpression();
		assertEquals(1, functionExpression.line());
		assertEquals("a", functionExpression.getName());
		
		// The method called is []
		assertEquals("[]", methodCallExpression.getMethodName());

		// 5 is the index
		SequenceExpression arguments = methodCallExpression.getArgs();
		assertEquals(1, arguments.size());
		assertEquals("5", ((IntegerExpression)arguments.get(0)).getText());
	}
	
	@Test
	public void ArrayIndexingSelf() throws Exception {
		parse("self [5]");

		MethodCallExpression methodCallExpression = (MethodCallExpression)result.getBody();
		assertEquals(1, methodCallExpression.line());

		// self is being indexed (TODO: Currently null)
		SelfExpression selfExpression = (SelfExpression)methodCallExpression.getExpression();
		// TODO: assertEquals(1, selfExpression.line());
		
		// The method called is []
		assertEquals("[]", methodCallExpression.getMethodName());

		// 5 is the index
		SequenceExpression arguments = methodCallExpression.getArgs();
		assertEquals(1, arguments.size());
		assertEquals("5", ((IntegerExpression)arguments.get(0)).getText());
	}
	
	@Test
	public void methodCallWithNoArguments() throws Exception {
		parse("a.b");
		MethodCallExpression methodCallExpression = (MethodCallExpression) result.getBody();
		assertEquals(1, methodCallExpression.line());
		
		// No arguments
		assertNull(methodCallExpression.getArgs());
		
		// Receiver is a
		FunctionExpression receiver = (FunctionExpression) methodCallExpression.getExpression();
		assertEquals(1, receiver.line());
		assertEquals("a", receiver.getName());
		
		// Method is b
		assertEquals("b", methodCallExpression.getMethodName());
	}

	@Test
	public void methodCallWithArrayLiteral() throws Exception {
		parse("a([5])");
		assertMethodCallWithArrayLiteral();
	}
	
	@Test
	public void methodCallWithArrayLiteralAndNoParentheses() throws Exception {
		parse("a [5]");
		assertMethodCallWithArrayLiteral();
	}
	
	private void assertMethodCallWithArrayLiteral() {
		MethodCallExpression methodCallExpression = (MethodCallExpression)result.getBody();
		assertEquals(1, methodCallExpression.line());

		// No expression in the method call
		assertNull(methodCallExpression.getExpression());
		
		// The method called is a
		assertEquals("a", methodCallExpression.getMethodName());

		// [5] is the argument
		SequenceExpression arguments = methodCallExpression.getArgs();
		assertEquals(1, arguments.size());
		ArrayExpression arrayExpression = (ArrayExpression) arguments.get(0);
		assertEquals(1, arrayExpression.line());
		SequenceExpression sequenceExpression = arrayExpression.getExpression();
		assertEquals(1, sequenceExpression.line());
		assertEquals(1, sequenceExpression.size());
		assertEquals("5", ((IntegerExpression)sequenceExpression.get(0)).getText());
	}
	
	@Test
	@Ignore // Just cannot make this work!!!
	public void negativeFloatLiteral() throws Exception {
		parse("-34.56");
		FloatExpression floatExpression = (FloatExpression) result.getBody();
		assertEquals(1, floatExpression.line());
		assertEquals("-34.56", floatExpression.getText());
	}
	
	@Test
	public void unaryMinus() throws Exception {
		parse("-(34.56)");
		// TODO: Check the result
		System.out.println(result);
	}
	
	@Test
	public void unaryMinusWithMethodCall() throws Exception {
		parse("-34.56.abs");
		// TODO: Check the result
		System.out.println(result);
	}
	
	@Test
	public void additionWithSelf() throws Exception {
		parse("0 - self");
		// TODO: Check the result
		System.out.println(result);
	}
	
	private void parse(String s) throws Exception {
		StringReader reader = new StringReader(s);
		RubyParser parser = new RubyParser(reader, "");
		result = parser.program();
	}
}
