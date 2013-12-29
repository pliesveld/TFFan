import java.util.*;

class EseaMatch
{
	String match_id;
	String match_header;
	int match_team_home_id;
	int match_team_away_id;
	String match_team_home;
	String match_team_away;
	MatchStatus status;
	
	EseaMatchStats match_team_home_stats;
	EseaMatchStats match_team_away_stats;
	
	Map<String,String> match_awards;
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer(512);
		sb.append("match #" + match_id)
		.append("\nhome: " + match_team_home + "(" + match_team_home_id +")")
		.append("\naway: " + match_team_away + "(" + match_team_away_id + ")")
		.append("\noutcome:" + match_header + "\n");

		if(match_awards != null)
			for(Map.Entry<String, String> award : match_awards.entrySet())
			{
				sb.append(award.getKey() + ": " + award.getValue() + "\n");
			}
	
		sb.append(match_team_home_stats);
		sb.append(match_team_away_stats);
		return sb.toString();
	}
	
}
