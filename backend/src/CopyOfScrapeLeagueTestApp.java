import java.io.*;
import java.util.*;

import org.jsoup.*;
import org.jsoup.nodes.Document;

import esea.EseaDivision;
import esea.scrape.ScrapeESEALeagueDivisions;
import esea.scrape.ScrapeException;
import esea.scrape.ScrapePage;
import esea.store.*;

public class CopyOfScrapeLeagueTestApp {
    public static void main(String[] args) {

        Storage db = null;

        try {
            db = new MySQL();
            db.createTables(true);
        } catch(ClassNotFoundException e) {
            System.err.println("Couldn't find database driver" + e.getMessage());
            System.exit(2);
        }

        int i = 0;
        for(String arg : args) {
            File file = new File(arg);
            if(!file.exists()) {
                System.out.println("Skipping ... " + file.getName());
                continue;
            }

            try {
                Document doc = ScrapePage.open_file(file);

                EseaDivision teams = ScrapeESEALeagueDivisions.parse(doc);
                if(teams != null)
                    db.insertTeam(teams);
            } catch(ScrapeException e) {
                System.err.println(e.getMessage());
            }

        }
    }
}
