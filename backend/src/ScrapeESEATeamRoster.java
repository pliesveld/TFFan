import java.io.IOException;
import java.util.Map;
import java.lang.Integer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ScrapeESEATeamRoster {

	public static EseaTeamRoster fetch(String esea_team_id) {
	    Document doc;
	    
	    String url = "http://play.esea.net/teams/" + esea_team_id;
	    	
        try {
        	String default_url = "http://play.esea.net/index.php";
        	Map<String, String> cookies = Jsoup.connect(default_url).execute().cookies();

            doc = Jsoup.connect(url).cookies(cookies).referrer(url).get();
          
            Element team_name, league_name;
            Elements roster_table, team_row, filter_players; 
            
            if ((team_name = doc.select("#profile-header > h1").first()) == null)
            	throw new ScrapeException("Failed to scrape #profile-header in " + url);
            
            if ((roster_table = doc.select("div#profile-column-right")) == null)
            	throw new ScrapeException("Failed to scrape div#profile-column-right in " + url);
            
            if((team_row = roster_table.select("div.row1,div.row2")) == null)
            	throw new ScrapeException("Failed to parse team row in " + url);
            
            if((filter_players
            	= team_row.select(
            		"span.right:contains(Paid):not(:contains(Not Paid)) + img + a + a")) == null)
            	throw new ScrapeException("Failed to parse team roster table in " + url);
            	
            if((league_name 
            	= doc.select(
            		"div#profile-info div.content div.data ~ label.margin-top:contains(League:) + div a").first()) == null)
            		throw new ScrapeException("Failed to parse league name in " + url);

            EseaTeamRoster team_roster = new EseaTeamRoster(league_name.text(),team_name.text(),esea_team_id);
            
            for(Element player : filter_players)
            {
            	int player_id = ScrapeUtility.fetchAttrName(player);
            	team_roster.addPlayer(player.text(), player_id);
            }
            
            return team_roster;
            
	    } catch (IOException|ScrapeException e) {
	            e.printStackTrace();
	    }
        return null;
	}

	
}

