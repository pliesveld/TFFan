

import java.io.IOException;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ScrapeESEAMatch {
	public static ESEAMatch fetch(String esea_team_id) {
		Document doc;
	    
	    String url = "http://play.esea.net/index.php?s=stats&d=match&id=" + esea_team_id;
		//String url = "http://localhost:32012/sample_match.html";

        ESEAMatch data = new ESEAMatch();
	    try {
//	    	String default_url = "http://play.esea.net/index.php";
//	    	Map<String, String> cookies = Jsoup.connect(default_url).execute().cookies();

	        doc = Jsoup.connect(url)
	        		//.cookies(cookies)
	        		.referrer(url).get();



	        Elements match_header = ScrapeUtility.validateSelect(doc, "div.match-header h1");
	        
	        Element match_home_team = ScrapeUtility.validateSelect(match_header,"a[href^=/teams/").first();
	        Element match_away_team = ScrapeUtility.validateSelect(match_header,"a + a[href^=/teams/").first();
	        
	        data.match_id = esea_team_id;
	        data.match_team_home = new String(match_home_team.text());
	        data.match_team_away = new String(match_away_team.text());
	        
	        data.match_team_home_id = ScrapeUtility.fetchAttrName(match_home_team);
	        data.match_team_away_id = ScrapeUtility.fetchAttrName(match_away_team);
	        
	        Elements match_stats = ScrapeUtility.validateSelect(doc,"div#body-match-stats.tabContent > table.box");
	        
	        assert match_stats.size() == 4;
	        
	        scrape_match_header(match_stats.get(0),data);
	        scrape_match_awards(match_stats.get(1),data);
	
	        ESEAStatsHeader stats_header = scrape_match_stats_header(match_stats.get(2));
	        
	        ESEAMatchStats m_stats = scrape_match_team_stats(match_stats.get(2));
	        data.match_team_home_stats = m_stats;
	        m_stats = scrape_match_team_stats(match_stats.get(3));
	        data.match_team_away_stats = m_stats;
	        
	        data.match_header = match_header.first().ownText();


	        //System.out.println(stats_header);
	    	//System.out.println(data.match_team_home_stats);
	    	//System.out.println(data.match_team_away_stats);
	        //System.out.println(data.toString());
	    } catch (ScrapeException|IOException e) {
	            e.printStackTrace();
	            return null;
	    }
	    return data;
	    
	}

	
	
	static void scrape_match_header(Element elem, ESEAMatch match)
	{
	//	System.out.println("match_header:" + elem.text());
	}
	
    static void scrape_match_awards(Element elem, ESEAMatch match) throws ScrapeException
    {
    	Map<String,String> match_awards = new TreeMap<String,String>();
    	Elements award_name = ScrapeUtility.validateSelect(elem,"tbody tr th acronym");
    	Elements awarded_player = ScrapeUtility.validateSelect(elem,"tbody tr td a + a");
    	
    	assert award_name.size() == awarded_player.size();

    	int size = award_name.size();

    	for(int i = 0; i < size;++i)
    		match_awards.put(award_name.get(i).ownText(), awarded_player.get(i).ownText());

    	match.match_awards = match_awards;
    }
    
    static ESEAStatsHeader scrape_match_stats_header(Element elem) throws ScrapeException
    {
    	ESEAStatsHeader result = new ESEAStatsHeader();
    	Map<String,String> field_desc = new HashMap<String,String>();
    	Collection<String> field = new ArrayList<String>();
    	
    	Elements stats_table = ScrapeUtility.validateSelect(elem,"thead + tbody tr th.acronym acronym");
    	
    	for(Element stat : stats_table)
    	{
    		field_desc.put(stat.ownText(),stat.attr("title"));
    		field.add(stat.ownText());
    	}
    	
    	result.description = field_desc;
    	result.field = field;
    	return result;
    }
    
    static ESEAMatchStats scrape_match_team_stats(Element elem) throws ScrapeException
    {
    	ESEAMatchStats match_stats = new ESEAMatchStats();
    	
    	Elements player_table = ScrapeUtility.validateSelect(elem,"thead + tbody + tbody > tr");

    	for(Element e : player_table)
    	{
        	Element player_name_node = ScrapeUtility.validateSelect(e,"td a + a[href^=/users/").first();
        	
        	String player_name = player_name_node.ownText();
        	int player_name_id = ScrapeUtility.fetchAttrName(player_name_node);
        	
    		Elements player_stats = ScrapeUtility.validateSelect(e,"td.stat");
    		
    		Collection<String> player_stats_array = new ArrayList<String>();
    		
    		for(Element stat : player_stats)
    		{
    			player_stats_array.add(stat.ownText());
    		}
    		
    		match_stats.addPlayer(player_name,player_name_id,player_stats_array);

    	}
    	

    
    	return match_stats;
    }

}

class ESEAMatchStats
{
	Map<EseaPlayer, Collection<String> > player_stats;
	public ESEAMatchStats()
	{
		player_stats = new HashMap<EseaPlayer, Collection<String>>();
	}
	
	public void addPlayer(String p_name, int p_id, Collection<String> p_stats)
	{
		EseaPlayer p = new EseaPlayer(p_name, p_id);
		player_stats.put(p,p_stats);
	}
	
	public String toString()
	{
		StringBuffer result = new StringBuffer(512);
		if(player_stats != null)
			for(Map.Entry<EseaPlayer, Collection<String>> pstats : player_stats.entrySet())
			{
				result.append(pstats.getKey() + ": " + pstats.getValue() + "\n");
			}
		return result.toString();
	}
}

class ESEAMatch
{
	String match_id;
	String match_header;
	int match_team_home_id;
	int match_team_away_id;
	String match_team_home;
	String match_team_away;
	
	ESEAMatchStats match_team_home_stats;
	ESEAMatchStats match_team_away_stats;
	
	Map<String,String> match_awards;
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer(512);
		sb.append("match #" + match_id)
		.append("\nhome: " + match_team_home + "(" + match_team_home_id +")")
		.append("\naway: " + match_team_away + "(" + match_team_away_id + ")")
		.append("\noutcome:" + match_header + "\n");

		if(match_awards != null)
			for(Map.Entry<String, String> award : match_awards.entrySet())
			{
				sb.append(award.getKey() + ": " + award.getValue() + "\n");
			}
	
		sb.append(match_team_home_stats);
		sb.append(match_team_away_stats);
		return sb.toString();
	}
	
}

class ESEAStatsHeader
{
	Map<String,String> description;
	Collection<String> field;
	
	public String toString()
	{
		return field.toString();
	}
}
