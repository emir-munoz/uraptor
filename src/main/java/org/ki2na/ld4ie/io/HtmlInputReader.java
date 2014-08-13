package org.ki2na.ld4ie.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.zip.GZIPInputStream;

import org.ki2na.ld4ie.model.CrawledHtmlPage;
import org.ki2na.ld4ie.model.CrawledHtmlPage.CrawledHtmlPageBuilder;
import org.ki2na.ld4ie.model.HtmlCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reader for the input data for training and testing.
 * 
 * <p>
 * <code>_*(.clean).html.txt.gz_</code>: These files include a collection of crawled HTML pages.
 * Each page is represented in the file in three following:
 * 
 * <ol>
 * <li><code>URI</code>: [URI of the crawled page]</li>
 * <li><code>Content-Type</code>: [Content Type including detect charset of the crawled page]</li>
 * <li><code>Content</code>: [The actual HTML content without line breaks. In case of the
 * <code>*.clean.*</code> version, annotations and comments where removed from the HTML code]</li>
 * </ol>
 * </p>
 * 
 * @author Emir Munoz (Emir.Munoz@ie.fujitsu.com)
 * @since 13/08/2014
 * @version 0.0.1
 * 
 */
public class HtmlInputReader
{

	/** class logger */
	private final static transient Logger _log = LoggerFactory.getLogger(HtmlInputReader.class.getName());
	/** input filename */
	private String filename;
	/** file encoding */
	private String encoding;
	/** collection of documents */
	private HtmlCollection docs;

	/**
	 * Constructor.
	 * 
	 * @param filename Path to the file.
	 */
	public HtmlInputReader(final String filename)
	{
		this.filename = filename;
		this.encoding = "UTF-8";
		docs = new HtmlCollection();
	}

	/**
	 * Constructor.
	 * 
	 * @param filename Path to the file.
	 * @param encoding Encoding to use.
	 */
	public HtmlInputReader(final String filename, final String encoding)
	{
		this.filename = filename;
		this.encoding = encoding;
		docs = new HtmlCollection();
	}

	/**
	 * Read the contents of the train/test file
	 */
	public void read()
	{
		_log.info("Reading from file '{}'...", filename);
		String NL = System.getProperty("line.separator");
		String line = null;
		try
		{
			Reader decoder = null;
			InputStream fileStream = null;
			/** set reader according to the extension */
			if (filename.endsWith(".gz"))
			{
				fileStream = new FileInputStream(filename);
				InputStream gzipStream = new GZIPInputStream(fileStream);
				decoder = new InputStreamReader(gzipStream, encoding);
			} else
			{
				decoder = new FileReader(filename);
			}

			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!
			BufferedReader input = new BufferedReader(decoder);
			CrawledHtmlPageBuilder doc = null;

			while ((line = input.readLine()) != null)
			{
				if (line.startsWith("URI:"))
				{
					// read the URI
					doc = new CrawledHtmlPage.CrawledHtmlPageBuilder(line.substring(5, line.length()));
				} else if (line.startsWith("Content-Type:"))
				{
					// read the content-type
					doc.contentType(line.substring(14, line.length()));
				} else if (line.startsWith("Content:"))
				{
					// read the HTML
					doc.content(line.substring(9, line.length()));
				} else if (line.equals(NL) || line.isEmpty())
				{
					// add new doc to the list
					docs.add(doc.build());
				}
			}
			input.close();
			fileStream.close();
		} catch (FileNotFoundException e)
		{
			_log.info("File not found in path {}", filename);
		} catch (IOException e)
		{
			_log.info("Error reading file {}", filename);
		} catch (URISyntaxException e)
		{
			_log.info("Input URI='{}' with syntax errors", line);
		}
	}

	public int getCount()
	{
		return this.docs.getCount();
	}

	public CrawledHtmlPage get(int index)
	{
		return this.docs.get(index);
	}

}
