import java.lang.*;
import java.util.*;

public class EseaTeamSchedule
{
	String esea_team_id;
	Map<Integer, EseaScheduleEvent> schedule;

	EseaTeamSchedule(String team_id)
	{
		this.esea_team_id = team_id;
		this.schedule = new HashMap<Integer,EseaScheduleEvent>();
	}

	void putMatch(int match_id, EseaScheduleEvent e)
	{
		schedule.put(match_id,e);
	}


	public String toString()
	{
		StringBuilder result = new StringBuilder(512);
		result.append("schedule team_id: ");
		result.append(esea_team_id);

		for(Integer match_id : schedule.keySet())
		{
			result.append("match_id: ");
			result.append(match_id);
			result.append("\n");
			result.append(schedule.get(match_id));
			result.append("\n");
		}
	
		return result.toString();
	}

}


class EseaScheduleEvent
{
	int home;
	int away;
	String map;

	int home_score;
	int away_score;

	EseaScheduleEvent(int home, int away, String map, int home_score, int away_score)
	{
		this.home = home;
		this.away = away;
		this.map = map;

		this.home_score = home_score;
		this.away_score = away_score;
	}

	public String toString()
	{
		StringBuilder result = new StringBuilder(256);
		result.append("Home team_id: " + home);
		if(home_score > away_score)
			result.append(" Win\n");
		else
			result.append(" Loss\n");
		result.append("Away team_id: " + away);
		if(home_score < away_score)
			result.append(" Win\n");
		else
			result.append(" Loss\n");
		result.append("Map: " + map);
		result.append("\n");

		return result.toString();
	}

}
