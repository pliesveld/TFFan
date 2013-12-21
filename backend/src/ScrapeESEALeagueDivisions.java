import java.io.IOException;

import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ScrapeESEALeagueDivisions {

	public static Collection<EseaTeamInfo> fetch(String url,String division_category) {
	    Document doc;
    	Collection<EseaTeamInfo> teamsArray = new ArrayList<EseaTeamInfo>(); 
	    
        try {
    //    	String default_url = "http://play.esea.net/index.php";
    //    	Map<String, String> cookies = Jsoup.connect(default_url).execute().cookies();
        		
            doc = Jsoup.connect(url)
            		//.cookies(cookies)
            		.referrer(url).get();

            Elements team_table = doc.select("div#league-standings table tbody");
            
            Elements team_row = team_table.select("tr.row1,tr.row2");
            
            Elements team_info = team_row.select("td a[href^=/teams/");
            
            for(Element team : team_info)
            {
            	String href_attr = team.attr("href").substring(7);	            	
            	EseaTeamInfo data = new EseaTeamInfo(division_category,team.text(),href_attr);
            	teamsArray.add(data);
            }

        } catch (IOException e) {
                e.printStackTrace();
                return null;
        }
        return teamsArray;
	}

	
}

