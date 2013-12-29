import java.util.*;

public class EseaTeamInfo 
{
	String game;
	String league;
	Collection<EseaTeam> teamsArray;

	EseaTeamInfo(String game, String league)
	{
		this.game = game;
		this.league = league;
		teamsArray  = new ArrayList<EseaTeam>(); 
	}

	public void add(EseaTeam t)
	{
		teamsArray.add(t);
	}

	public String toString()
	{
		StringBuilder result = new StringBuilder(1024);
		result.append("game: ");
		result.append(game);
		result.append("league: ");
		result.append(league);
		result.append(teamsArray.toString());
		return result.toString();
	}
}

