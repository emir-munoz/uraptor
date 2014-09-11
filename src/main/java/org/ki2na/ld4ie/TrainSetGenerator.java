package org.ki2na.ld4ie;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.ki2na.ld4ie.extractor.HtmlCleaner;
import org.ki2na.ld4ie.io.HtmlInputReader;

/**
 * Generate the training set corpus.
 * 
 * @author Emir Munoz (Emir.Munoz@ie.fujitsu.com)
 * @version 0.0.1
 * @since 01/09/2014
 * 
 */
public class TrainSetGenerator
{

	/**
	 * Main method.
	 */
	public static void main(String[] args) throws IOException
	{
		HtmlCleaner cleaner = new HtmlCleaner();
		HtmlInputReader reader = new HtmlInputReader("data/train1.html.txt.gz");
		reader.read();
		System.out.println(reader.getCount() + " documents found!");

		File htmlTemplateFile;
		for (int i = 0; i < reader.getCount(); i++)
		{
			System.out.println(reader.get(i));
			htmlTemplateFile = new File("./data/trainCorpus3/" + i + ".html");
			FileUtils.writeStringToFile(htmlTemplateFile,
					cleaner.annotate(reader.get(i).getContent(), reader.get(i).getURI().toString()));
		}
	}

}
