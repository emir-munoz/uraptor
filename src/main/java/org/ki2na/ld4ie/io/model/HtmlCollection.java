package org.ki2na.ld4ie.io.model;

import java.util.ArrayList;

/**
 * Collection of crawled HTML pages.
 * 
 * @author Emir Munoz (Emir.Munoz@ie.fujitsu.com)
 * @since 13/08/2014
 * @version 0.0.2
 * 
 */
public class HtmlCollection extends ArrayList<CrawledHtmlPage>
{

	/** serial number */
	private static final long serialVersionUID = 5144106476020583481L;
	/** number of elements */
	private int count = 0;

	/**
	 * Add document to the collection.
	 * 
	 * @param page Document to be added.
	 */
	public boolean add(CrawledHtmlPage page)
	{
		count++;
		return super.add(page);
	}

	/**
	 * @return total number of documents in the list.
	 */
	public int getCount()
	{
		return count;
	}

	/**
	 * Return document at position <code>index</code>.
	 * 
	 * @param index Index of the document.
	 */
	public CrawledHtmlPage get(int index)
	{
		return super.get(index);
	}

}
