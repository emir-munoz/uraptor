package org.ki2na.ld4ie.extractor.model;

import java.util.List;

import org.ki2na.ld4ie.dom.TagAnnotation;

/**
 * CSS path rule representation.
 * 
 * @author Emir Munoz (Emir.Munoz@ie.fujitsu.com)
 * @version 0.0.5
 * @since 04/09/2014
 * 
 */
public class CssRule
{

	/** CSS path */
	private String path;
	/** path depth */
	private int depth;
	/** corresponding vCard annotations */
	private List<TagAnnotation> annotations;

	/**
	 * Class constructor.
	 * 
	 * @param path CSS path.
	 * @param depth Max. depth of the path.
	 */
	public CssRule(String path, int depth)
	{
		this.path = path;
		this.depth = depth;
	}

	/**
	 * Class constructor.
	 * 
	 * @param path CSS path.
	 * @param depth Max. depth of the path.
	 * @param annotations Tag annotations.
	 */
	public CssRule(String path, int depth, List<TagAnnotation> annotations)
	{
		this.path = path;
		this.depth = depth;
		this.annotations = annotations;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public int getDepth()
	{
		return depth;
	}

	public void setDepth(int depth)
	{
		this.depth = depth;
	}

	public List<TagAnnotation> getAnnotations()
	{
		return annotations;
	}

	public void setAnnotations(List<TagAnnotation> annotations)
	{
		this.annotations = annotations;
	}

	public void addAnnotation(TagAnnotation annotation)
	{
		this.annotations.add(annotation);
	}

}
