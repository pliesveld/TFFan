package esea.scrape;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ScrapeUtility {

    static public class ESEA {
        static String validateLeagueHeader(Document doc) throws ScrapeException {
            Elements league = ScrapeUtility.validateSelect(doc,"div#league-standings div h1");
            if(league.size() != 1)
                throw new ScrapeException("Unexpected league size " + league.size());
            String league_str = league.first().text().substring(3);


            if(!league_str.startsWith("TF2")
                    && !league_str.startsWith("CS: GO")
                    && !league_str.startsWith("CS 1.6"))
                throw new ScrapeException("Invalid league header: " + league_str);

            return league_str;
        }

    }

    static int parseInt(String arg) throws ScrapeException {
        try {
            int result = Integer.parseInt(arg);
            return result;
        } catch(IndexOutOfBoundsException|NumberFormatException e) {
            throw new ScrapeException("invalid integer:" + arg);
        }
    }

    static int fetchAttrHrefAsInt(Element n) throws ScrapeException {
        return fetchAttrHrefAsInt(n,7);
    }

    static int fetchAttrHrefAsInt(Element n, int offset) throws ScrapeException {
        if(!n.hasAttr("href"))
            throw new ScrapeException("Expected element to have href attribute." + n);
        try {
            String href_attr = n.attr("href").substring(offset);
            int result = parseInt(href_attr);
            return result;
        } catch(IndexOutOfBoundsException e) {
            throw new ScrapeException("invalid integer attribute:" + n.attr("href"),e);
        }
    }

    static <T extends org.jsoup.nodes.Element> Elements validateSelect(T e, String cssQuerry) throws ScrapeException {
        Elements result;
        if((result = e.select(cssQuerry)) == null || result.size() == 0)
            throw new ScrapeException("select " + cssQuerry + " expected to be matched " + e);
        return result;
    }

    static Elements validateSelect(Elements e, String cssQuerry) throws ScrapeException {
        Elements result;
        if((result = e.select(cssQuerry)) == null || result.size() == 0)
            throw new ScrapeException("select " + cssQuerry + " expected to be matched " + e);
        return result;
    }



    static Element validateSingleSelect(Document e, String cssQuerry) throws ScrapeException {
        Elements result;
        if((result = e.select(cssQuerry)) == null || result.first() == null)
            throw new ScrapeException("select " + cssQuerry + " expected to be matched.");


        return result.first();
    }
}
