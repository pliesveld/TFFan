import java.io.IOException;
import java.io.File;

import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ScrapeESEALeagueDivisions extends ScrapePage {

	public ScrapeESEALeagueDivisions()
	{
		super();
	}

	public ScrapeESEALeagueDivisions(String file_name)
	{
		super(file_name);
	}

	public ScrapeESEALeagueDivisions(File file)
	{
		super(file);
	}

	public EseaTeamInfo fetch(String url) {
		EseaTeamInfo teamInfo; 
        try {
				if(doc == null)
				{
					doc = Jsoup.connect(url)
            		.cookies(cookies)
            		.referrer(url).get();
				}

				String league = ScrapeUtility.ESEA.validateLeagueHeader(doc);
				Elements team_table = ScrapeUtility.validateSelect(doc,"div#league-standings table tbody");            
				Elements team_row = ScrapeUtility.validateSelect(team_table,"tr.row1,tr.row2");
				Elements team_info = ScrapeUtility.validateSelect(team_row,"td a[href^=/teams/");
            
				int idx = league.lastIndexOf(' ');

				String game = league.substring(0,idx);
				league = league.substring(idx+1);
				
				teamInfo = new EseaTeamInfo(game,league);
            for(Element team : team_info)
            {
            	int team_id = ScrapeUtility.fetchAttrHrefAsInt(team);
            	teamInfo.add( new EseaTeam(team.ownText(),team_id));
            }

        } catch (IOException|ScrapeException e) {
                e.printStackTrace();
                return null;
        }
        return teamInfo;
	}

	
}

