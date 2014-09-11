package org.ki2na.ld4ie.io.model;

import java.net.URISyntaxException;

/**
 * Representation of one input record.
 * 
 * @author Emir Munoz (Emir.Munoz@ie.fujitsu.com)
 * @since 13/08/2014
 * @version 0.0.3
 * 
 */
public class CrawledHtmlPage
{

	/** Web page URI */
	private java.net.URI URI;
	/** Content-type of the web page */
	private String contentType;
	/** HTML code of the web page */
	private String content;

	/**
	 * Class constructor.
	 * 
	 * @param URI Web page URI.
	 * @param type Content-type.
	 * @param content HTML code.
	 */
	public CrawledHtmlPage(final java.net.URI URI, final String type, final String content)
	{
		this.URI = URI;
		this.contentType = type;
		this.content = content;
	}

	/**
	 * @return URI of the web page.
	 */
	public java.net.URI getURI()
	{
		return URI;
	}

	/**
	 * @return Content-type of the web page.
	 */
	public String getContentType()
	{
		return contentType;
	}

	/**
	 * @return HTML code of the web page.
	 */
	public String getContent()
	{
		return content;
	}

	@Override
	public boolean equals(Object other)
	{
		CrawledHtmlPage oPage = (CrawledHtmlPage) other;
		if (this.URI.equals(oPage.getURI()) && this.contentType.equals(oPage.getContentType())
				&& this.content.equals(oPage.getContent()))
			return true;
		else
			return false;
	}

	@Override
	public String toString()
	{
		return "HTMLPage: {URI=" + URI + ", content-type=" + contentType + ", content=" + content.toString() + "}";
	}

	/**
	 * Crawled HTML Page Builder.
	 * 
	 * @author Emir Munoz (Emir.Munoz@ie.fujitsu.com)
	 * @since 13/08/2014
	 * 
	 */
	public static class CrawledHtmlPageBuilder
	{
		private java.net.URI nestedURI;
		private String nestedContentType;
		private String nestedContent;

		public CrawledHtmlPageBuilder(final String URIstr) throws URISyntaxException
		{
			super();
			this.nestedURI = new java.net.URI(URIstr);
		}

		public CrawledHtmlPageBuilder(final java.net.URI nestedURI, final String nestedContentType,
				final String nestedContent)
		{
			super();
			this.nestedURI = nestedURI;
			this.nestedContentType = nestedContentType;
			this.nestedContent = nestedContent;
		}

		public CrawledHtmlPageBuilder URI(final java.net.URI URI)
		{
			this.nestedURI = URI;
			return this;
		}

		public CrawledHtmlPageBuilder contentType(final String contentType)
		{
			this.nestedContentType = contentType;
			return this;
		}

		public CrawledHtmlPageBuilder content(final String content)
		{
			this.nestedContent = content;
			return this;
		}

		public CrawledHtmlPage build()
		{
			return new CrawledHtmlPage(nestedURI, nestedContentType, nestedContent);
		}

	}

}
