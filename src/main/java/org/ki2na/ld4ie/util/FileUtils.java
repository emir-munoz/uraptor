package org.ki2na.ld4ie.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * File Utils.
 * 
 * @author Emir Munoz (Emir.Munoz@ie.fujitsu.com)
 * @version 0.0.1
 * 
 */
public class FileUtils
{

	/**
	 * Read a file from path and convert it into string.
	 * 
	 * @param path path to the file.
	 * @param encoding encoding to use.
	 * @return A string version of the input file.
	 * @throws IOException
	 */
	public static String readFile(String path, Charset encoding) throws IOException
	{
		return new Scanner(new File(path), "UTF-8").useDelimiter("\\A").next();
	}

}
