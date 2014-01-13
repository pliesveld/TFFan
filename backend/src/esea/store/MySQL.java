package esea.store;
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

public class MySQL extends Storage
{

	Connection connection;

	public MySQL() throws ClassNotFoundException
	{
		Class.forName("org.sqlite.JDBC");
		
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/happs","happs","");
		} catch(SQLException e) {
			System.err.println("Failed to open connection to databse: " + e.getMessage());
			System.exit(3);
		}


		team_insert_cache = new HashSet<Integer>();
	}
	

	@Override
	public void createTables(boolean DropTables)
	{
		try {
			Statement statement = getConnection().createStatement();
			statement.setQueryTimeout(30);

			if(DropTables)
			{
				statement.executeUpdate("drop table if exists esea_team");
				statement.executeUpdate("drop table if exists esea_player");
				statement.executeUpdate("drop table if exists esea_schedule");
				statement.executeUpdate("drop table if exists esea_match");
			}

			statement.executeUpdate("create table if not exists esea_team (tID INT(11) NOT NULL PRIMARY KEY, tName VARCHAR(24) NOT NULL, game VARCHAR(7) NOT NULL, league VARCHAR(15) NOT NULL)");
			statement.executeUpdate("create table if not exists esea_player (pID INT(11) NOT NULL PRIMARY KEY, pName VARCHAR(24) NOT NULL, tID INT DEFAULT NULL, score INT DEFAULT NULL)");
			statement.executeUpdate("create table if not exists esea_schedule (mID INT(11) NOT NULL PRIMARY KEY, tID_home INT NOT NULL, tID_away INT NOT NULL, map VARCHAR(14) NOT NULL, score_home INT DEFAULT NULL, score_away INT DEFAULT NULL)");
			statement.executeUpdate("create table if not exists esea_match (mID INT(11) NOT NULL,pID INT(11) NOT NULL,stat_P INT DEFAULT 0,stat_PPM REAL DEFAULT 0.0,stat_DMG INT DEFAULT 0,stat_DDM REAL DEFAULT 0.0,stat_F INT DEFAULT 0,stat_FPM REAL DEFAULT 0.0,stat_A INT DEFAULT 0,stat_APM REAL DEFAULT 0.0,stat_D INT DEFAULT 0,stat_DPM REAL DEFAULT 0.0,stat_CPC INT DEFAULT 0,stat_CPB INT DEFAULT 0,stat_DOM INT DEFAULT 0,stat_REV INT DEFAULT 0,stat_UC INT DEFAULT 0,stat_UCD INT DEFAULT 0, PRIMARY KEY(mID, pID))");
		} catch(SQLException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}

	@Override
	public void updateScores()
	{

	}

	@Override
	public Connection getConnection() throws SQLException {
		return connection;
		//return DriverManager.getConnection("jdbc:mysql://localhost/happs","happs","");
	}

}
