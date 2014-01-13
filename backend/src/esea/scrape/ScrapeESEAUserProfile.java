package esea.scrape;
import java.io.IOException;
import java.sql.SQLException;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class ScrapeESEAUserProfile extends ScrapePage
{
	ScrapeESEAUserProfile () throws ScrapeException
	{
		super();
	}
	
	public String fetchClass(String esea_player_id) throws ScrapeException
	{
		//http://play.esea.net/users/529573?tab=stats&last_type_scope=league&game_id=43&type_scope=league&period[type]=seasons

		String url_base = "http://play.esea.net/users/";
		String url_get_args = "?tab=stats&game_id=43&type_scope=league&period[type]=seasons&period[season_type]=regular+season";
		String url = url_base + esea_player_id + url_get_args;
		
		try {
			return open(url).select("#upanel-profile > div:eq(6) > table > tbody > tr + tr > td").first().ownText();			
		}catch(IOException e) {
			throw new ScrapeException("Couldn't load user profile: " + esea_player_id, e);
		}
	}

	
}