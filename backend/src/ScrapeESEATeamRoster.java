import java.io.IOException;
import java.util.Map;

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
          
            Element team_name = doc.select("#profile-header > h1").first();
            
            Elements roster_table = doc.select("div#profile-column-right");
            
            Elements team_row = roster_table.select("div.row1,div.row2");
            
            Elements filter_players = team_row.select("span.right:contains(Paid):not(:contains(Not Paid)) + img + a + a");
            
            Element league_name = doc.select("div#profile-info div.content div.data ~ label.margin-top:contains(League:) + div a").first();
            
            EseaTeamRoster team_roster = new EseaTeamRoster(league_name.text(),team_name.text(),esea_team_id);
            
            for(Element player : filter_players)
            {
            	String href_attr = player.attr("href").substring(7);
            	team_roster.addPlayer(player.text(), href_attr);
            }
            
            return team_roster;
            
	    } catch (IOException e) {
	            e.printStackTrace();
	    }
        return null;

	}

}

