import java.io.*;
import java.util.*;

import org.jsoup.nodes.Document;

import esea.EseaTeamRoster;
import esea.EseaTeamSchedule;
import esea.scrape.ScrapeESEATeam;
import esea.scrape.ScrapeException;
import esea.scrape.ScrapePage;
import esea.store.*;

public class ScrapeScheduleTestApp
{
	public static void main(String[] args)
	{

		Storage db_store = null;

		try {
			db_store = new MySQL();
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

			try {
				ScrapeESEATeam team_page = new ScrapeESEATeam();
				String fName = file.getName();
				fName = fName.substring(0,fName.indexOf('.'));

				Document team_doc = ScrapePage.open_file(file);
				
				EseaTeamRoster roster = null;
				try {
					roster = team_page.parse_roster(team_doc);
				} catch (ScrapeException e) {
					e.printStackTrace();
					throw e;
				}
				EseaTeamSchedule schedule = null;
				try {
					schedule = team_page.parse_schedule(team_doc);
				} catch (esea.scrape.ScrapeException e) {
					e.printStackTrace();
					throw e;
				}

				if(roster != null)
					db_store.insertRoster(roster);
				if(schedule != null)
					db_store.insertSchedule(schedule);
			} catch(ScrapeException e) {
				System.err.println(e.getMessage());
			}
		}



	}
}

