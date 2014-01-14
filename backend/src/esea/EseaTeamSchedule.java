package esea;
import java.util.*;

public class EseaTeamSchedule
{
	int esea_team_id;
	private Map<Integer, EseaScheduleEvent> schedule;



	public class EseaScheduleEvent
	{
		private int home;
		private int away;
		private String map;

		private int home_score;
		private int away_score;

		public EseaScheduleEvent(int home, int away, String map, int home_score, int away_score)
		{
			this.setHome(home);
			this.setAway(away);
			this.setMap(map);

			this.setHome_score(home_score);
			this.setAway_score(away_score);
		}

		public String toString()
		{
			StringBuilder result = new StringBuilder(256);
			result.append("Home team_id: " + getHome());
			if(getHome_score() > getAway_score())
				result.append(" Win\n");
			else
				result.append(" Loss\n");
			result.append("Away team_id: " + getAway());
			if(getHome_score() < getAway_score())
				result.append(" Win\n");
			else
				result.append(" Loss\n");
			result.append("Map: " + getMap());
			result.append("\n");

			return result.toString();
		}

		public int getHome() {
			return home;
		}

		public void setHome(int home) {
			this.home = home;
		}

		public int getAway() {
			return away;
		}

		public void setAway(int away) {
			this.away = away;
		}

		public int getHome_score() {
			return home_score;
		}

		public void setHome_score(int home_score) {
			this.home_score = home_score;
		}

		public int getAway_score() {
			return away_score;
		}

		public void setAway_score(int away_score) {
			this.away_score = away_score;
		}

		public String getMap() {
			return map;
		}

		public void setMap(String map) {
			this.map = map;
		}

	}

	public EseaTeamSchedule(int esea_team_id2)
	{
		this.esea_team_id = esea_team_id2;
		this.setSchedule(new HashMap<Integer,EseaScheduleEvent>());
	}

	public void putMatch(int match_id, EseaScheduleEvent e)
	{
		getSchedule().put(match_id,e);
	}


	public String toString()
	{
		StringBuilder result = new StringBuilder(512);
		result.append("schedule team_id: ");
		result.append(esea_team_id);

		for(Integer match_id : getSchedule().keySet())
		{
			result.append("match_id: ");
			result.append(match_id);
			result.append("\n");
			result.append(getSchedule().get(match_id));
			result.append("\n");
		}

		return result.toString();
	}

	public Map<Integer, EseaScheduleEvent> getSchedule() {
		return schedule;
	}

	public void setSchedule(Map<Integer, EseaScheduleEvent> schedule) {
		this.schedule = schedule;
	}

}


