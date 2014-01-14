package esea.scrape;


import java.lang.Integer;
import java.sql.Date;
import java.text.ParseException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import esea.EseaTeamRoster;
import esea.EseaTeamSchedule;
import esea.EseaTeamSchedule.EseaScheduleEvent;


public class ScrapeESEATeam {

	private Pattern team_title_pattern = Pattern.compile("^Premium - Teams - (.+)$");

	public Document fetch(String esea_team_id) throws ScrapeException
	{
		Document result;
		String base_url = "http://play.esea.net/teams/";

		try {
			Integer.parseInt(esea_team_id);
		} catch(NumberFormatException e) {
			throw new ScrapeException("Invalid team id: " + esea_team_id);
		}	

		result = ScrapePage.open(base_url + esea_team_id);

		Matcher m = team_title_pattern.matcher(result.title());
		if(!m.matches())
			throw new ScrapeException("failed to fetch esea team page: " + result.title());

		return result;
	}

	public EseaTeamSchedule parse_schedule(Document doc) throws ScrapeException 
	{
		EseaTeamSchedule result = null;

		Matcher m = team_title_pattern.matcher(doc.title());
		if(!m.matches())
			throw new ScrapeException("attempting to parse an esea team schedule: " + doc.title());


		Element team_profile = ScrapeUtility.validateSelect(doc,"a#tab-default[href^=/teams/]").first();
		if(team_profile == null)
			throw new ScrapeException("invalid esea team page: " + doc.title());

		int esea_team_id = ScrapeUtility.fetchAttrHrefAsInt(team_profile);

		Elements team_table = ScrapeUtility.validateSelect(doc,"div#profile-content div.tabContent table tbody tr.row1:gt(1)");

		for( Element team_match : team_table )
		{
			if(team_match.select("td + td").first().ownText().contains("-"))
				continue;

			
			
			Element team_home = ScrapeUtility.validateSelect(team_match,"td + td > a").first();
			Element team_away = ScrapeUtility.validateSelect(team_match,"td + td + td > a").first();

			Element match_map = ScrapeUtility.validateSelect(team_match,"td + td + td + td").first();

			String match_time = ScrapeUtility.validateSelect(team_match,"td:last-child").val();


			Element match_result = team_match.select("td + td + td + td > a").first();

			if(match_result == null)
				continue;
			
			String str = match_result.ownText();
			if(!str.startsWith("Win") && !str.startsWith("Loss"))
			{
				if(str.startsWith("Upcoming"))
				{
					// schedule delayed-check based on date from element #td + td + td + td + td + td > a
					continue;
				}
				throw new ScrapeException("Field expected to be Win/Loss/Upcoming: " + str);
			}

			Element match_score = ScrapeUtility.validateSelect(team_match,"td + td + td + td + td + td > a").first();
			int match_id = -1;

			if(match_score.hasAttr("href"))
			{
				int id_off = match_score.attr("href").indexOf("id=");
				if(id_off == -1)
				{
					System.err.println(esea_team_id);
					System.exit(4);
					throw new ScrapeException("a href expecting match id=" + match_score);
				}
				id_off += 3;
				match_id = ScrapeUtility.fetchAttrHrefAsInt(match_score,id_off);
			}

			int team_home_id = ScrapeUtility.fetchAttrHrefAsInt(team_home);
			int team_away_id = ScrapeUtility.fetchAttrHrefAsInt(team_away);
			int team_home_score = -1;
			int team_away_score = -1;

			String matchScore = match_score.ownText();
			int sep_idx = matchScore.indexOf('-');

			if(sep_idx == -1)
				throw new ScrapeException("Invalid score " + matchScore);

			try {
				team_home_score = Integer.parseInt(matchScore.substring(0,sep_idx));
				team_away_score = Integer.parseInt(matchScore.substring(sep_idx+1));
			} catch(IndexOutOfBoundsException|NumberFormatException e) {
				throw new ScrapeException("Invalid Score: " + matchScore,e);
			}


			if(result == null)
				result = new EseaTeamSchedule(esea_team_id);

			EseaScheduleEvent match_event = result.new EseaScheduleEvent(team_home_id, team_away_id, match_map.ownText(), team_home_score, team_away_score);
			result.putMatch(match_id, match_event);
			
		}		 
		return result;

	}
	public EseaTeamRoster parse_roster(Document doc) throws ScrapeException 
	{
		boolean isDead = false;


		Matcher m = team_title_pattern.matcher(doc.title());
		if(!m.matches())
			throw new ScrapeException("attempting to parse an esea team schedule: " + doc.title());


		Element team_profile = ScrapeUtility.validateSelect(doc,"a#tab-default[href^=/teams/]").first();
		if(team_profile == null)
			throw new ScrapeException("invalid esea team page: " + doc.title());

		int esea_team_id = ScrapeUtility.fetchAttrHrefAsInt(team_profile);

		Element team_name, league_name;
		Elements roster_table, team_row, player_roster_table = null; 


		Elements profile_info = ScrapeUtility.validateSelect(doc,"div#profile-info");

		Elements dead_check = profile_info.select("div > span");

		team_name = ScrapeUtility.validateSingleSelect(doc, "#profile-header > h1");
		league_name = ScrapeUtility.validateSingleSelect(doc,"div#profile-info div.content div.data ~ label.margin-top:contains(League:) + div a");


		if(dead_check.size() > 0 && dead_check.first().ownText().matches("Dead"))
		{
			isDead = true;
		} else {
			roster_table = ScrapeUtility.validateSelect(doc,"div#profile-column-right");
			team_row = ScrapeUtility.validateSelect(roster_table,"div.row1,div.row2");

			player_roster_table = team_row.select("span.right:contains(Paid):not(:contains(Not Paid)) + img + a + a");

			if(roster_table == null || roster_table.size() == 0)
			{
				isDead = true;
			}
			
		}
		
		
	
		EseaTeamRoster team_roster = new EseaTeamRoster(league_name.text(),team_name.text(),esea_team_id,isDead);



		if(!isDead && player_roster_table != null && player_roster_table.size() > 0)
		{
				for(Element player : player_roster_table)
				{
					int player_id = ScrapeUtility.fetchAttrHrefAsInt(player);
					team_roster.addPlayer( player.text(), player_id);
				}

		
		}

		return team_roster;

	}

}
