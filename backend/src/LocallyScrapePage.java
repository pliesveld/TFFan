import java.io.*;
import java.util.*;

import org.jsoup.nodes.Document;

import esea.scrape.ScrapeException;
import esea.scrape.ScrapePage;

public class LocallyScrapePage {
    enum Game {
        cs16, csgo, tf2
    }

    static void verify(File f) {
        if(!f.exists()) {
            System.err.println("Expected file not found: " + f.getPath());
            System.exit(1);
        }

    }


    /**
     * Utility test class to load pages from
     * filesystem.
     */
    public static void main(String[] args) {
        String eseaDirPath = "/home/happs/Source/TFFan/esea/";

        if( args.length > 1 )
            eseaDirPath = args[0];

        File eseaDir = new File(eseaDirPath);
        verify(eseaDir);

        String[] game_subdir = {"cs16", "csgo", "tf2"};
        String[] page_subdir = {"standings","matches","teams"};

        for(String game_d : game_subdir) {

            File f_game = new File(eseaDir, game_d);
            verify(f_game);


            int i = 0;
            for(String page : page_subdir) {
                File f;
                verify((f = new File(f_game,page)));


                String[] list;
                list = f.list();

                System.out.println(f.getPath());
                for( String dirItem : list ) {
                    try {
                        String web_file = f.getPath() + "/" + dirItem;
                        Document doc = ScrapePage.open_file(new File(web_file));
                        //System.out.println("File:" + web_file);
                        switch(i) {

                        case 0:
                            esea.scrape.ScrapeESEALeagueDivisions league = new esea.scrape.ScrapeESEALeagueDivisions();
                            esea.EseaDivision result = null;//league.parse(doc);
                            if(result != null)
                                System.out.println(result);
                            break;
                        case 1:
                            esea.scrape.ScrapeESEAMatch match = new esea.scrape.ScrapeESEAMatch();
                            esea.EseaMatch result2 = null;//match.parse(doc);
                            if(result2 != null)
                                System.out.println(result2);
                            break;
                        case 2:
                            esea.scrape.ScrapeESEATeam team = new esea.scrape.ScrapeESEATeam();
                            esea.EseaTeamRoster team_roster = team.parse_roster(doc);
                            esea.EseaTeamSchedule team_schedule = team.parse_schedule(doc);
                            //      if(team_roster != null && !team_roster.isDead())
                            //          System.out.println(team_roster);
                            if(team_schedule != null)
                                System.out.println(team_schedule);
                            break;
                        default:
                            System.err.println("???");
                            break;
                        }

                    } catch (ScrapeException e) {
                        e.printStackTrace();
                        System.exit(1);
                    }

                }
                ++i;
            }
        }

    }
}
