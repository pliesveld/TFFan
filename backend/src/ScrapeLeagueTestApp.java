import java.io.*;
import java.util.*;

public class ScrapeLeagueTestApp
{
	public static void main(String[] args)
	{

		Sample db_store = null;

		try {
			db_store = new Sample();
		} catch(ClassNotFoundException e) {
			System.err.println("Couldn't find database driver" + e.getMessage());
			System.exit(2);
		}

		int i = 0;
		for(String arg : args)
		{
			File file = new File(arg);
			if(!file.exists())
			{
				System.out.println("Skipping ... " + file.getName());
				continue;
			}

			ScrapeESEALeagueDivisions page = new ScrapeESEALeagueDivisions(file);
			String fName = file.getName();
			fName = fName.substring(0,fName.indexOf('.'));

			EseaTeamInfo teams = page.fetch(fName);
			db_store.insertTeam(teams);

			for(EseaTeam team : teams.teamsArray)
				System.out.printf("curl -o %d.html \"http://play.esea.net/teams/%d\"\n",team.teamId, team.teamId);
		}
	}
}
