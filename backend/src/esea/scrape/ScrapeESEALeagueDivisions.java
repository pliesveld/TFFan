package esea.scrape;
import java.io.IOException;
import java.io.File;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import esea.EseaTeam;
import esea.EseaTeamInfo;


public class ScrapeESEALeagueDivisions extends ScrapePage {

	public ScrapeESEALeagueDivisions()
	{
		super();
	}

	public ScrapeESEALeagueDivisions(String file_name) throws ScrapeException
	{
		super(file_name);
	}

	public ScrapeESEALeagueDivisions(File file) throws ScrapeException
	{
		super(file);
	}
	
	public EseaTeamInfo fetch(String url) {
		if(doc == null)
			try {
				doc = this.open(url);
			} catch (IOException e) {
				e.printStackTrace();

			}
		return parse(doc);
	}
	
	public EseaTeamInfo parse(Document d) {
		EseaTeamInfo teamInfo; 
		try {
			if(d == null)
				throw new ScrapeException("Invalid document reference to parse.");

			String league = ScrapeUtility.ESEA.validateLeagueHeader(d);
			Elements team_table = ScrapeUtility.validateSelect(d,"div#league-standings table tbody");            
			Elements team_row = ScrapeUtility.validateSelect(team_table,"tr.row1,tr.row2");
			Elements team_info = ScrapeUtility.validateSelect(team_row,"td a[href^=/teams/");

			int idx = league.lastIndexOf(' ');

			String game = league.substring(0,idx);
			league = league.substring(idx+1);

			teamInfo = new EseaTeamInfo(game,league);
			for(Element team : team_info)
			{
				int team_id = ScrapeUtility.fetchAttrHrefAsInt(team);
				teamInfo.add(new EseaTeam(team.ownText(),team_id));
			}

		} catch (ScrapeException e) {
			e.printStackTrace();
			return null;
		}
		return teamInfo;
	}


}

