// $ANTLR : "ruby.g" -> "RubyLexerBase.java"$

package com.trifork.hotruby.parser;

import com.trifork.hotruby.ast.*;

public interface RubyTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int RPAREN_IN_METHOD_DEFINITION = 4;
	int SYMBOL = 5;
	int SEMI = 6;
	int LINE_BREAK = 7;
	int IF_MODIFIER = 8;
	int UNLESS_MODIFIER = 9;
	int WHILE_MODIFIER = 10;
	int UNTIL_MODIFIER = 11;
	int RESCUE_MODIFIER = 12;
	int COMMA = 13;
	int LCURLY_BLOCK = 14;
	int RCURLY = 15;
	int REST_ARG_PREFIX = 16;
	int IDENTIFIER = 17;
	int ASSIGN_WITH_NO_LEADING_SPACE = 18;
	int CONSTANT = 19;
	int FUNCTION = 20;
	int GLOBAL_VARIBLE = 21;
	int LPAREN = 22;
	int RPAREN = 23;
	int BOR = 24;
	int LITERAL_in = 25;
	int LITERAL_end = 26;
	int LOGICAL_OR = 27;
	int COLON_WITH_NO_FOLLOWING_SPACE = 28;
	int INSTANCE_VARIBLE = 29;
	int CLASS_VARIBLE = 30;
	int UNARY_PLUS_MINUS_METHOD_NAME = 31;
	int DOT = 32;
	int COLON2 = 33;
	int LBRACK_ARRAY_ACCESS = 34;
	int LBRACK = 35;
	int RBRACK = 36;
	int LITERAL_return = 37;
	int LITERAL_break = 38;
	int LITERAL_next = 39;
	int LITERAL_retry = 40;
	int LITERAL_redo = 41;
	int LITERAL_do = 42;
	int BLOCK_ARG_PREFIX = 43;
	int ASSOC = 44;
	int LITERAL_nil = 45;
	int LITERAL_self = 46;
	int LITERAL_true = 47;
	int LITERAL_false = 48;
	int LITERAL___FILE__ = 49;
	int LITERAL___LINE__ = 50;
	int DOUBLE_QUOTE_STRING = 51;
	int SINGLE_QUOTE_STRING = 52;
	int STRING_BEFORE_EXPRESSION_SUBSTITUTION = 53;
	int STRING_BETWEEN_EXPRESSION_SUBSTITUTION = 54;
	int STRING_AFTER_EXPRESSION_SUBSTITUTION = 55;
	int DOUBLE_QUOTE_WARRAY = 56;
	int SINGLE_QUOTE_WARRAY = 57;
	int WARRAY_BEFORE_EXPRESSION_SUBSTITUTION = 58;
	int REGEX = 59;
	int REGEX_BEFORE_EXPRESSION_SUBSTITUTION = 60;
	int COMMAND_OUTPUT = 61;
	int COMMAND_OUTPUT_BEFORE_EXPRESSION_SUBSTITUTION = 62;
	int HERE_DOC_BEGIN = 63;
	int INTEGER = 64;
	int HEX = 65;
	int BINARY = 66;
	int OCTAL = 67;
	int FLOAT = 68;
	int ASCII_VALUE = 69;
	int LEADING_COLON2 = 70;
	int LITERAL_super = 71;
	int LITERAL_yield = 72;
	int ASSIGN = 73;
	int LCURLY_HASH = 74;
	int LITERAL_rescue = 75;
	int LITERAL_else = 76;
	int LITERAL_ensure = 77;
	int LITERAL_begin = 78;
	int LITERAL_if = 79;
	int LITERAL_elsif = 80;
	int LITERAL_unless = 81;
	int LITERAL_case = 82;
	int LESS_THAN = 83;
	int LEFT_SHIFT = 84;
	int RIGHT_SHIFT = 85;
	int EQUAL = 86;
	int CASE_EQUAL = 87;
	int GREATER_THAN = 88;
	int GREATER_OR_EQUAL = 89;
	int LESS_OR_EQUAL = 90;
	int PLUS = 91;
	int MINUS = 92;
	int STAR = 93;
	int DIV = 94;
	int MOD = 95;
	int POWER = 96;
	int BAND = 97;
	int BXOR = 98;
	int MATCH = 99;
	int COMPARE = 100;
	int BNOT = 101;
	int SINGLE_QUOTE = 102;
	int LOGICAL_AND = 103;
	int NOT = 104;
	int LITERAL_and = 105;
	int LITERAL_BEGIN = 106;
	int LITERAL_def = 107;
	// "defined?" = 108
	int LITERAL_END = 109;
	int LITERAL_or = 110;
	int LITERAL_module = 111;
	int LITERAL_until = 112;
	int LITERAL_then = 113;
	int LITERAL_when = 114;
	int LITERAL_for = 115;
	int LITERAL_while = 116;
	int LITERAL_alias = 117;
	int LITERAL_class = 118;
	int LITERAL_not = 119;
	int LITERAL_undef = 120;
	int COLON = 121;
	int DO_IN_CONDITION = 122;
	int QUESTION = 123;
	int PLUS_ASSIGN = 124;
	int MINUS_ASSIGN = 125;
	int STAR_ASSIGN = 126;
	int DIV_ASSIGN = 127;
	int MOD_ASSIGN = 128;
	int POWER_ASSIGN = 129;
	int BAND_ASSIGN = 130;
	int BXOR_ASSIGN = 131;
	int BOR_ASSIGN = 132;
	int LEFT_SHIFT_ASSIGN = 133;
	int RIGHT_SHIFT_ASSIGN = 134;
	int LOGICAL_AND_ASSIGN = 135;
	int LOGICAL_OR_ASSIGN = 136;
	int INCLUSIVE_RANGE = 137;
	int EXCLUSIVE_RANGE = 138;
	int NOT_EQUAL = 139;
	int NOT_MATCH = 140;
	int UNARY_PLUS = 141;
	int UNARY_MINUS = 142;
	int PURE_LINE_BREAK = 143;
	int LINE_FEED = 144;
	int REGEX_MODIFIER = 145;
	int SPECIAL_STRING = 146;
	int STRING_CHAR = 147;
	int HERE_DOC_CONTENT = 148;
	int HERE_DOC_DELIMITER = 149;
	int RDOC = 150;
	int ANYTHING_OTHER_THAN_LINE_FEED = 151;
	int LINE = 152;
	int ESC = 153;
	int IDENTIFIER_CONSTANT_AND_KEYWORD = 154;
	int UNDER_SCORE = 155;
	int FLOAT_WITH_LEADING_DOT = 156;
	int NON_ZERO_DECIMAL = 157;
	int OCTAL_CONTENT = 158;
	int HEX_CONTENT = 159;
	int BINARY_CONTENT = 160;
	int EXPONENT = 161;
	int COMMENT = 162;
	int WHITE_SPACE_CAHR = 163;
	int WHITE_SPACE = 164;
	int LINE_CONTINUATION = 165;
	int END_OF_FILE = 166;
}
