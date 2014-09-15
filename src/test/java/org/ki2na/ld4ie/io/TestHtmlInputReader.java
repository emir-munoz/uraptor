package org.ki2na.ld4ie.io;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Test class for HTML Input Reader.
 * 
 * @author Emir Munoz (Emir.Munoz@ie.fujitsu.com)
 * @since 13/08/2014
 * @version 0.0.1
 * 
 */
public class TestHtmlInputReader
{

	@Test
	@Ignore
	public void readData()
	{
		HtmlInputReader reader = new HtmlInputReader("data/train1.html.txt.gz");
		reader.read();

		System.out.println(reader.getCount() + " documents found!");
		System.out.println(reader.get(0));
	}

}
