package demo.wrappers;

public class TeamDitails {
    public String teamName;
    public String year;
    public String winPercent;
    
    public void putData(String teamName, String year, String winPercent){
        this.teamName = teamName;
        this.year = year;
        this.winPercent = winPercent;
    }
    public TeamDitails getData(){
        return this;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getYear() {
        return year;
    }

    public String getWinPercent() {
        return winPercent;
    }

    
}
