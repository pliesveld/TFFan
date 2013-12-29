import java.io.*;
import java.util.Map;
import java.lang.Integer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ScrapeESEATeamRoster extends ScrapePage {

	public ScrapeESEATeamRoster(File file)
	{
		super(file);
	}

	public EseaTeamSchedule fetch(String esea_team_id) 
	{
		EseaTeamSchedule result = null;
	   String url = "http://play.esea.net/teams/" + esea_team_id;

		try {
			if( doc == null )
				doc = Jsoup.connect(url).cookies(cookies).referrer(url).get();

			Elements team_table = ScrapeUtility.validateSelect(doc,"div#profile-content div.tabContent table tbody tr.row1:gt(1)");

			for( Element team_match : team_table )
			{
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


				System.out.println("match_id " + match_id);
				System.out.println("team_home: " + team_home_id + " points: " + team_home_score);
				System.out.println("team_away: " + team_away_id + " points: " + team_away_score);
				System.out.println("match_map: " + match_map.ownText());

				if(result == null)
					result = new EseaTeamSchedule(esea_team_id);

				EseaScheduleEvent match_event = new EseaScheduleEvent(team_home_id, team_away_id, match_map.ownText(), team_home_score, team_away_score);

				result.putMatch(match_id, match_event);
			}

		} catch(ScrapeException|IOException e) {
			e.printStackTrace();
		}
		finally {
			return result;
		}

	}

	public EseaTeamRoster parseRoster(String esea_team_id) {
	    String url = "http://play.esea.net/teams/" + esea_team_id;
		 boolean isDead = false;
	    	
        try {

				if( doc == null )
					doc = Jsoup.connect(url).cookies(cookies).referrer(url).get();
          
            Element team_name, league_name;
            Elements roster_table, team_row, filter_players; 

				Elements profile_info = ScrapeUtility.validateSelect(doc,"div#profile-info div.content div.data.margin-top span");

				if(profile_info != null && profile_info.first() != null)
				{
					if(profile_info.first().ownText().matches("Dead"))
						isDead = true;
				}

            
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

            EseaTeamRoster team_roster = new EseaTeamRoster(league_name.text(),team_name.text(),esea_team_id,isDead);
            
            for(Element player : filter_players)
            {
            	int player_id = ScrapeUtility.fetchAttrHrefAsInt(player);
            	team_roster.addPlayer( player.text(), player_id);
            }
            
            return team_roster;
            
	    } catch (IOException|ScrapeException e) {
	            e.printStackTrace();
	    }
        return null;
	}

	
}

