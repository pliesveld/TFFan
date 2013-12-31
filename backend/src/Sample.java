import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import esea.EseaMatch;
import esea.EseaPlayer;
import esea.EseaTeam;
import esea.EseaTeamInfo;
import esea.EseaTeamRoster;
import esea.EseaTeamSchedule;

public class Sample
{

	Connection connection = null;
	Set<Integer> team_insert_cache;

	public Sample() throws ClassNotFoundException
	{
		Class.forName("org.sqlite.JDBC");

		try {
			connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
		} catch(SQLException e) {
			System.err.println("Failed to open connection to databse: " + e.getMessage());
			System.exit(3);
		}
		//	Connection connection = DriverManager.getConnection("jdbc:sqlite::memory:");

		team_insert_cache = new HashSet<Integer>();
	}

	public void createTables()
	{
		createTables(false);
	}
	public void createTables(boolean DropTables)
	{
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);

			if(DropTables)
			{
				statement.executeUpdate("drop table if exists esea_team");
				statement.executeUpdate("drop table if exists esea_player");
				statement.executeUpdate("drop table if exists esea_schedule");
				statement.executeUpdate("drop table if exists esea_match");
			}

			statement.executeUpdate("create table if not exists esea_team (tID INT(11) NOT NULL PRIMARY KEY, tName VARCHAR(24) NOT NULL, game VARCHAR(5) NOT NULL, league VARCHAR(15) NOT NULL)");
			statement.executeUpdate("create table if not exists esea_player (pID INT(11) NOT NULL PRIMARY KEY, pName VARCHAR(24) NOT NULL, tID DEFAULT NULL,score INT DEFAULT 0)");
			statement.executeUpdate("create table if not exists esea_schedule (mID INT(11) NOT NULL PRIMARY KEY, tID_home NOT NULL, tID_away NOT NULL, map VARCHAR(14) NOT NULL, score_home INT DEFAULT NULL, score_away INT DEFAULT NULL)");
			statement.executeUpdate("create table if not exists esea_match(mID INT(11) NOT NULL,pID INT(11) NOT NULL,stat_P INT DEFAULT 0,stat_PPM REAL DEFAULT 0.0,stat_DMG INT DEFAULT 0,stat_DDM REAL DEFAULT 0.0,g stat_F INT DEFAULT 0,stat_FPM REAL DEFAULT 0.0,stat_A INT DEFAULT 0,stat_APM REAL DEFAULT 0.0,stat_D INT DEFAULT 0,stat_DPM REAL DEFAULT 0.0,stat_CPC INT DEFAULT 0,stat_CPB INT DEFAULT 0,stat_DOM INT DEFAULT 0,stat_REV INT DEFAULT 0,stat_UC INT DEFAULT 0,stat_UCD INT DEFAULT 0, PRIMARY KEY(mID, pID))");
		} catch(SQLException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
	public void insertTeam(EseaTeamInfo teams)
	{
		String league = teams.getLeague();
		String game = teams.getGame();

		for( EseaTeam team : teams.getTeamsArray() )
		{
			String sqlStatement = null;
			try {
				Statement statement = connection.createStatement();
				sqlStatement = String.format("INSERT INTO esea_team values(%d, '%s', '%s', '%s')",
						team.teamId, team.teamName, game, league);
				statement.executeUpdate(sqlStatement);

			} catch(SQLException e)
			{
				if(e.getMessage().endsWith("not unique"))
				{
					System.err.println("Skipping.. " + league + " " + game);
				} else {
					e.printStackTrace();
				}
			}
		}

	}
	public void insertSchedule(EseaTeamSchedule team)
	{
		for(Map.Entry<Integer,EseaTeamSchedule.EseaScheduleEvent> match : team.getSchedule().entrySet())
		{

			int m_id = match.getKey();
			int home_id = match.getValue().getHome();
			int away_id = match.getValue().getAway();
			int home_score = match.getValue().getHome_score();
			int away_score = match.getValue().getAway_score();
			String map = match.getValue().getMap();
			String sqlStatement	= null;

			if(team_insert_cache.contains(m_id))
				continue;

			try {
				Statement statement = connection.createStatement();
				sqlStatement = String.format("INSERT INTO esea_schedule values(%d, %d, %d, '%s', %d, %d)",m_id, home_id, away_id, map, home_score, away_score);
				statement.executeUpdate(sqlStatement);
				team_insert_cache.add(m_id);
			} catch(SQLException e)	{
				if(!e.getMessage().endsWith("not unique"))
				{
					e.printStackTrace();
				}
			}
		}
	}

	public void insertRoster(EseaTeamRoster roster)
	{	
		//if(roster.isDead())
		for(EseaTeamRoster.EseaTeamRosterPlayerEntry player : roster.getRoster())
		{
			String sqlStatement = String.format("INSERT INTO esea_player (pID, pName, tID) VALUES(%d, '%s',%d)", player.getId(), player.getName(),roster.getTeamId());
			try {
				Statement statement = connection.createStatement();
				statement.executeUpdate(sqlStatement);
			} catch(SQLException e) {
				if(!e.getMessage().endsWith("not unique"))
				{
					System.err.println(sqlStatement);
					e.printStackTrace();
				}
			}
		}
	}
	public void insertMatch(EseaMatch match)
	{
		String match_id = match.match_id();
		try {
			if(match.getMatch_stats() != null && match.getMatch_stats().getPlayer_stats() != null)
				for(Map.Entry<EseaPlayer, Collection<String>> pstats : match.getMatch_stats().getPlayer_stats().entrySet())
				{

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
					statement.executeUpdate(result.toString());


				}
		} catch(SQLException e) {
			if(e.getMessage().endsWith("not unique"))
			{
				System.err.println("Skipping.. " + match_id);
			} else {
				e.printStackTrace();
			}
		}
	}

	public void updateScores()
	{
		
	}
}
