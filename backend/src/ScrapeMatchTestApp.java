import java.io.*;
import java.util.*;

import org.jsoup.nodes.Document;

import esea.EseaMatch;
import esea.scrape.ScrapeESEAMatch;
import esea.scrape.ScrapeException;
import esea.scrape.ScrapePage;
import esea.store.*;

public class ScrapeMatchTestApp {

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
				System.out.println("Skipping ... " + file.getName());
				continue;
			}

			try {
				Document doc = ScrapePage.open_file(file);

                if (doc == null)
                {
                    System.err.println("Invalid file to parse: " + file);
                } else {
                    System.out.println("Prasing file: " + file.getName());
                }
				ScrapeESEAMatch page = new ScrapeESEAMatch();
				String fName = file.getName();
				fName = fName.substring(0,fName.indexOf('.'));
				EseaMatch teams = page.parse(doc);
				if(teams != null) {
                    db_store.insertMatch(teams);
                }
			} catch(ScrapeException e) {
				System.err.println(e.getMessage());
			}
		}
	}
}
