package BE;

public class Team {

    int TeamId;
    String TeamName;

    public Team(int id, String name) {
        this.TeamId = id;
        this.TeamName = name;
    }

    public Team(String name) {
        this.TeamName = name;
    }

    public int getTeamId() {
        return TeamId;
    }

    public void setTeamId(int teamId) {
        TeamId = teamId;
    }

    public String getTeamName() {
        return TeamName;
    }

    public void setTeamName(String teamName) {
        TeamName = teamName;
    }

    @Override
    public String toString() {
        return TeamName;
    }
}
