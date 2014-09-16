package org.ki2na.ld4ie.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

/**
 * Helper class for file utils.
 * 
 * @author Emir Munoz (Emir.Munoz@ie.fujitsu.com)
 * @version 0.0.2
 * 
 */
public class FileUtils
{

	/**
	 * Utility class should not be instantiatable.
	 */
	private FileUtils()
	{
	}

	/**
	 * Create a directory if doesn't exists.
	 * 
	 * @param dirName Directory name.
	 * @param log Logger.
	 */
	public static void createDirectory(String dirName, Logger log)
	{
		File dataDir = new File(dirName);
		if (!dataDir.exists())
		{
			log.info("Creating directory: " + dirName);
			dataDir.mkdir();
		}
	}

	/**
	 * Read a file from path and convert it into string. (Method #1)
	 * 
	 * @param path path to the file.
	 * @param encoding encoding to use.
	 * @return A string version of the input file.
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static String readFile(String path, Charset encoding) throws IOException
	{
		return new Scanner(new File(path), "UTF-8").useDelimiter("\\A").next();
	}

	/**
	 * Read a file from path and convert it into string. (Method #2)
	 * 
	 * @param path path to the file.
	 * @param encoding encoding to use.
	 * @return A string version of the input file.
	 * @throws IOException
	 */
	public static String readFile2(String path, Charset encoding) throws IOException
	{
		FileInputStream is = new FileInputStream(path);
		String content = IOUtils.toString(is);
		is.close();

		return content;
	}

}
