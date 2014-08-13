package org.ki2na.ld4ie.model;

import java.util.ArrayList;

/**
 * Collection of CrawledHtmlPages.
 * 
 * @author Emir Munoz (Emir.Munoz@ie.fujitsu.com)
 * @since 13/08/2014
 * @version 0.0.1
 * 
 */
public class HtmlCollection extends ArrayList<CrawledHtmlPage>
{

	private static final long serialVersionUID = 5144106476020583481L;
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
	 * @return total number of documents.
	 */
	public int getCount()
	{
		return count;
	}

	/**
	 * Return document at position index.
	 * 
	 * @param index Index of the document.
	 */
	public CrawledHtmlPage get(int index)
	{
		return super.get(index);
	}

}
