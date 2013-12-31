package esea.scrape;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
		try {
			cookies = Jsoup.connect(default_url).execute().cookies();
		//	System.out.println("ScrapePage()" + cookies.toString());
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public Map<String,String> getCookies()
	{
		return cookies;
	}
	
	Document open(String url) throws IOException
	{
		//http://play.esea.net/users/529573?tab=stats&last_type_scope=league&game_id=43&type_scope=league&period[type]=seasons
 
		return Jsoup.connect(url)
			.cookies(cookies)
			//.cookie("viewed_welcome_page", "1")
			.get();

	}
	
	ScrapePage(String filename)
	{
		this(new File(filename));
	}

	ScrapePage(File input)
	{

		System.out.println("ScrapePage(" + input.getName() + ")");
		try {
			doc = Jsoup.parse(input,"UTF-8", "file:// " + input.getName());
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

//	abstract Document fetchPage()			 throws ScrapeException;
//	abstract void scrapePage(Document d) throws ScrapeException;
}
