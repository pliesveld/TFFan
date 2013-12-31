import java.io.*;
import java.util.*;

import esea.EseaTeamRoster;
import esea.EseaTeamSchedule;
import esea.scrape.ScrapeESEATeam;
import esea.scrape.ScrapeException;

public class ScrapeScheduleTestApp
{
	public static void main(String[] args)
	{

		Sample db_store = null;

		try {
			db_store = new Sample();
			db_store.createTables();
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
				System.out.println("File not found Skipping ... " + file.getName());
				continue;
			}

			ScrapeESEATeam team_page = new ScrapeESEATeam(file);
			String fName = file.getName();
			fName = fName.substring(0,fName.indexOf('.'));

			//		EseaTeamPage teamPage = team_page.fetch(fName);
			EseaTeamRoster roster = null;
			try {
				roster = team_page.fetch_roster(fName);
			} catch (ScrapeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			EseaTeamSchedule schedule = null;
			try {
				schedule = team_page.fetch_schedule(fName);
			} catch (esea.scrape.ScrapeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if(roster != null)
			{
				db_store.insertRoster(roster);
			}
			if(schedule != null)
				db_store.insertSchedule(schedule);
		}



	}
}

