
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ScrapeUtility {
	static int fetchAttrName(Element n) throws ScrapeException
	{
		if(!n.hasAttr("href"))
			throw new ScrapeException("Expected element to have href attribute." + n);
		try {
			String href_attr = n.attr("href").substring(7);
			int result = Integer.parseInt(href_attr);
			return result;
		} catch(IndexOutOfBoundsException|NumberFormatException e) {
			throw new ScrapeException("invalid integer attribute:" + n.attr("href"),e);
		}
	}
	
	static Elements validateSelect(Elements e, String cssQuerry) throws ScrapeException
	{
		Elements result;
		if((result = e.select(cssQuerry)) == null)
			throw new ScrapeException("select " + cssQuerry + " expected to be matched " + e);
		return result;
	}
	static Elements validateSelect(Document e, String cssQuerry) throws ScrapeException
	{
		Elements result;
		if((result = e.select(cssQuerry)) == null)
			throw new ScrapeException("select " + cssQuerry + " expected to be matched " + e);
		return result;
	}
	static Elements validateSelect(Element e, String cssQuerry) throws ScrapeException
	{
		Elements result;
		if((result = e.select(cssQuerry)) == null)
			throw new ScrapeException("select " + cssQuerry + " expected to be matched " + e);
		return result;
	}
}
