package org.ki2na.ld4ie.evaluation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import arq.cmdline.CmdGeneral;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;

/**
 * This class can be used as a command line tool to validate the output of the MicroRaptor system in
 * the context of the LD4IE 2014 challenge
 * 
 * @author Pierre-Yves Vandenbussche
 */
public class ChallengeValidator extends CmdGeneral
{

	/** class logger */
	private final static Logger log = LoggerFactory.getLogger(ChallengeValidator.class);
	private String participantNqFilePath;
	private String targetNqFilePath;

	/**
	 * Main method.
	 */
	public static void main(String[] args)
	{
		new ChallengeValidator(args).mainRun();
	}

	public ChallengeValidator(String[] args)
	{
		super(args);
		getUsage().startCategory("Arguments");
		getUsage().addUsage("participantNqFilePath", "absolute path for the participant nquad file");
		getUsage().addUsage("targetNqFilePath", "absolute path for the target nquad file");
	}

	@Override
	protected String getCommandName()
	{
		return "challengeValidator";
	}

	@Override
	protected String getSummary()
	{
		return getCommandName() + " participantNqFilePath targetNqFilePath";
	}

	@Override
	protected void processModulesAndArgs()
	{
		if (getPositional().size() < 2)
		{
			doHelp();
		}
		participantNqFilePath = getPositionalArg(0);
		targetNqFilePath = getPositionalArg(1);
	}

	@Override
	protected void exec()
	{
		try
		{
			log.info("####### <challengeValidator> #######");

			// Paths.get(participantNqFilePath).toUri().toString()
			Dataset datasetParticipant = RDFDataMgr.loadDataset(participantNqFilePath, Lang.NQUADS);
			// Paths.get(targetNqFilePath).toUri().toString()
			Dataset datasetTarget = RDFDataMgr.loadDataset(targetNqFilePath, Lang.NQUADS);
			@SuppressWarnings("deprecation")
			Dataset datasetNegatives = DatasetFactory.create();

			// get the list of model names from the participant Dataset
			int countModels = 0;
			int nbStatements = 0;
			List<String> modelNamesParticipant = new ArrayList<String>();
			for (Iterator<String> iterator = datasetParticipant.listNames(); iterator.hasNext();)
			{
				String modelName = (String) iterator.next();
				modelNamesParticipant.add(modelName);
				nbStatements += datasetParticipant.getNamedModel(modelName).size();
				countModels++;
			}

			// get the model names from the target dataset
			int countModelsTarget = 0;
			int nbStatementsTarget = 0;
			List<String> modelNamesTarget = new ArrayList<String>();
			for (Iterator<String> iterator = datasetTarget.listNames(); iterator.hasNext();)
			{
				String modelName = (String) iterator.next();
				modelNamesTarget.add(modelName);
				nbStatementsTarget += datasetTarget.getNamedModel(modelName).size();
				countModelsTarget++;
			}

			// do the diff
			int countPositives = 0;
			for (Iterator<String> iterator = modelNamesParticipant.iterator(); iterator.hasNext();)
			{
				String modelName = (String) iterator.next();
				Model negatives = ModelFactory.createDefaultModel();
				if (modelNamesTarget.contains(modelName))
				{// make sure the model is present in the target
					Model m = datasetTarget.getNamedModel(modelName);
					for (Iterator<Statement> iterator2 = datasetParticipant.getNamedModel(modelName).listStatements(); iterator2
							.hasNext();)
					{
						Statement st = (Statement) iterator2.next();
						try
						{
							if (askcontainsStatement(st, m))
								countPositives++;
							else
							{
								negatives.add(st);
							}
						} catch (Exception e)
						{
							e.printStackTrace();
						}
						continue;

					}
					datasetNegatives.addNamedModel(modelName, negatives);
				} else
				{// models not present in the target all negatives
					datasetNegatives.addNamedModel(modelName, datasetParticipant.getNamedModel(modelName));
				}

			}

			try
			{
				File outputFile = new File("./outputNegatives.nq");
				if (outputFile.exists())
					outputFile.delete();
				outputFile.createNewFile();
				FileOutputStream fop = new FileOutputStream(outputFile);
				RDFDataMgr.write(fop, datasetNegatives, RDFFormat.NQ);
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}

			log.info("## Participant Dataset");
			log.info("Nb models: " + countModels);
			log.info("Nb statements: " + nbStatements);
			log.info("");
			log.info("## Target Dataset");
			log.info("Nb models: " + countModelsTarget);
			log.info("Nb statements: " + nbStatementsTarget);
			log.info("");
			log.info("Nb positive statements: " + countPositives);
			// nb false statements = nb of retrieved participant statements - positive ones
			log.info("Nb false statements: " + (nbStatements - countPositives));
			// precision = nb of positive statements / nb of retrieved participant statements
			float precision = new Float(countPositives) / new Float(nbStatements);
			log.info("Precision: " + precision);
			// recall = nb of positive statements / nb of targeted correct statements
			float recall = new Float(countPositives) / new Float(nbStatementsTarget);
			log.info("Recall: " + recall);
			// f measure = 2* (precision*recall)/(precision+recall)
			log.info("F-measure: " + 2 * (precision * recall) / (precision + recall));
			log.info("####### </challengeValidator> #######");
		} catch (Exception e)
		{
			cmdError("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public boolean askcontainsStatement(Statement st, Model m)
	{
		String q = "ASK{?subject ?predicate ?object.";
		if (st.getSubject().isAnon())
			q += " FILTER isBlank(?subject)";
		if (st.getObject().isAnon())
			q += " FILTER isBlank(?object)";
		q += "}";

		QuerySolutionMap args = new QuerySolutionMap();
		if (!st.getSubject().isAnon())
			args.add("subject", st.getSubject());
		if (!st.getObject().isAnon())
			args.add("object", st.getObject());
		QueryExecution qe = QueryExecutionFactory.create(q, m, args);

		return qe.execAsk();
	}

}
