/*
 [The "BSD licence"]
 Copyright (c) 2005-2006 Xue Yong Zhi (http://seclib.blogspot.com)
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.
 3. The name of the author may not be used to endorse or promote products
    derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.trifork.hotruby.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import antlr.RecognitionException;
import antlr.TokenStreamException;

public class RubyParserSmokeTest
{
	public static void main(String[] args)
	{
		if (args.length < 1)
		{
			System.out.println("Need to spefic a dir name!");
			return;
		}

		RubyParserSmokeTest test = new RubyParserSmokeTest();

		for (int i = 0; i < args.length; ++i)
		{
			test.run(new File(args[i]));
		}

		System.out.println(test.getNumberOfFiles() + " ruby programs have been parsed, " + test.getNumberOfFails() + " failed.");
	}

	private int numberOfFiles = 0;
	private int numberOfFails = 0;

	public int  getNumberOfFiles()
	{
		return numberOfFiles;
	}

	public int  getNumberOfFails()
	{
		return numberOfFails;
	}

	public void run(File f)
	{
		// If this is a directory, walk each file/dir in that directory
		if (f.isDirectory())
		{
			String files[] = f.list();
			for (int i=0; i < files.length; i++)
			{
				run(new File(f, files[i]));
			}
		}
		else if ((f.getName().length()>3) &&
				f.getName().substring(f.getName().length()-3).equals(".rb"))
		{
			++numberOfFiles;
			parseFile(f);
		}
	}

	private void parseFile(File f)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(f));
			RubyParser parser = null;
			
			try
			{
				parser = new RubyParser(reader, f.getPath());
				parser.program();
			}
			catch (TokenStreamException e)
			{
				System.out.println("lexer exception for " + f.getPath() + ": " + e);
				++numberOfFails;
				
			}
			catch (RecognitionException e)
			{
				e.printStackTrace();
				System.out.println("parser exception for " + f.getPath() + ": " + e);
				// System.exit(1);
				++numberOfFails;
			}
			catch (NullPointerException e) {
				e.printStackTrace();
				System.out.println("null:");
			}
			finally
			{
				reader.close();
			}
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Can not open" + f.getPath());
			++numberOfFails;	
		}
		catch (IOException e)
		{
			System.out.println("Can not close" + f.getPath());
		}
	}
}

