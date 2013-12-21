
public class EseaTeamInfo {
	public String division;
	public String teamName;
	public String teamId;

	public EseaTeamInfo(String div, String teamName, String id)
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