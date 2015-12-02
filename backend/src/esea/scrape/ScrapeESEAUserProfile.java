package esea.scrape;



import org.jsoup.nodes.Document;



public class ScrapeESEAUserProfile {
    public Document fetch(String esea_player_id) throws ScrapeException {
        String url_base = "http://play.esea.net/users/";
        String url_get_args = "?tab=stats&game_id=43&type_scope=league&period[type]=seasons&period[season_type]=regular+season";
        String url = url_base + esea_player_id + url_get_args;

        try {
            Integer.parseInt(esea_player_id);
        } catch(NumberFormatException e) {
            throw new ScrapeException("Invalid esea player id: " + esea_player_id);
        }

        return ScrapePage.open(url);
    }


    public String fetchClass(Document doc) throws ScrapeException {
        //http://play.esea.net/users/529573?tab=stats&last_type_scope=league&game_id=43&type_scope=league&period[type]=seasons

        return doc.select("#upanel-profile > div:eq(6) > table > tbody > tr + tr > td").first().ownText();
    }


}