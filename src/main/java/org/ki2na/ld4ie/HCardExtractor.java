package org.ki2na.ld4ie;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Scanner;

import org.apache.any23.extractor.ExtractionException;
import org.apache.any23.extractor.SingleDocumentExtraction;
import org.apache.any23.extractor.html.HCardExtractorFactory;
import org.apache.any23.source.StringDocumentSource;
import org.apache.any23.writer.RepositoryWriter;
import org.ki2na.ld4ie.io.HtmlInputReader;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.Rio;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailException;
import org.openrdf.sail.memory.MemoryStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extractor for the <a href="http://microformats.org/wiki/hcard">hCard</a> microformat.
 * 
 * <p>
 * Main references from:
 * <ul>
 * <li>https://github.com/apache/any23/blob/master/core/src/test/java/org/apache/any23/
 * AbstractAny23TestBase.java</li>
 * <li>
 * https://github.com/apache/any23/blob/master/core/src/test/java/org/apache/any23/extractor/html
 * /AbstractExtractorTestCase.java</li>
 * <li>https://code.google.com/p/any23/source/browse/trunk/any23-core/src/main/java/org/deri/any23/
 * extractor/html/HCardExtractor.java?r=1594</li>
 * <li>
 * https://github.com/apache/any23/tree/master/test-resources/src/test/resources/microformats/hcard</li>
 * <li>
 * https://apache.googlesource.com/any23/+/d4af4899142090e931ef4a8a1695270c5cae047c/core/src/test
 * /java/org/apache/any23/extractor/html/HCardExtractorTest.java</li>
 * </ul>
 * </p>
 * 
 * @author Emir Munoz (Emir.Munoz@ie.fujitsu.com)
 * @since 13/08/2014
 * @version 0.0.1
 * 
 */
public class HCardExtractor
{

	/** class logger */
	private final static transient Logger _log = LoggerFactory.getLogger(HCardExtractor.class.getName());
	// private static final VCard vVCARD = VCard.getInstance();
	/** Internal connection used to collect extraction results. */
	private RepositoryConnection conn;

	/**
	 * Constructor.
	 * 
	 * @throws SailException
	 * @throws RepositoryException
	 * @throws IOException
	 */
	public HCardExtractor() throws SailException, RepositoryException, IOException
	{
		Sail store = new MemoryStore();
		store.initialize();
		conn = new SailRepository(store).getConnection();
	}

	/**
	 * Extract the hCard metadata from the content.
	 * 
	 * @param content HTML page as string.
	 * @param baseURI URI of the web page.
	 */
	private void extract(String content, URI baseURI)
	{
		/* First method - using directly the string source */
		StringDocumentSource source = new StringDocumentSource(content, baseURI.toString());
		SingleDocumentExtraction ex = new SingleDocumentExtraction(source, new HCardExtractorFactory(),
				new RepositoryWriter(conn));

		/* Second method - using a temporal file in memory */
		// File tempFile = File.createTempFile("any23test-", "-.mdata");
		// FileOutputStream output = new FileOutputStream(tempFile);
		// InputStream input = new ByteArrayInputStream(content.getBytes(Charset.forName("UTF-8")));
		// IOUtils.copy(input, output);

		// SingleDocumentExtraction ex = new SingleDocumentExtraction(new
		// FileDocumentSource(tempFile),
		// new HCardExtractorFactory(), new RepositoryWriter(conn));

		try
		{
			ex.setMIMETypeDetector(null);
			ex.run();
		} catch (IOException e)
		{
			_log.info("Error reading the HTML content from '{}'", baseURI);
		} catch (ExtractionException e)
		{
			_log.info("Error in the microformat extraction from '{}'", baseURI);
		}
	}

	/**
	 * Returns all statements matching the pattern <code>(s p o)</code>.
	 * 
	 * @param s subject.
	 * @param p predicate.
	 * @param o object.
	 * @return list of statements.
	 * @throws RepositoryException
	 */
	protected RepositoryResult<Statement> getStatements(Resource s, org.openrdf.model.URI p, Value o)
			throws RepositoryException
	{
		return conn.getStatements(s, p, o, false);
	}

	/**
	 * Display all the extracted statements.
	 * 
	 * @throws RepositoryException
	 */
	private void showStatements() throws RepositoryException
	{
		RepositoryResult<Statement> statements = this.getStatements(null, null, null);

		if (statements.hasNext())
			System.out.println("Conn contains elements");
		else
			System.out.println("Conn does not contains elements");

		while (statements.hasNext())
			System.out.println(statements.next().toString());
	}

	/**
	 * Counts all statements matching the pattern <code>(s p o)</code>.
	 * 
	 * @param s subject.
	 * @param p predicate.
	 * @param o object.
	 * @return number of matches.
	 * @throws RepositoryException
	 */
	protected int getStatementsSize(Resource s, org.openrdf.model.URI p, Value o) throws RepositoryException
	{
		RepositoryResult<Statement> result = this.getStatements(s, p, o);
		int count = 0;
		try
		{
			while (result.hasNext())
			{
				result.next();
				count++;
			}
		} finally
		{
			result.close();
		}
		return count;
	}

	/**
	 * Check if the model is empty.
	 * 
	 * @throws RepositoryException
	 */
	public boolean isModelEmpty() throws RepositoryException
	{
		if (conn.isEmpty())
			return true;
		return false;
	}

	/**
	 * Read a file from path and convert it into string.
	 * 
	 * @param path path to the file.
	 * @param encoding encoding to use.
	 * @return A string version of the input file.
	 * @throws IOException
	 */
	public String readFile(String path, Charset encoding) throws IOException
	{
		return new Scanner(new File(path), "UTF-8").useDelimiter("\\A").next();
	}

	/**
	 * Dumps the extracted model in <i>Turtle</i> format.
	 * 
	 * @return a string containing the model in Turtle.
	 * @throws RepositoryException
	 */
	protected String dumpModelToTurtle() throws RepositoryException
	{
		System.out.println("### Dump Model To Turtle ###");
		StringWriter w = new StringWriter();
		try
		{
			conn.export(Rio.createWriter(RDFFormat.TURTLE, w));
			return w.toString();
		} catch (RDFHandlerException ex)
		{
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Dumps the extracted model in <i>NQuads</i> format.
	 * 
	 * @return a string containing the model in NQuads.
	 * @throws RepositoryException
	 */
	protected String dumpModelToNQuads() throws RepositoryException
	{
		System.out.println("### Dump Model To NQuads ###");
		StringWriter w = new StringWriter();
		try
		{
			conn.export(Rio.createWriter(RDFFormat.NQUADS, w));
			return w.toString();
		} catch (RDFHandlerException ex)
		{
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Dumps the extracted model in <i>RDFXML</i> format.
	 * 
	 * @return a string containing the model in RDFXML.
	 * @throws RepositoryException
	 */
	protected String dumpModelToRDFXML() throws RepositoryException
	{
		System.out.println("### Dump Model To RDF/XML ###");
		StringWriter w = new StringWriter();
		try
		{
			conn.export(Rio.createWriter(RDFFormat.RDFXML, w));
			return w.toString();
		} catch (RDFHandlerException ex)
		{
			throw new RuntimeException(ex);
		}
	}

	/**
	 * @return string containing human readable statements.
	 * @throws RepositoryException
	 */
	protected String dumpHumanReadableTriples() throws RepositoryException
	{
		System.out.println("### Dump Human Readable Triples ###");
		StringBuilder sb = new StringBuilder();
		RepositoryResult<Statement> result = conn.getStatements(null, null, null, false);
		while (result.hasNext())
		{
			Statement statement = result.next();
			sb.append(String.format("%s %s %s %s\n", statement.getSubject(), statement.getPredicate(),
					statement.getObject(), statement.getContext()));
		}
		return sb.toString();
	}

	/**
	 * Dumps the list of statements contained in the extracted model.
	 * 
	 * @return list of extracted statements.
	 * @throws RepositoryException
	 */
	@SuppressWarnings("deprecation")
	protected List<Statement> dumpAsListOfStatements() throws RepositoryException
	{
		return conn.getStatements(null, null, null, false).asList();
	}

	/**
	 * Close connection.
	 * 
	 * @throws RepositoryException
	 */
	public void tearDown() throws RepositoryException
	{
		conn.close();
		conn = null;
	}

	public static void main(String[] args) throws SailException, RepositoryException, IOException, URISyntaxException
	{
		HCardExtractor hCard = new HCardExtractor();

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
