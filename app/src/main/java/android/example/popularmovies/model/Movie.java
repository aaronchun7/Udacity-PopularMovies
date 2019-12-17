package android.example.popularmovies.model;

import java.util.List;

public class Movie {
    private int popularity;
    private int voteCount;
    private boolean isVideo;
    private String posterPath;
    private int movieId;
    private boolean isAdult;
    private String backdropPath;
    private String originalLanguage;
    private String originalTitle;
    private List<Integer> genreIds;
    private String title;
    private double voteAverage;
    private String overview;
    private String releaseDate; //In YYYY-MM-DD format

    public Movie() {
        // Default Constructor
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
