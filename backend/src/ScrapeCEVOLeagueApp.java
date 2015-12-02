import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.regex.*;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import esea.EseaDivision;
import esea.EseaTeam;
import esea.scrape.ScrapeESEALeagueDivisions;
import esea.scrape.ScrapeException;
import esea.scrape.ScrapePage;

public class ScrapeCEVOLeagueApp
{
    public static void parseRoster(Elements roster) throws ScrapeException
    {
        ListIterator<Element> list = roster.listIterator();

        while(list.hasNext())
        {
            Element member = list.next();
            System.out.println(member.ownText());
        }
    }

    public static void parseMatches(Elements matches) throws ScrapeException
    {

        for( Element match : matches )
        {
            System.out.println(match.ownText());
        }

    }


	public static void main(String[] args) throws ScrapeException
	{
        try {
            Document doc = ScrapePage.open("http://cevo.com/event/tf2-6v6/roster/15417/");

            Elements roster = doc.select("div#roster a[href^=/member/]");
            Elements matches = doc.select("div#matches a[href^=/event/tf2-6v6/match]");

            parseRoster(roster);
            parseMatches(matches);




		} catch (ScrapeException e1) {
			e1.printStackTrace();
		}

    }
}

enum Game
{
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


