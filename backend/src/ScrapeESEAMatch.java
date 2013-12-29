

import java.io.IOException;
import java.io.File;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ScrapeESEAMatch extends ScrapePage {

	public ScrapeESEAMatch()
	{
		super();
	}

	public ScrapeESEAMatch(String file_name)
	{
		super(file_name);
	}

	public ScrapeESEAMatch(File file)
	{
		super(file);
	}


	public EseaMatch fetch(String esea_match_id) {
	    
	    String default_url = "http://play.esea.net/index.php?s=stats&d=match&id=" + esea_match_id;

        EseaMatch data = new EseaMatch();
	    try {

			if(doc == null)
	        doc = Jsoup.connect(default_url)
	        		.cookies(cookies)
	        		.referrer(default_url).get();


			Elements match_page = ScrapeUtility.validateSelect(doc, "div#layout-column-center");
			
	        Elements match_status = ScrapeUtility.validateSelect(match_page, "div.module-header:first-child");
	        
	        
	        String status_str = match_status.first().ownText(); 

	        Elements match_header = ScrapeUtility.validateSelect(match_page, "div#stats-match-view div.match-header > h1");
	        
	        Element match_home_team = ScrapeUtility.validateSelect(match_header,"a[href^=/teams/").first();
	        Element match_away_team = ScrapeUtility.validateSelect(match_header,"a + a[href^=/teams/").first();

	        data.match_id = esea_match_id;
	        data.match_team_home = new String(match_home_team.text());
	        data.match_team_away = new String(match_away_team.text());
	        
	        data.match_team_home_id = ScrapeUtility.fetchAttrHrefAsInt(match_home_team);
	        data.match_team_away_id = ScrapeUtility.fetchAttrHrefAsInt(match_away_team);
	        

	        if(status_str.contains("Forfeit"))
	        {
	        	data.status = MatchStatus.FORFEIT;
	        	return data;
	        } else if(!status_str.contains("Completed")) {
	        	data.status = MatchStatus.SCRAPE_ERROR;
	        	throw new ScrapeException("Unknown module-header: " + status_str);
	        } else {
	        	data.status = MatchStatus.COMPLETED;
	        }
	        
	        
	        
	        Elements match_stats = ScrapeUtility.validateSelect(doc,"div#body-match-stats.tabContent > table.box");
	        
	        
	        if(match_stats.size() != 4)
	        	throw new ScrapeException("Invalid stats table.");

	        assert match_stats.size() == 4;
	        
	   //     scrape_match_header(match_stats.get(0),data);
	        scrape_match_awards(match_stats.get(1),data);
	
	 //       ESEAStatsHeader stats_header = scrape_match_stats_header(match_stats.get(2));
	        
	        EseaMatchStats player_stats = new EseaMatchStats();   
	        player_stats.addAll(scrape_match_team_stats(match_stats.get(2)));
	        player_stats.addAll(scrape_match_team_stats(match_stats.get(3));
	        
	        data.match_team_home_stats = player_stats;

	        
	        data.match_header = match_header.first().ownText();


	        //System.out.println(stats_header);
	    	System.out.println(data.match_team_home_stats);
	    	//System.out.println(data.match_team_away_stats);
	        //System.out.println(data.toString());
	    } catch (ScrapeException|IOException e) {
	            e.printStackTrace();
	            data = null;
	    }
	    return data;
	    
	}

	
	
	static void scrape_match_header(Element elem, EseaMatch match)
	{
	//	System.out.println("match_header:" + elem.text());
	}
	
    static void scrape_match_awards(Element elem, EseaMatch match) throws ScrapeException
    {
    	Map<String,String> match_awards = new TreeMap<String,String>();
    	Elements award_name = ScrapeUtility.validateSelect(elem,"tbody tr th acronym");
    	Elements awarded_player = ScrapeUtility.validateSelect(elem,"tbody tr td a + a");
    	
    	if(award_name.size() != awarded_player.size())
    		throw new ScrapeException("failed to players in player awards table.");

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
    
    static void scrape_match_team_stats(Element elem, EseaMatchStats match_stats) throws ScrapeException
    {
    	
    	
    	Elements player_table = ScrapeUtility.validateSelect(elem,"thead + tbody + tbody > tr");

    	for(Element e : player_table)
    	{
        	Element player_name_node = ScrapeUtility.validateSelect(e,"td a + a[href^=/users/").first();
        	
        	String player_name = player_name_node.ownText();
        	int player_name_id = ScrapeUtility.fetchAttrHrefAsInt(player_name_node);
        	
    		Elements player_stats = ScrapeUtility.validateSelect(e,"td.stat");
    		
    		Collection<String> player_stats_array = new ArrayList<String>();
    		
    		for(Element stat : player_stats)
    		{
    			player_stats_array.add(stat.ownText());
    		}
    		
    		match_stats.addPlayer(player_name,player_name_id,player_stats_array);

    	}
    	

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
