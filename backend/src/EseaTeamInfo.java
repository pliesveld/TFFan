
public class EseaTeamInfo {
	public String division;
	public String teamName;
	public int teamId;

	public EseaTeamInfo(String div, String teamName, int id)
	{
		this.division = div;
		this.teamName = teamName;
		this.teamId = id;
	}
	
	public String toString()
	{
		return "div: " + division + " team: " + teamName + " id: " + teamId;
	}
}