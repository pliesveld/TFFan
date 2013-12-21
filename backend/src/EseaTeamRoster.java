import java.util.ArrayList;


public class EseaTeamRoster {
	private String league;
	private String teamName;
	private String teamId;
	private ArrayList<EseaTeamRosterPlayerEntry> teamRoster;
	

	public EseaTeamRoster(String league, String teamName, String id)
	{
		this.league = league;
		this.teamName = teamName;
		this.teamId = id;
		this.teamRoster = new ArrayList<EseaTeamRosterPlayerEntry>();
	}
	
	public void addPlayer(String name, String id)
	{
		EseaTeamRosterPlayerEntry e = new EseaTeamRosterPlayerEntry(name,id);
		teamRoster.add(e);
	}
	
	public String toString()
	{
		StringBuffer result = new StringBuffer(4*32 + 12*this.teamRoster.size());
		
		result.append("League: ");
		result.append(this.league);
		result.append("\nTeam: ");
		result.append(this.teamName);
		result.append("(" + this.teamId + ")\n");
		
		for(EseaTeamRosterPlayerEntry e : this.teamRoster)
		{
			result.append("player: " + e.getName() + " " + e.getId() + "\n");
		}
		
		return result.toString();
	}
}

class EseaTeamRosterPlayerEntry
{
	private String name;
	private String id;
	
	public EseaTeamRosterPlayerEntry(String name, String id)
	{
		this.name = name;
		this.id = id;
	}
	public String getName() { return this.name; }
	public String getId()   { return this.id; }
}