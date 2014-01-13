import java.io.*;
import java.util.*;

import esea.EseaMatch;
import esea.scrape.ScrapeESEAMatch;
import esea.scrape.ScrapeException;
import esea.store.*;

public class ScrapeMatchTestApp {
	
   public static void main(String[] args)
   {
	
	  Storage db_store = null;
	
	  try {
	     db_store = new MySQL();
	     db_store.createTables();
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
	
	     try {
	    	 ScrapeESEAMatch page = new ScrapeESEAMatch(file);
	    	 String fName = file.getName();
	    	 fName = fName.substring(0,fName.indexOf('.'));
	    	 EseaMatch teams = page.fetch(fName);
	    	 if(teams != null)
	    		 db_store.insertMatch(teams);
	     } catch(ScrapeException e) {
	    	 System.err.println(e.getMessage());
	     }
	  }
   }
}
