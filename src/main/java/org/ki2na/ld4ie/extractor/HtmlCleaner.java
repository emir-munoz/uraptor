package org.ki2na.ld4ie.extractor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

/**
 * Cleaner process for HTML pages. Using the JSOUP class <code>WhiteList</code>
 * (http://jsoup.org/apidocs/org/jsoup/safety/Whitelist.html)
 * 
 * Based on <a
 * href="http://stackoverflow.com/questions/8683018/jsoup-clean-without-adding-html-entities"
 * >link</a>
 * 
 * @author Emir Munoz (Emir.Munoz@ie.fujitsu.com)
 * @version 0.0.1
 * @since 01/09/2014
 * 
 */
public class HtmlCleaner
{

	private final Whitelist wl = Whitelist.relaxed();

	/**
	 * Class constructor.
	 */
	public HtmlCleaner()
	{
		String[] tags = new String[] { "title", "span" };
		wl.addTags(tags);
		wl.addAttributes(":all", "class");
	}

	public String clean(String html)
	{
		// Parser str into a Document
		Document doc = Jsoup.parse(html);
		// Clean the document
		doc = new Cleaner(wl).clean(doc);
		// Adjust escape mode
		doc.outputSettings().escapeMode(EscapeMode.xhtml);

		// Get back the string of the Document
		return doc.html();
	}

	public Document clean2(String html)
	{
		// Parser str into a Document
		Document doc = Jsoup.parse(html);
		// Clean the document
		doc = new Cleaner(wl).clean(doc);

		// Get back the string of the Document
		return doc;
	}

	public String annotate(String html)
	{
		// Parser str into a Document
		Document doc = clean2(html);
		// Visit all nodes and look for properties
		Elements vcards = doc.select("[class*=vcard]");
		// add slot attribute
		for (Element vcard : vcards)
		{
			vcard.attr("slot", "vcard");
		}

		Elements fns = doc.select("[class*=fn]");
		// add slot attribute
		for (Element fn : fns)
		{
			fn.attr("slot", "fn");
		}

		// Adjust escape mode
		doc.outputSettings().escapeMode(EscapeMode.xhtml);

		return doc.html();
	}

}
