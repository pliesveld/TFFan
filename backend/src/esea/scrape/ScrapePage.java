package esea.scrape;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public abstract class ScrapePage
{
	protected Document doc;
	private String default_url = "http://play.esea.net/index.php";

	Map<String, String> cookies;

	ScrapePage()
	{

		(cookies = new TreeMap<String,String>()).put("viewed_welcome_page", "1"); 

	}

	public Map<String,String> getCookies()
	{
		return cookies;
	}
	
	Document open(String url) throws IOException
	{
		Connection result = Jsoup.connect(url)
			.cookie("viewed_welcome_page", "1");
		
		System.err.println("Request Headers\n" + result.request().headers());
		Response response = result.execute();

		System.err.println("HTTP STATUS " + response.statusCode());
		System.err.println("Response Headers\n" + response.headers());
		return doc = response.parse();
		
		
	}
	
	ScrapePage(String filename) throws ScrapeException
	{
		this(new File(filename));
	}

	ScrapePage(File input) throws ScrapeException
	{

		System.out.println("ScrapePage(" + input.getName() + ")");
		try {
			doc = Jsoup.parse(input,"UTF-8", "file:// " + input.getName());
		} catch(IOException e) {
			throw new ScrapeException("Couldn't load document from file: " + input.getPath(), e);
		}
	}

//	abstract Document fetchPage()			 throws ScrapeException;
//	abstract void scrapePage(Document d) throws ScrapeException;
}
