import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Sample
{

    Connection connection = null;
    Set<Integer> commit_cache;
    
    public Sample() throws ClassNotFoundException
    {
        Class.forName("org.sqlite.JDBC");
        try
        {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");

            // specify database to be stored in memory
            //	Connection connection = DriverManager.getConnection("jdbc:sqlite::memory:");


            //specify databsae on file
            //Connection connection = DriverManager.getConnection("jdbc:sqlite:/home/leo/work/mydatabase.db");

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists esea_team");
            statement.executeUpdate("create table esea_team (tID INT(11) NOT NULL PRIMARY KEY, tName VARCHAR(24) NOT NULL, game VARCHAR(5) NOT NULL, league VARCHAR(15) NOT NULL)");

            statement.executeUpdate("drop table if exists esea_schedule");
            statement.executeUpdate("create table esea_schedule (mID INT(11) NOT NULL PRIMARY KEY, tID_home NOT NULL, tID_away NOT NULL, map VARCHAR(14) NOT NULL, score_home INT DEFAULT NULL, score_away INT DEFAULT NULL)");
 
            statement.executeUpdate("drop table if exists esea_match");
            statement.executeUpdate("CREATE TABLE esea_match(   mID   INT(11)     NOT NULL,   pID   INT(11)     NOT NULL,   stat_P      INT   DEFAULT 0,  stat_PPM REAL     DEFAULT 0.0,   stat_DMG INT   DEFAULT 0,     stat_DDM REAL     DEFAULT 0.0,   stat_F      INT   DEFAULT 0,  stat_FPM REAL     DEFAULT  0.0,  stat_A      INT   DEFAULT  0, stat_APM REAL     DEFAULT 0.0,   stat_D      INT   DEFAULT 0,  stat_DPM REAL     DEFAULT 0.0,   stat_CPC INT   DEFAULT 0,  stat_CPB INT   DEFAULT 0,  stat_DOM INT   DEFAULT 0,  stat_REV INT   DEFAULT 0,  stat_UC  INT   DEFAULT 0,  stat_UCD INT   DEFAULT 0 )");
            /*
              statement.executeUpdate("insert into person values(1, 'leo')");
              statement.executeUpdate("insert into person values(2, 'yui')");
              ResultSet rs = statement.executeQuery("select * from person");
              while(rs.next())
              {
                // read the result set
                System.out.println("name = " + rs.getString("name"));
                System.out.println("id = " + rs.getInt("id"));
              }
            */
            commit_cache = new HashSet<Integer>();
        }
        catch(SQLException e)
        {

            // if the error message is "out of memory",
            // it probably means no database file is found
				e.printStackTrace();
            System.err.println(e.getMessage());
				System.exit(1);
        }
    }

	public void insertTeam(EseaTeamInfo teams)
	{
		String league = teams.league;
		String game = teams.game;

		for( EseaTeam team : teams.teamsArray )
		{
			String sqlStatement = null;
			try {
            Statement statement = connection.createStatement();
				sqlStatement = String.format("INSERT INTO esea_team values(%d, '%s', '%s', '%s')",
				 team.teamId, team.teamName, game, league);
				statement.executeUpdate(sqlStatement);
				
			} catch(SQLException e)
			{
				e.printStackTrace();
				if(sqlStatement != null)
					System.err.println("Skipping... " + e.getMessage() + "sql: " + sqlStatement);
			}
		}

	}

	public void insertSchedule(EseaTeamSchedule team)
	{
		for(Map.Entry<Integer,EseaScheduleEvent> match : team.schedule.entrySet())
		{
			int m_id = match.getKey();
			int home_id = match.getValue().home;
			int away_id = match.getValue().away;
			int home_score = match.getValue().home_score;
			int away_score = match.getValue().away_score;
			String map = match.getValue().map;
			String sqlStatement	= null;

			if(commit_cache.contains(m_id))
				continue;
			
			try {
				Statement statement = connection.createStatement();
				sqlStatement = String.format("INSERT INTO esea_schedule values(%d, %d, %d, '%s', %d, %d)",m_id, home_id, away_id, map, home_score, away_score);
				statement.executeUpdate(sqlStatement);
				commit_cache.add(m_id);
			} catch(SQLException e)	{
		//		e.printStackTrace();
	//			System.err.println(e.getMessage());
				if(sqlStatement != null)
					System.err.println("Skipping... " + e.getMessage() + "sql: " + sqlStatement);
			}

		}
	}
	
	public void insertMatch(EseaMatch match)
	{
		String match_id = match.match_id;
		
		if(match.match_team_home_stats != null && match.match_team_home_stats.player_stats != null)
			for(Map.Entry<EseaPlayer, Collection<String>> pstats : match.match_team_home_stats.player_stats.entrySet())
			{
				try {
					StringBuilder result = new StringBuilder(256);
					result.append("INSERT INTO esea_match VALUES(");
					result.append(match_id);					
					result.append("," + pstats.getKey().getId());
					
					for(String stat : pstats.getValue())
					{
						result.append(",");
						result.append(stat);
					}

					result.append(")");
					
					Statement statement = connection.createStatement();
					System.err.println(result.toString());
					statement.executeUpdate(result.toString());
				

				} catch(SQLException e) {
					e.printStackTrace();
				}
	
			}
	}
	
}
