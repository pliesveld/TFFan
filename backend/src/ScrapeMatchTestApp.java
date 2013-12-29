import java.io.*;
import java.util.*;

public class ScrapeMatchTestApp {
	
   public static void main(String[] args)
   {
	
	  Sample db_store = null;
	
	  try {
	     db_store = new Sample();
	  } catch(ClassNotFoundException e) {
	     System.err.println("Couldn't find database driver" + e.getMessage());
	     System.exit(2);
	  }
	
	  int i = 0;
	  for(String arg : args)
	  {
	     File file = new File(arg);
	     if(!file.exists())
	     {
	        System.out.println("Skipping ... " + file.getName());
	        continue;
	     }
	
	     ScrapeESEAMatch page = new ScrapeESEAMatch(file);
	     String fName = file.getName();
	     fName = fName.substring(0,fName.indexOf('.'));
	     EseaMatch teams = page.fetch(fName);
	     if(teams != null)
	    	 db_store.insertMatch(teams);
	  }
   }
}