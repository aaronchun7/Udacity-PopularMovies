package android.example.popularmovies.utilities;

import android.example.popularmovies.R;
import android.example.popularmovies.model.Movie;
import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils {

    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String MOVIE_POPULAR_URL = "https://api.themoviedb.org/3/movie/popular";
    private static final String MOVIE_TOP_RATED_URL = "https://api.themoviedb.org/3/movie/top_rated";
    private static final String API_PARAM = "api_key";
    private static final String LANG_PARAM = "language";
    private static final String PAGE_PARAM = "page";
    private static final String REGION_PARAM = "region";

    //This is the recommended size; however, I might want to dynamically change this
    public static final String IMAGE_SIZE = "w185";

    private String mPosterPath;

    public static URL getPopularMovies(String language, int page, String region) {
        Uri popularUri = Uri.parse(MOVIE_POPULAR_URL).buildUpon()
                .appendQueryParameter(API_PARAM, ApiKeys.TMDB_API_KEY)
                .appendQueryParameter(LANG_PARAM, language)
                .appendQueryParameter(PAGE_PARAM, String.valueOf(page))
                .appendQueryParameter(REGION_PARAM, region)
                .build();

        URL url = null;
        try {
            url = new URL(popularUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL getTopRatedMovies(String language, int page, String region) {
        Uri topRatedUri = Uri.parse(MOVIE_TOP_RATED_URL).buildUpon()
                .appendQueryParameter(API_PARAM, ApiKeys.TMDB_API_KEY)
                .appendQueryParameter(LANG_PARAM, language)
                .appendQueryParameter(PAGE_PARAM, String.valueOf(page))
                .appendQueryParameter(REGION_PARAM, region)
                .build();

        URL url = null;
        try {
            url = new URL(topRatedUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpsUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A"); //This should grab from the beginning of the input

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return null;
    }

    public static String[] parseMovieListDataFromJson(String movieListJson) {
        if (movieListJson != null && !movieListJson.isEmpty()) {
            try {
                JSONObject movieListData = new JSONObject(movieListJson);
                String pageNumber = movieListData.getString("page");
                String totalResults = movieListData.getString("total_results");
                String totalPages = movieListData.getString("total_pages");
                String results = movieListData.getString("results");
                return new String[]{pageNumber, totalResults, totalPages, results};
            } catch(JSONException je) {
                je.printStackTrace();
            }
        }
        return null;
    }

    public static List<String> parseMovieListFromJson(String resultsJson) {
        if (resultsJson != null && !resultsJson.isEmpty()) {
            try {
                JSONArray movieJsonArray = new JSONArray(resultsJson);
                List<String> movieList = new ArrayList<>();
                for (int i = 0; i < movieJsonArray.length(); i++) {
                    movieList.add(movieJsonArray.getJSONObject(i).toString());
                }
                return movieList;
            } catch(JSONException je) {
                je.printStackTrace();
            }
        }
        return null;
    }

    public static Movie parseMovieDataFromJson(String movieDataJson) {
        if (movieDataJson != null && !movieDataJson.isEmpty()) {
            try {
                JSONObject movieData = new JSONObject(movieDataJson);
                Movie movie = new Movie();
                movie.setPopularity(movieData.getInt("popularity"));
                movie.setVoteCount(movieData.getInt("vote_count"));
                movie.setVideo(movieData.getBoolean("video"));
                movie.setPosterPath(movieData.getString("poster_path"));
                movie.setMovieId(movieData.getInt("id"));
                movie.setAdult(movieData.getBoolean("adult"));
                movie.setBackdropPath(movieData.getString("backdrop_path"));
                movie.setOriginalLanguage(movieData.getString("original_language"));
                movie.setOriginalTitle(movieData.getString("original_title"));

                JSONArray genreIdsJson = movieData.getJSONArray("genre_ids");
                List<Integer> genreIds = new ArrayList<>();
                for (int i = 0; i < genreIdsJson.length(); i++) {
                    String genreIdStr = genreIdsJson.get(i).toString();
                    genreIds.add(Integer.parseInt(genreIdStr));
                }
                movie.setGenreIds(genreIds);

                movie.setTitle(movieData.getString("title"));
                movie.setVoteAverage(movieData.getDouble("vote_average"));
                movie.setOverview(movieData.getString("overview"));
                movie.setReleaseDate(movieData.getString("release_date"));

                return movie;
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }
        return null;
    }
}
