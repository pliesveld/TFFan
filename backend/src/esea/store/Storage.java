package esea.store;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import esea.EseaMatch;
import esea.EseaPlayer;
import esea.EseaTeam;
import esea.EseaDivision;
import esea.EseaTeamRoster;
import esea.EseaTeamSchedule;


public abstract class Storage {

    Set<Integer> team_insert_cache;

    public Storage() {
        super();
    }

    public abstract Connection getConnection() throws SQLException;
    public abstract void createTables(boolean DropTables);

    public void createTables() {
        createTables(false);
    }

    public abstract void updateScores();

    public void insertTeam(EseaDivision teams) {
        String league = teams.getLeague();
        String game = teams.getGame();

        for( EseaTeam team : teams.getTeamsArray() ) {
            String sqlStatement = null;
            try {
                Statement statement = getConnection().createStatement();
                sqlStatement = String.format("INSERT INTO esea_team values(%d, '%s', '%s', '%s')",
                                             team.teamId, team.teamName, game, league);
                statement.executeUpdate(sqlStatement);

            } catch(SQLException e) {
                if(e.getMessage().endsWith("not unique")) {
                    System.err.println("Skipping.. " + league + " " + game);
                } else {
                    e.printStackTrace();
                }
            }
        }
    }

    public void insertRoster(EseaTeamRoster roster) {
        //if(roster.isDead())
        for(EseaTeamRoster.EseaTeamRosterPlayerEntry player : roster.getRoster()) {
            String sqlStatement = String.format("INSERT INTO esea_player (pID, pName, tID) VALUES(%d, '%s',%d)", player.getId(), player.getName(),roster.getTeamId());
            try {
                Statement statement = getConnection().createStatement();
                statement.executeUpdate(sqlStatement);
            } catch(SQLException e) {
                if(!e.getMessage().endsWith("not unique")) {
                    System.err.println(sqlStatement);
                    e.printStackTrace();
                }
            }
        }
    }

    public void insertSchedule(EseaTeamSchedule team) {
        for(Map.Entry<Integer,EseaTeamSchedule.EseaScheduleEvent> match : team.getSchedule().entrySet()) {

            int m_id = match.getKey();
            int home_id = match.getValue().getHome();
            int away_id = match.getValue().getAway();
            int home_score = match.getValue().getHome_score();
            int away_score = match.getValue().getAway_score();
            String map = match.getValue().getMap();
            String sqlStatement = null;

            if(team_insert_cache.contains(m_id))
                continue;

            try {
                Statement statement = getConnection().createStatement();
                sqlStatement = String.format("INSERT INTO esea_schedule values(%d, %d, %d, '%s', %d, %d)",m_id, home_id, away_id, map, home_score, away_score);
                statement.executeUpdate(sqlStatement);
                team_insert_cache.add(m_id);
            } catch(SQLException e) {
                if(!e.getMessage().endsWith("not unique")) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void insertMatch(EseaMatch match) {
        String match_id = match.match_id();
        try {
            if(match.getMatch_stats() != null && match.getMatch_stats().getPlayer_stats() != null)
                for(Map.Entry<EseaPlayer, Collection<String>> pstats : match.getMatch_stats().getPlayer_stats().entrySet()) {

                    StringBuilder result = new StringBuilder(256);
                    result.append("INSERT INTO esea_match VALUES(");
                    result.append(match_id);
                    result.append("," + pstats.getKey().getId());

                    for(String stat : pstats.getValue()) {
                        result.append(",");
                        result.append(stat);
                    }

                    result.append(")");
                    Statement statement = getConnection().createStatement();
                    statement.executeUpdate(result.toString());


                }
        } catch(SQLException e) {
            if(e.getMessage().endsWith("not unique")) {
                System.err.println("Skipping.. " + match_id);
            } else {
                e.printStackTrace();
            }
        }
    }

}
