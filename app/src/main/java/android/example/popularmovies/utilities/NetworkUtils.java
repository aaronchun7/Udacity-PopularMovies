package android.example.popularmovies.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils {

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String MOVIE_POPULAR_URL = "https://api.themoviedb.org/3/movie/popular";
    private static final String MOVIE_TOP_RATED_URL = "https://api.themoviedb.org/3/movie/top_rated";
    private static final String API_PARAM = "api_key";
    private static final String LANG_PARAM = "language";
    private static final String PAGE_PARAM = "page";
    private static final String REGION_PARAM = "region";

    //This is the recommended size; however, I might want to dynamically change this
    private static final String IMAGE_SIZE = "w185";

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
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
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
        } finally {
            urlConnection.disconnect();
        }
    }

}
