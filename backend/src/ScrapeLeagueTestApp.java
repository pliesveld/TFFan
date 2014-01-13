import java.io.*;
import java.sql.SQLException;
import java.util.*;

import esea.EseaTeamInfo;
import esea.scrape.ScrapeESEALeagueDivisions;
import esea.scrape.ScrapeException;
import esea.store.*;

public class ScrapeLeagueTestApp
{
	public static void main(String[] args)
	{

		Storage db_store = null;

		try {
			db_store = new MySQL();
			db_store.createTables(true);
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

			try {
				ScrapeESEALeagueDivisions page = new ScrapeESEALeagueDivisions(file);
				String fName = file.getName();
				fName = fName.substring(0,fName.indexOf('.'));

				EseaTeamInfo teams = page.fetch(fName);
				if(teams != null)
					db_store.insertTeam(teams);
			} catch(ScrapeException e) {
				System.err.println(e.getMessage());
			}

		}
	}
}
