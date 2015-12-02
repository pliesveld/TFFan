package esea;
import java.util.*;

public class EseaTeam {
    public String teamName;
    public int teamId;
    public boolean isDead;

    public EseaTeam(String teamName, int id, boolean isDead) {
        this.teamName = teamName;
        this.teamId = id;
        this.isDead = isDead;
    }

    public EseaTeam(String teamName, int id) {
        this(teamName,id,false);
    }

    public String toString() {
        return " team: " + teamName + " id: " + teamId;
    }
}
