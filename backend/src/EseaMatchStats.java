import java.util.*;


class EseaMatchStats
{
		Map<EseaPlayer, Collection<String> > player_stats;
		public EseaMatchStats()
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
