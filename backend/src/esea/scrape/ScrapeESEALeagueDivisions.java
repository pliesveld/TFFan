package esea.scrape;


//import java.util.regex.Pattern;
//import java.util.regex.Matcher;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import esea.EseaTeam;
import esea.EseaDivision;


public class ScrapeESEALeagueDivisions
{
	static String LEAGUE_DOC_TITLE = "Premium - League - Standings";

	static public Document fetch(String division_id) throws ScrapeException
	{
		Document result;
		String base_url = "http://play.esea.net/index.php?s=league&d=standings&division_id=";

		result = ScrapePage.open(base_url + division_id);
		return result;
	}
	
	
	static public EseaDivision parse(Document doc) throws ScrapeException {
		EseaDivision teamInfo; 

		if(!doc.title().contentEquals(LEAGUE_DOC_TITLE))
			throw new ScrapeException("attempting to parse document that is not an esea league standings page: " + doc.title());


		String league = ScrapeUtility.ESEA.validateLeagueHeader(doc);
		Elements team_table = ScrapeUtility.validateSelect(doc,"div#league-standings table tbody");            
		Elements team_row = ScrapeUtility.validateSelect(team_table,"tr.row1,tr.row2");
		Elements team_info = ScrapeUtility.validateSelect(team_row,"td a[href^=/teams/");

		int idx = league.lastIndexOf(' ');

		String game = league.substring(0,idx);
		league = league.substring(idx+1);

		teamInfo = new EseaDivision(game,league);
		for(Element team : team_info)
		{
			int team_id = ScrapeUtility.fetchAttrHrefAsInt(team);
			teamInfo.add(new EseaTeam(team.ownText(),team_id));
		}


		return teamInfo;
	}

}

