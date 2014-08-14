package org.ki2na.ld4ie.extractor;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.ki2na.ld4ie.io.HtmlInputReader;
import org.openrdf.repository.RepositoryException;
import org.openrdf.sail.SailException;

/**
 * hCard extractor test.
 * 
 * @author Emir Munoz (Emir.Munoz@ie.fujitsu.com)
 * @since 14/08/2014
 * @version 0.0.1
 * 
 */
public class TestHCardExtractor
{

	/** hCard extractor class */
	private HCardExtractor hCard;

	/**
	 * Instance of hCard extractor.
	 * 
	 * @throws SailException
	 * @throws RepositoryException
	 * @throws IOException
	 */
	@Before
	public void setUp() throws SailException, RepositoryException, IOException
	{
		hCard = new HCardExtractor();
	}

	@Test
	public void extract() throws RepositoryException
	{
		// HCardExtractor hCard = new HCardExtractor();

		HtmlInputReader reader = new HtmlInputReader("data/train1.html.txt.gz");
		reader.read();
		System.out.println(reader.getCount() + " documents found!");
		// System.out.println(reader.get(2));

		// String content = hCard.readFile("data/23-abbr-title-everything.html",
		// Charset.forName("UTF-8"));
		// String content = hCard.readFile("data/example-mf-hcard.html", Charset.forName("UTF-8"));
		// System.out.println(content);
		// URI baseURI = new URI("http://example.com/");
		// hCard.extract(content, baseURI);

		// test extraction for first document
		int index = 2;
		hCard.extract(reader.get(index).getContent(), reader.get(index).getURI());

		if (hCard.isModelEmpty())
			System.out.println("model is empty");

		// hCard.showStatements();
		System.out.println(hCard.dumpModelToNQuads());

		// close
		hCard.tearDown();
	}
	
}
