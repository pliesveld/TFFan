package esea;
import java.util.*;

public class EseaMatch
{
	private String match_id;
	public String getMatch_id() {
		return match_id;
	}

	public String getMatch_header() {
		return match_header;
	}

	public int getMatch_team_home_id() {
		return match_team_home_id;
	}

	public int getMatch_team_away_id() {
		return match_team_away_id;
	}

	public String getMatch_team_home() {
		return match_team_home;
	}

	public String getMatch_team_away() {
		return match_team_away;
	}

	public MatchStatus getStatus() {
		return status;
	}

	public EseaMatchPlayerStats getMatch_stats() {
		return match_stats;
	}

	public Map<String, String> getMatch_awards() {
		return match_awards;
	}

	public void setMatch_id(String match_id) {
		this.match_id = match_id;
	}

	public void setMatch_header(String match_header) {
		this.match_header = match_header;
	}

	public void setMatch_team_home_id(int match_team_home_id) {
		this.match_team_home_id = match_team_home_id;
	}

	public void setMatch_team_away_id(int match_team_away_id) {
		this.match_team_away_id = match_team_away_id;
	}

	public void setMatch_team_home(String match_team_home) {
		this.match_team_home = match_team_home;
	}

	public void setMatch_team_away(String match_team_away) {
		this.match_team_away = match_team_away;
	}

	public void setStatus(MatchStatus status) {
		this.status = status;
	}

	public void setMatch_stats(EseaMatchPlayerStats match_stats) {
		this.match_stats = match_stats;
	}

	public void setMatch_awards(Map<String, String> match_awards) {
		this.match_awards = match_awards;
	}

	String match_header;
	int match_team_home_id;
	int match_team_away_id;
	String match_team_home;
	String match_team_away;
	MatchStatus status;
	
	EseaMatchPlayerStats match_stats;
	
	Map<String,String> match_awards;
	
	public String match_id() {
		return match_id;
	}

	public void match_id(String match_id) {
		this.match_id = match_id;
	}

	public void addStats(EseaMatchPlayerStats m_stats)
	{
		if(match_stats == null)
			match_stats = m_stats;
		else
		{
			match_stats.getPlayer_stats().putAll(m_stats.getPlayer_stats());
		}
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer(512);
		sb.append("match #" + match_id())
		.append("\nhome: " + match_team_home + "(" + match_team_home_id +")")
		.append("\naway: " + match_team_away + "(" + match_team_away_id + ")")
		.append("\noutcome:" + match_header + "\n");

		if(match_awards != null)
			for(Map.Entry<String, String> award : match_awards.entrySet())
			{
				sb.append(award.getKey() + ": " + award.getValue() + "\n");
			}
	
		sb.append(match_stats);
		return sb.toString();
	}
	
}
