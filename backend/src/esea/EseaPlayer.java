package esea;
import java.lang.*;

public class EseaPlayer {
    private String name;
    private int id;

    public EseaPlayer(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public int hashCode() {
        return id;
    }

    /*
        public boolean equals(Object other)
        {
            if(other instanceof EseaPlayer) {
                EseaPlayer otherPlayer = (EseaPlayer) other;
                return this.id == otherPlayer.id;
                        /*
                        (
                            (this.name == otherPlayer.name ||
                                ( this.name != null && otherPlayer.name != null &&
                                    this.name.equals(otherPlayer.name)))
                        &&
                            (this.id == otherPlayer.id ||
                                ( this.id != null && otherPlayer.id != null &&
                                    this.id.equals(otherPlayer.id)))
                        );
            } else if(other instanceof String){
                try {
                    int id = Integer.parseInt((String)other);
                    return (this.id == id );
                } catch( NumberFormatException nfe) {
                    String otherPlayer = (String) other;
                    return this.name == otherPlayer || this.name != null && otherPlayer != null && this.name.equals(otherPlayer);
                }
            }
            return false;
        }
        */
    public String toString() {
        return this.name + "(" + this.id + ")";
    }

    public String getName() {
        return this.name;
    }
    public int getId()   {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setId(int id)     {
        this.id = id;
    }
}
