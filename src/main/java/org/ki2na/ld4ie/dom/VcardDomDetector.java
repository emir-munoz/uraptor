package org.ki2na.ld4ie.dom;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.ki2na.ld4ie.extractor.HCardExtractor;
import org.ki2na.ld4ie.extractor.HtmlCleaner;
import org.ki2na.ld4ie.extractor.model.CssRule;
import org.ki2na.ld4ie.io.HtmlInputReader;
import org.ki2na.ld4ie.io.model.CrawledHtmlPage;
import org.openrdf.repository.RepositoryException;
import org.openrdf.sail.SailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.internal.Lists;

/**
 * Detector of vCards in HTML pages.
 * 
 * @author Emir Munoz (Emir.Munoz@ie.fujitsu.com)
 * @version 0.1.4
 * @since 04/09/2014
 * 
 */
public class VcardDomDetector
{

	/** class logger */
	private final static transient Logger _log = LoggerFactory.getLogger(VcardDomDetector.class.getName());
	/** HTML cleaner */
	private final HtmlCleaner cleaner = new HtmlCleaner();
	/** HTML reader */
	private HtmlInputReader reader;
	/** List to represent rules */
	private List<CssRule> rules;

	/**
	 * Class constructor. Read documents and initialise rules.
	 * 
	 * @param filename Dataset to mine.
	 */
	public VcardDomDetector(String filename)
	{
		reader = new HtmlInputReader(filename);
		rules = Lists.newArrayList();
		initRules();
	}

	/**
	 * Load the rules in the rules list. Rules can be read from a file.
	 */
	private void initRules()
	{
		List<CssRule> _rules = Lists.newArrayList();
		// -------------#8
		List<TagAnnotation> tagAnn = Lists.newArrayList();
		tagAnn.clear();
		tagAnn.add(new TagAnnotation("div", 1, "vcard"));
		tagAnn.add(new TagAnnotation("div > a", 1, "url"));
		tagAnn.add(new TagAnnotation("div > a > span", 2, "fn"));
		tagAnn.add(new TagAnnotation("div > a > span", 3, "adr"));
		tagAnn.add(new TagAnnotation("div > a > span > span > span", 1, "street-address"));
		tagAnn.add(new TagAnnotation("div > a > span > span > span", 2, "locality"));
		tagAnn.add(new TagAnnotation("div > a > span > span > span", 3, "region"));
		_rules.add(new CssRule("div.name_seo_wp > a > span > span > span", 4, tagAnn)); // (8) -->
																						// includes
		// (6)
		// rules.add(new CssRule("div > a > span > span > span", 4)); // (8) --> too generic
		// -------------#6
		tagAnn = Lists.newArrayList();
		tagAnn.add(new TagAnnotation("div", 1, "vcard"));
		tagAnn.add(new TagAnnotation("div > a > span", 2, "fn"));
		tagAnn.add(new TagAnnotation("div > a > span > span", 1, "email"));
		_rules.add(new CssRule("div.name_seo_wp > a > span ~ span > span", 3, tagAnn)); // (6)
		// rules.add(new CssRule("div > a > span + span > span", 3)); // (6) --> too generic
		// -------------#14
		tagAnn = Lists.newArrayList();
		tagAnn.add(new TagAnnotation("div", 1, "vcard"));
		tagAnn.add(new TagAnnotation("div > img", 1, "photo"));
		tagAnn.add(new TagAnnotation("div > cite", 1, "fn"));
		tagAnn.add(new TagAnnotation("div > cite > a", 1, "url"));
		_rules.add(new CssRule("div.comment-author > img + cite > a", 2, tagAnn)); // (14) -->
																					// includes (9)
		// -------------#1
		tagAnn = Lists.newArrayList();
		tagAnn.add(new TagAnnotation("div", 1, "vcard"));
		tagAnn.add(new TagAnnotation("div > span > a > img", 2, "photo"));
		_rules.add(new CssRule("div > span > a > img + img", 3, tagAnn)); // (1)
		tagAnn = Lists.newArrayList();
		tagAnn.add(new TagAnnotation("div", 1, "vcard"));
		tagAnn.add(new TagAnnotation("div > p > span", 1, "fn"));
		tagAnn.add(new TagAnnotation("div > p > span > a", 1, "url"));
		_rules.add(new CssRule("div.comment-author > p > span > a", 3, tagAnn)); // (1) ######
		// rules.add(new CssRule("div.avatar-image-container > span > a > img + img", 3)); // (1)
		// optional
		// -------------#4
		tagAnn = Lists.newArrayList();
		tagAnn.add(new TagAnnotation("div", 1, "vcard"));
		tagAnn.add(new TagAnnotation("div > img", 1, "photo"));
		tagAnn.add(new TagAnnotation("div > cite", 1, "fn"));
		_rules.add(new CssRule("div.comment-author > img + cite + span", 1, tagAnn)); // (4)
		// rules.add(new CssRule("div > img + cite + span", 1)); // (4) --> too general
		// -------------#15
		tagAnn = Lists.newArrayList();
		tagAnn.add(new TagAnnotation("div", 1, "vcard"));
		tagAnn.add(new TagAnnotation("div > cite", 1, "fn"));
		_rules.add(new CssRule("div.comment-author > img + cite", 1, tagAnn)); // (15) --> includes
																				// (14)
		// -------------#9
		tagAnn = Lists.newArrayList();
		tagAnn.add(new TagAnnotation("div", 1, "vcard"));
		tagAnn.add(new TagAnnotation("div > cite", 1, "fn"));
		_rules.add(new CssRule("div.comment-author > cite + span", 1, tagAnn)); // (9)
		_rules.add(new CssRule("div.social-comment-author > cite + span", 1, tagAnn)); // (9)
		// -------------#10
		tagAnn = Lists.newArrayList();
		tagAnn.add(new TagAnnotation("div", 1, "vcard"));
		tagAnn.add(new TagAnnotation("div > div", 1, "fn org"));
		_rules.add(new CssRule("div.source-org > div", 1, tagAnn)); // (10) OK
		_rules.add(new CssRule("div.author > div", 1, tagAnn)); // (10) OK
		// -------------#5
		tagAnn = Lists.newArrayList();
		tagAnn.add(new TagAnnotation("span", 1, "vcard"));
		tagAnn.add(new TagAnnotation("span > span", 1, "fn"));
		tagAnn.add(new TagAnnotation("span > span > a", 1, "author profile"));
		// _rules.add(new CssRule("span > span > a > span", 3)); // (5)
		_rules.add(new CssRule("span.post-author > span > a > span", 3, tagAnn)); // (5) optional
		// -------------#12
		tagAnn = Lists.newArrayList();
		tagAnn.add(new TagAnnotation("span", 1, "vcard"));
		tagAnn.add(new TagAnnotation("span > span", 1, "fn"));
		// _rules.add(new CssRule("span > span > a", 2)); // (12) --> includes (5) --> too generic
		_rules.add(new CssRule("span.author > span > a", 2, tagAnn)); // (12)
		// -------------#13
		tagAnn = Lists.newArrayList();
		tagAnn.add(new TagAnnotation("span", 1, "vcard"));
		tagAnn.add(new TagAnnotation("span > a", 1, "url fn name"));
		_rules.add(new CssRule("span.author > a > span", 2, tagAnn)); // (13)
		// rules.add(new CssRule("span > a > span", 3)); // (13) // --> too generic and only few
		// cases
		// -------------#2
		tagAnn = Lists.newArrayList();
		tagAnn.add(new TagAnnotation("span", 1, "vcard"));
		tagAnn.add(new TagAnnotation("span > a", 1, "fn"));
		_rules.add(new CssRule("span.author > a", 1, tagAnn)); // (2)
		_rules.add(new CssRule("span.entry-author > a", 1, tagAnn)); // (2) optional
		_rules.add(new CssRule("span.author-name > a", 1, tagAnn)); // (2) optional
		_rules.add(new CssRule("span.source-org > a", 1, tagAnn)); // (2) optional
		// -------------#3
		tagAnn = Lists.newArrayList();
		tagAnn.add(new TagAnnotation("span", 1, "vcard"));
		tagAnn.add(new TagAnnotation("span > span", 1, "fn"));
		// rules.add(new CssRule("span > span", 1)); // (3) --> too generic
		_rules.add(new CssRule("span.author > span", 1, tagAnn)); // (3) -->includes (5)(11)(12)
		_rules.add(new CssRule("span.post-author > span", 1, tagAnn)); // (3) optional
		_rules.add(new CssRule("span.source-org > span", 1, tagAnn)); // (3) optional
		// -------------#7
		// rules.add(new CssRule("span", 1)); // (7) --> too generic
		// -------------#11
		// rules.add(new CssRule("span > span", 2)); // (11) --> same as (3)
		rules.addAll(_rules);
	}

	/**
	 * Read the collection of documents.
	 */
	public void readCollection()
	{
		reader.read();
		_log.info(reader.getCount() + " documents found!");
	}

	/**
	 * Check a document against all the defined rules.
	 * 
	 * @param page Document page.
	 * @param writer Output writer.
	 * @param i Iteration number (for evaluation).
	 */
	public String checkDocument(CrawledHtmlPage page, PrintWriter writer, int i)
	{
		_log.info("Extracting microformats from document #" + i + " - \tURL=" + page.getURI().toString());
		writer.println("===> " + i + "\t" + page.getURI());

		// get a cleaner version of the original document
		Document doc = cleaner.clean(page.getContent(), page.getURI().toString());

		// for each document evaluate all the rules
		for (CssRule rule : rules)
		{
			// get all vCard candidates for this CSS selector
			Elements vcardCandidates = doc.select(rule.getPath());

			// evaluate each candidate
			for (Element vcardCandidate : vcardCandidates)
			{
				// System.out.println(vcardCandidate + " RULE=" + rule.getPath() + " DEPTH=" +
				// rule.getDepth());

				// go up to the parent of the vCard
				Element parent = vcardCandidate.parent();
				for (int x = 0; x < rule.getDepth() - 1; x++)
					parent = parent.parent();

				// validate that the element is a <div> or <span> (optional). Annotate the vCard
				// candidate with the properties indicated by the rule.
				if (parent.nodeName().equals("div") || parent.nodeName().equals("span"))
				{
					// if the vCard has not been visited yet; else vCard visited already
					if (!parent.hasClass("em_visited"))
					{
						// System.out.println(parent);
						parent.addClass("em_visited");
						String htmlAnnotated = annotateVcard(parent, rule);
						parent.html(htmlAnnotated);
						writer.println(parent);
					}
				}
			}
		}

		return doc.html();
	}

	/**
	 * Perform the annotations as indicated by the rules.
	 * 
	 * @param parent Candidate vCard parent element.
	 * @param rule CssRule to apply.
	 * @return parent node as HTML with the annotations.
	 */
	public String annotateVcard(Element parent, CssRule rule)
	{
		List<TagAnnotation> anList = rule.getAnnotations();
		Elements root;
		Element ele;

		// for each tag annotation
		for (TagAnnotation annotation : anList)
		{
			root = parent.select(annotation.getSelector());
			ele = root.get(annotation.getPosition() - 1);
			ele.addClass(annotation.getProperty());
		}

		return parent.html();
	}

	/**
	 * Extract the vCards from webpages.
	 * 
	 * @param extlog Extraction log writer.
	 * @param nquads N-Quads writer.
	 * @throws RepositoryException
	 */
	public void runExtraction(PrintWriter extlog, PrintWriter nquads) throws RepositoryException
	{
		// For each document check all rules
		// int numDocs = 1481;
		int numDocs = reader.getCount();
		HCardExtractor hCard;
		String htmlAnnotated;
		for (int i = 0; i < numDocs; i++)
		{
			try
			{
				hCard = new HCardExtractor();
				// System.out.println(reader.get(i)); // original HTML
				htmlAnnotated = checkDocument(reader.get(i), extlog, i);
				// System.out.println(htmlAnnotated); // annotated HTML
				hCard.extract(htmlAnnotated, reader.get(i).getURI());
				nquads.print(hCard.dumpModelToNQuads());
				// hCard.tearDown(); // slow down the process
			} catch (SailException e)
			{
				_log.error("SailException creating HCardExtractor");
			} catch (RepositoryException e)
			{
				_log.error("RepositoryException creating HCardExtractor");
			} catch (IOException e)
			{
				_log.error("IOException creating HCardExtractor");
			}
			extlog.flush();
			nquads.flush();
		}
	}

	/**
	 * Main method.
	 * 
	 * @throws FileNotFoundException
	 * @throws RepositoryException
	 */
	public static void main(String[] args) throws FileNotFoundException, RepositoryException
	{
		// String filename = "data/train1.html.txt.gz";
		// String filename = "data/train4.html.txt.gz";
		String filename = "data/test.clean.html.txt.gz";

		_log.info("Starting the extraction from file {}", filename);
		VcardDomDetector dd = new VcardDomDetector(filename);
		dd.readCollection();

		PrintWriter extlog = new PrintWriter("./data/test.out.txt");
		PrintWriter nquads = new PrintWriter("./data/nquads/test.out.nq");
		dd.runExtraction(extlog, nquads);

		extlog.close();
		nquads.close();

		_log.info("Extraction finished");
	}

}
