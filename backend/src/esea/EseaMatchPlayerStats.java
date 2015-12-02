package esea;
import java.util.*;


public class EseaMatchPlayerStats {
    private Map<EseaPlayer, Collection<String> > player_stats;
    public EseaMatchPlayerStats() {
        setPlayer_stats(new HashMap<EseaPlayer, Collection<String>>());
    }

    public void addPlayer(String p_name, int p_id, Collection<String> p_stats) {
        EseaPlayer p = new EseaPlayer(p_name, p_id);
        getPlayer_stats().put(p,p_stats);
    }

    public String toString() {
        StringBuffer result = new StringBuffer(512);
        if(getPlayer_stats() != null)
            for(Map.Entry<EseaPlayer, Collection<String>> pstats : getPlayer_stats().entrySet()) {
                result.append(pstats.getKey() + ": " + pstats.getValue() + "\n");
            }
        return result.toString();
    }

    public Map<EseaPlayer, Collection<String> > getPlayer_stats() {
        return player_stats;
    }

    public void setPlayer_stats(Map<EseaPlayer, Collection<String> > player_stats) {
        this.player_stats = player_stats;
    }
}
