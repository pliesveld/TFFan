import java.util.ArrayList;


public class EseaTeamRoster {
	private String league;
	private String teamName;
	private String teamId;
	private boolean isDead;
	private ArrayList<EseaTeamRosterPlayerEntry> teamRoster;
	

	public EseaTeamRoster(String league, String teamName, String id)
	{
		this(league,teamName,id,false);
	}

	public EseaTeamRoster(String league, String teamName, String id, boolean isDead)
	{
		this.league = league;
		this.teamName = teamName;
		this.teamId = id;
		this.isDead = isDead;
		this.teamRoster = new ArrayList<EseaTeamRosterPlayerEntry>();
	}
	
	public void addPlayer(String name, int player_id)
	{
		EseaTeamRosterPlayerEntry e = new EseaTeamRosterPlayerEntry(name,player_id);
		teamRoster.add(e);
	}
	
	public String toString()
	{
		StringBuffer result = new StringBuffer(4*32 + 12*this.teamRoster.size());
		
		result.append("League: ");
		result.append(this.league);
		result.append("\nTeam: ");
		result.append(this.teamName);
		result.append("(" + this.teamId + ")");
		if(isDead)
			result.append("--Dead\n");
		else
			result.append("\n");
		
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
	private int id;
	
	public EseaTeamRosterPlayerEntry(String name, int player_id)
	{
		this.name = name;
		this.id = player_id;
	}
	public String getName() { return this.name; }
	public int getId()   { return this.id; }
}
