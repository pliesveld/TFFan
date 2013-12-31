package esea.scrape;
import java.io.*;
import java.util.Map;
import java.lang.Integer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import esea.EseaTeamRoster;
import esea.EseaTeamSchedule;
import esea.EseaTeamSchedule.EseaScheduleEvent;


public class ScrapeESEATeam extends ScrapePage {

	public ScrapeESEATeam(File file)
	{
		super(file);
	}

	public EseaTeamPage fetch(String esea_team_id)
	{
		EseaTeamPage result = new EseaTeamPage();
		String url = "http://play.esea.net/teams/" + esea_team_id;

		try {
			try {
				if( doc == null )
					doc = Jsoup.connect(url).cookies(cookies).referrer(url).get();
			} catch(IOException e) {
				e.printStackTrace();
				result = null;
			}
			result.schedule = fetch_schedule(esea_team_id);
			result.roster = fetch_roster(esea_team_id);			
		} catch(ScrapeException e)
		{
			System.err.println("Failed to scrape team #" +esea_team_id + " reason: " + e.getMessage());
			result = null;
		}

		return result;
	}

	public EseaTeamSchedule fetch_schedule(String esea_team_id) throws ScrapeException 
	{
		EseaTeamSchedule result = null;

		Elements team_table = ScrapeUtility.validateSelect(doc,"div#profile-content div.tabContent table tbody tr.row1:gt(1)");

		for( Element team_match : team_table )
		{
			if(team_match.select("td + td").first().ownText().contains("-"))
				continue;

			Element team_home = ScrapeUtility.validateSelect(team_match,"td + td > a").first();
			Element team_away = ScrapeUtility.validateSelect(team_match,"td + td + td > a").first();

			Element match_map = ScrapeUtility.validateSelect(team_match,"td + td + td + td").first();

			Element match_result = ScrapeUtility.validateSelect(team_match,"td + td + td + td + td > a").first();

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
	public EseaTeamRoster fetch_roster(String esea_team) throws ScrapeException 
	{
		boolean isDead = false;

		int esea_team_id = ScrapeUtility.parseInt(esea_team);

		Element team_name, league_name;
		Elements roster_table, team_row, player_roster_table; 


		Elements profile_info = ScrapeUtility.validateSelect(doc,"div#profile-info");

		Elements dead_check = profile_info.select("div > span");

		team_name = ScrapeUtility.validateSingleSelect(doc, "#profile-header > h1");
		league_name = ScrapeUtility.validateSingleSelect(doc,"div#profile-info div.content div.data ~ label.margin-top:contains(League:) + div a");



		EseaTeamRoster team_roster = new EseaTeamRoster(league_name.text(),team_name.text(),esea_team_id,isDead);


		if(dead_check.size() > 0 && dead_check.first().ownText().matches("Dead"))
		{
			isDead = true;
		} else {

			roster_table = ScrapeUtility.validateSelect(doc,"div#profile-column-right");
			team_row = ScrapeUtility.validateSelect(roster_table,"div.row1,div.row2");

			player_roster_table = ScrapeUtility.validateSelect(team_row,"span.right:contains(Paid):not(:contains(Not Paid)) + img + a + a");

			for(Element player : player_roster_table)
			{
				int player_id = ScrapeUtility.fetchAttrHrefAsInt(player);
				team_roster.addPlayer( player.text(), player_id);

			}

		}

		return team_roster;

	}

}
