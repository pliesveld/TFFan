package esea.scrape;

import java.io.File;
import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class ScrapePage {
    public static Document open(String url) throws ScrapeException {
        Connection conn = null;
        Document doc = null;
        try {

            conn = Jsoup.connect(url)
                   .cookie("viewed_welcome_page", "1");

//      System.err.println("Request Headers\n" + conn.request().headers());
            Response response = conn.execute();

//      System.err.println("HTTP STATUS " + response.statusCode());
//      System.err.println("Response Headers\n" + response.headers());
            doc = response.parse();
        } catch(IOException e) {
            throw new ScrapeException("Failed to open page: " + e.getMessage() + "\nurl:" + url,e);
        }
        return doc;
    }

    public static Document open_file(File input) throws ScrapeException {
        Document doc;
//      System.out.println("ScrapePage(" + input.getName() + ")");
        try {
            doc = Jsoup.parse(input,"UTF-8", "file:// " + input.getName());
        } catch(IOException e) {
            throw new ScrapeException("Couldn't load document from file: " + input.getPath(), e);
        }
        return doc;
    }

//  abstract Document fetchPage()            throws ScrapeException;
//  abstract void scrapePage(Document d) throws ScrapeException;
}
