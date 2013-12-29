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

            Elements team_table = ScrapeUtility.validateSelect(doc,"div#league-standings table tbody");            
            Elements team_row = ScrapeUtility.validateSelect(team_table,"tr.row1,tr.row2");
            Elements team_info = ScrapeUtility.validateSelect(team_row,"td a[href^=/teams/");
            
            for(Element team : team_info)
            {
            	int team_id = ScrapeUtility.fetchAttrName(team);		            	
            	teamsArray.add(new EseaTeamInfo(division_category,team.text(),team_id));
            }

        } catch (IOException|ScrapeException e) {
                e.printStackTrace();
                return null;
        }
        return teamsArray;
	}

	
}

