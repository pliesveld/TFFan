import java.io.*;
import java.util.*;
import jsoup.*;

public class LocallyScrapePage
{
   enum Game 
    {
        cs16, csgo, tf2
    }

    static void verify(File f)
    {
        if(!f.exists())
        {
            System.err.println("Expected file not found: " + f.getPath());
            System.exit(1);
        }
    
    }

    
    public static void main(String[] args)
    {
        String eseaDirPath = "/home/happs/Source/TFFan/esea/";
        File eseaDir = new File(eseaDirPath);
        verify(eseaDir);

        String[] game_subdir = {"cs16", "csgo", "tf2"};
        String[] page_subdir = {"standings","matches","teams"};

        for(String game_d : game_subdir)
        {

            File f_game = new File(eseaDir, game_d);
            verify(f_game);

            for(String page : page_subdir)
            {
                File f;
                verify((f = new File(f_game,page)));


                String[] list;
                list = f.list();

                System.out.println(f.getPath());
                for( String dirItem : list )
                {
                    System.out.println("\t" + dirItem);
                }
            }
        }

    }
}
