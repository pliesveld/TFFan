package esea;

import esea.scrape.ScrapeESEALeagueDivisions;
import esea.scrape.ScrapeException;
import esea.scrape.ScrapePage;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScrapeSeasonLeagueApp {
    public static void main(String[] args) throws ScrapeException {
        Document doc = ScrapePage.open("http://play.esea.net/index.php?s=league&d=standings");
        Elements season = doc.select("select > optgroup:eq(1)");

        assert season.hasAttr("label");
        String season_label = season.attr("label");
        Elements current_league_division = season.select("option[value]");

        Elements what = doc.select("div.division");
        Pattern p = Pattern.compile("^Season (?<season>[0-9]{1,2}) / (?<startdate>[A-Z][a-z]{2} [0-9]{1,2}, [0-9]{4}) - (?<enddate>[A-Z][a-z]{2} [0-9]{1,2}, [0-9]{4})$");

        String current_season = what.first().ownText();
        Matcher m = p.matcher(current_season);

        //System.err.println("current:" + current_season);

        if(!m.matches())
            return;





        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MMM d, yyyy");
        try {
            Date dateStart = dateFormat.parse(m.group("startdate"));
            Date dateEnd =   dateFormat.parse(m.group("enddate"));

            Date dateToday = new Date();

            System.out.println("Today: " + dateToday);
            System.out.println("Season: " + m.group("season"));
            System.out.println("Season Start: " + dateStart);
            System.out.println("Season End: " + dateEnd);



            if(dateStart.after(dateToday)) {
                System.out.println("Season hasn't started yet.");
            } else if(dateEnd.before(dateToday)) {
                System.out.println("Season has ended.");
            } else if(dateToday.after(dateStart) && dateToday.before(dateEnd)) {
                System.out.println("Current: " + m.group("season"));
                System.out.println("Current Season: " + season_label);
                for(Element e : current_league_division) {
                    assert(e.hasText() && e.hasAttr("value"));
                    Game league_game = Game.parse(e.ownText());
                    System.out.print("League: " + league_game);
                    String div_id = e.attr("value");

                    Document div_doc = ScrapeESEALeagueDivisions.fetch(div_id);
                    EseaDivision esea_div = ScrapeESEALeagueDivisions.parse(div_doc);

                    for(EseaTeam team : esea_div.getTeamsArray()) {
                        System.out.println("curl --create-dirs -o esea/" + league_game + "/teams/" + team.teamId + ".html \"http://play.esea.net/teams/" + team.teamId + "\"");
                    }


                }

            } else {
                System.err.println("Invalid season dates.");
            }
        } catch (ParseException e1) {
            e1.printStackTrace();
        }




        /*
        try {
            ScrapeESEALeagueDivisions page = new ScrapeESEALeagueDivisions();
            page.fetch();
            String fName = file.getName();
            fName = fName.substring(0,fName.indexOf('.'));

            EseaDivision teams = page.fetch(fName);
            if(teams != null)
                db_store.insertTeam(teams);
        } catch(ScrapeException e) {
            System.err.println(e.getMessage());
        }
         */
    }

}

enum Game {
    cs16, csgo, tf2;
    static Pattern game_reg = Pattern.compile("(CS 1.6|CS: GO|TF2) (Open|Main|Intermediate|Invite)");
    static Game parse(String from) throws ScrapeException
    {
        Game result;
        Matcher m = game_reg.matcher(from);
        if(!m.matches())
            throw new ScrapeException("Invalid game string:" + from);
        else if(from.charAt(2) == ' ')
            return Game.cs16;
        else if(from.charAt(2) == ':')
            return Game.csgo;
        else if(from.charAt(2) == '2')
            return Game.tf2;
        return null;
    }
}


