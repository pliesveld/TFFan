package esea;
import java.util.*;

public class EseaTeamInfo 
{
	private String game;
	private String league;
	private Collection<EseaTeam> teamsArray;

	public EseaTeamInfo(String game, String league)
	{
		this.setGame(game);
		this.setLeague(league);
		setTeamsArray(new ArrayList<EseaTeam>()); 
	}

	public String getLeague() {
		return league;
	}

	public void setLeague(String league) {
		this.league = league;
	}

	public void add(EseaTeam t)
	{
		getTeamsArray().add(t);
	}

	public String toString()
	{
		StringBuilder result = new StringBuilder(1024);
		result.append("game: ");
		result.append(getGame());
		result.append("league: ");
		result.append(getLeague());
		result.append(getTeamsArray().toString());
		return result.toString();
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public Collection<EseaTeam> getTeamsArray() {
		return teamsArray;
	}

	public void setTeamsArray(Collection<EseaTeam> teamsArray) {
		this.teamsArray = teamsArray;
	}
}

