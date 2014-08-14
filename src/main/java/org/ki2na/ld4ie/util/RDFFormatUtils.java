package org.ki2na.ld4ie.util;

import java.io.StringWriter;

import org.openrdf.model.Statement;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.Rio;

/**
 * RDF Format utilities.
 * 
 * @author Emir Munoz (Emir.Munoz@ie.fujitsu.com)
 * @since 14/08/2014
 * @version 0.0.1
 * 
 */
public class RDFFormatUtils
{

	/**
	 * Dumps the extracted model in <i>Turtle</i> format.
	 * 
	 * @return a string containing the model in Turtle.
	 * @throws RepositoryException
	 */
	public static String dumpModelToTurtle(RepositoryConnection conn) throws RepositoryException
	{
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
	public static String dumpModelToNQuads(RepositoryConnection conn) throws RepositoryException
	{
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
	public static String dumpModelToRDFXML(RepositoryConnection conn) throws RepositoryException
	{
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
	 * Dumps the extracted model into a list of human readable statements.
	 * 
	 * @return string containing human readable statements.
	 * @throws RepositoryException
	 */
	public static String dumpHumanReadableTriples(RepositoryConnection conn) throws RepositoryException
	{
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

}
