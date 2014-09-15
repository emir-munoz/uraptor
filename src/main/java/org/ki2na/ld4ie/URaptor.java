package org.ki2na.ld4ie;

import java.io.FileNotFoundException;

import org.ki2na.ld4ie.dom.VcardDomDetector;
import org.ki2na.ld4ie.evaluation.ChallengeValidator;
import org.openrdf.repository.RepositoryException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

/**
 * Command line reader.
 * 
 * @author Emir Munoz (Emir.Munoz@ie.fujitsu.com)
 * @version 0.0.1
 * 
 */
public class URaptor
{

	@Parameter(names = "-extract", description = "Run microformat extraction")
	public boolean extract;

	@Parameter(names = "-input", description = "[Extract] Input HTMLs file")
	public String htmlInput = null;

	@Parameter(names = "-output", description = "[Extract] Output N-Quads file")
	public String nquadsOutput = null;

	@Parameter(names = "-evaluate", description = "Run evaluation of RDF n-quad files")
	public boolean evaluate;

	@Parameter(names = "-gold", description = "[Evaluate] Gold standard n-quad file")
	public String goldStandardFile = null;

	@Parameter(names = "-model", description = "[Evaluate] Extracted n-quad file")
	public String extractionFile = null;

	@Parameter(names = { "-h", "--help" }, description = "uRaptor help")
	public boolean help;

	/**
	 * Run extraction of microformats.
	 */
	public void extract()
	{
		try
		{
			if (htmlInput != null && nquadsOutput != null)
				VcardDomDetector.main(new String[] { htmlInput, nquadsOutput });
			else
				System.out.println("Please introduce a value for -input and -output parameters");
		} catch (FileNotFoundException e)
		{
			System.out.println("Input file not found.");
		} catch (RepositoryException e)
		{
			System.out.println("N-Quads file cannot be created.");
		}
	}

	/**
	 * Evaluate the models.
	 */
	public void evaluate()
	{
		if (extractionFile != null && goldStandardFile != null)
			ChallengeValidator.main(new String[] { extractionFile, goldStandardFile });
		else
			System.out.println("Please introduce a value for -model and -gold parameters");
	}

	/**
	 * Main method.
	 */
	public static void main(String[] args)
	{
		URaptor raptor = new URaptor();
		JCommander com = new JCommander(raptor, args);
		com.setProgramName("uRaptor");
		if (raptor.help)
		{
			com.usage();
			System.exit(1);
		}
		// check that variables are not null
		if (raptor.extract == false && raptor.evaluate == false)
		{
			com.usage();
			System.exit(1);
		}
		// determine task to execute
		if (raptor.extract)
		{
			raptor.extract();
		} else if (raptor.evaluate)
		{
			raptor.evaluate();
		} else
		{
			System.out.println("Please indicate one task to execute");
			com.usage();
			System.exit(1);
		}
	}

}
