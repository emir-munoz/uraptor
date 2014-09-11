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
 * <a href=
 * "https://github.com/rorygibson/jsoup/blob/master/src/main/java/org/jsoup/safety/Whitelist.java"
 * >link</a>
 * 
 * <a href=
 * "https://github.com/jhy/jsoup/blob/master/src/test/java/org/jsoup/safety/CleanerTest.java//"
 * >link</a>
 * 
 * Using Whitelist.relaxed we cover the following HTML tags: "a", "b", "blockquote", "br",
 * "caption", "cite", "code", "col", "colgroup", "dd", "div", "dl", "dt", "em", "h1", "h2", "h3",
 * "h4", "h5", "h6", "i", "img", "li", "ol", "p", "pre", "q", "small", "strike", "strong", "sub",
 * "sup", "table", "tbody", "td", "tfoot", "th", "thead", "tr", "u", "ul"
 * 
 * @author Emir Munoz (Emir.Munoz@ie.fujitsu.com)
 * @version 0.0.6
 * @since 01/09/2014
 * 
 */
public class HtmlCleaner
{

	/** White list to use */
	private final Whitelist wl = Whitelist.relaxed().preserveRelativeLinks(false);

	/**
	 * Class constructor.
	 */
	public HtmlCleaner()
	{
		String[] tags = new String[] { "title", "span" };
		wl.addTags(tags);
		wl.addProtocols("a", "href", "javascript", "skype", "ymsgr", "msnim", "aim", "tel", "callto");
		wl.addProtocols("img", "src", "data");
		wl.addAttributes(":all", "class");
	}

	/**
	 * Clean HTML string and return the cleaner version.
	 * 
	 * @param html Input HTML string.
	 * @return Cleaned version of the HTML as string.
	 */
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

	/**
	 * Clean the HTML string and return a document.
	 * 
	 * @param html Input HTML string.
	 * @param baseUri Base URI of the document.
	 * @return Cleaned version of the HTML as document.
	 */
	public Document clean(String html, String baseUri)
	{
		// Parser str into a Document
		Document doc = Jsoup.parse(html, baseUri);
		// Clean the document
		doc = new Cleaner(wl).clean(doc);
		// Adjust escape mode
		doc.outputSettings().escapeMode(EscapeMode.xhtml);

		// Get back the string of the Document
		return doc;
	}

	/**
	 * Annotate the HTML according to vCard properties.
	 * 
	 * @param html Input HTML string.
	 * @param baseUri Base URI of the document.
	 * @return Annotated HTML as string.
	 */
	public String annotate(String html, String baseUri)
	{
		// Parser str into a Document
		Document doc = clean(html, baseUri);
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

		return doc.html();
	}

}
