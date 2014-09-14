package org.ki2na.ld4ie.dom;

/**
 * Tag annotation for vCard rules. For example: the second <img> tag should be annotated with
 * property photo.
 * 
 * @author Emir Munoz (Emir.Munoz@ie.fujitsu.com)
 * @version 0.0.3
 * @since 09/09/2014
 * 
 */
public class TagAnnotation
{

	/** CSS selector for the tag */
	private String selector;
	/** position of the tag in the array */
	private int position;
	/** property to assign to the tag */
	private String property;

	/**
	 * Class constructor.
	 * 
	 * @param selector HTML selector for the tag.
	 * @param position Position of the tag.
	 * @param property HCard property.
	 */
	public TagAnnotation(String selector, int position, String property)
	{
		this.selector = selector;
		this.position = position;
		this.property = property;
	}

	public String getSelector()
	{
		return selector;
	}

	public void setSelector(String selector)
	{
		this.selector = selector;
	}

	public int getPosition()
	{
		return position;
	}

	public void setPosition(int position)
	{
		this.position = position;
	}

	public String getProperty()
	{
		return property;
	}

	public void setProperty(String property)
	{
		this.property = property;
	}

	@Override
	public String toString()
	{
		return "TagAnn:{selector=" + this.selector + ",position=" + this.position + ",property=" + this.property + "}";
	}

}
