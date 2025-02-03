package demo.wrappers;

public class FilmsDitails {
    private  String flimYear;
    private  String filmTitle;
    private String filmNomination;
    private String filmAwards;
    private String filmWinner;

    public FilmsDitails(String flimYear, String filmTitle, String filmNomination, String filmAwards, String filmWinner) {
        this.flimYear = flimYear;
        this.filmTitle = filmTitle;
        this.filmNomination = filmNomination;
        this.filmAwards = filmAwards;
        this.filmWinner = filmWinner;
    }

    public String getFlimYear() {
        return flimYear;
    }

    public String getFilmTitle() {
        return filmTitle;
    }

    public String getFilmNomination() {
        return filmNomination;
    }

    public String getFilmAwards() {
        return filmAwards;
    }

    public String isFilmWinner() {
        return filmWinner;
    }
}
