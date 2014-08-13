package org.ki2na.ld4ie;

import org.ki2na.ld4ie.io.HtmlInputReader;

/**
 * Hello world!
 * 
 */
public class App
{
	public static void main(String[] args)
	{
		HtmlInputReader reader = new HtmlInputReader("data/train1.html.txt.gz");
		reader.read();
		System.out.println(reader.getCount() + " documents found!");

		for (int i = 0; i < reader.getCount(); i++)
			System.out.println(reader.get(i));
		
	}
}
