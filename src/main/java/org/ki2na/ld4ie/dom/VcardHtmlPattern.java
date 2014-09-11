package org.ki2na.ld4ie.dom;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.ki2na.ld4ie.extractor.HtmlCleaner;
import org.ki2na.ld4ie.io.HtmlInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.internal.Lists;

/**
 * vCard HTML tags extractor for analysis. This allows us to fin patterns in the DOM tree that can
 * be used to detect vCards in HTML pages without any annotations.
 * 
 * @author Emir Munoz (Emir.Munoz@ie.fujitsu.com)
 * @version 0.0.3
 * @since 03/09/2014
 * 
 */
public class VcardHtmlPattern
{

	/** class logger */
	private final static transient Logger _log = LoggerFactory.getLogger(VcardHtmlPattern.class.getName());

	/**
	 * Main method.
	 */
	public static void main(String[] args) throws IOException
	{
		HtmlCleaner cleaner = new HtmlCleaner();
		HtmlInputReader reader = new HtmlInputReader("data/train4.html.txt.gz");
		reader.read();
		_log.info(reader.getCount() + " documents found!");

		final List<String> tags = Lists.newArrayList();
		List<String> classes = Lists.newArrayList();

		// output file
		GZIPOutputStream zip = new GZIPOutputStream(new FileOutputStream(new File("./data/vcardDOM4_white.txt.gz")));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(zip, "UTF-8"));

		for (int i = 0; i < reader.getCount(); i++)
		{
			System.out.println(reader.get(i).getURI());
			writer.append("===> " + i + "\t" + reader.get(i).getURI());
			writer.newLine();

			Document doc = cleaner.clean(reader.get(i).getContent(), reader.get(i).getURI().toString());
			// visit all nodes and look for properties
			Elements vcards = doc.select("[class*=vcard]");
			// add slot attribute
			for (Element vcard : vcards)
			{
				classes.addAll(vcard.classNames());
				writer.append(vcard.toString());
				writer.newLine();

				// traverse all children of this node. Implement head (open tag) and tail (close
				// tag).
				NodeTraversor nd = new NodeTraversor(new NodeVisitor()
				{
					@Override
					public void head(Node node, int depth)
					{
						if (!node.nodeName().equals("br") && node.toString().trim().length() > 0)
							tags.add("<" + node.nodeName() + ">");
					}

					@Override
					public void tail(Node node, int depth)
					{
						if (!node.nodeName().equals("br") && node.toString().trim().length() > 0)
							tags.add("</" + node.nodeName() + ">");
					}
				});
				// actual traverse of the element
				nd.traverse(vcard);

				writer.append("TAG=" + vcard.tagName());
				writer.newLine();
				writer.append("CLASSES=" + classes);
				writer.newLine();
				writer.append("CHILDREN_TAGS=" + tags);
				writer.newLine();
				writer.append("--------------");
				writer.newLine();

				// clear lists
				tags.clear();
				classes.clear();
			}
		}
		if (writer != null)
			writer.close();

		_log.info("Patterns generated");
	}

}
