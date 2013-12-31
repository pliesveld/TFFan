package esea.scrape;
import java.io.IOException;
import java.sql.SQLException;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class ScrapeESEAUserProfile extends ScrapePage {
	
	
	public static void main(String[] args)
	{
		ScrapeProfile page = new ScrapeProfile();
		try {
			String Class = page.fetchClass(args[0]);
			System.out.printf("id %-6s: %s\n", args[0], Class);
		} catch(ScrapeException e) {
			System.err.println("Scrape faield: " + e.getMessage());
			e.printStackTrace();
		}
	}
}

class ScrapeProfile extends ScrapePage
{
	ScrapeProfile()
	{
		super();
	}
	
	public String fetchClass(String esea_player_id) throws ScrapeException
	{
		String url_base = "http://play.esea.net/users/";
		String url_get_args = "?tab=stats&last_type_scope=league&game_id=43&type_scope=league&period[type]=seasons";
		String url = url_base + esea_player_id + url_get_args;
		
		try {
			return open(url).select("#upanel-profile > div:eq(6) > table > tbody > tr + tr > td").first().ownText();			
		}catch(IOException e) {
			throw new ScrapeException("Couldn't load user profile: ." + esea_player_id, e);
		}
	}

	
}