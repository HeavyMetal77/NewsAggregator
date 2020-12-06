package ua.tarastom.newsaggregator.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ua.tarastom.newsaggregator.models.Article;

public interface ApiInterface {

    @GET("top-headlines")
    Call<List<Article>> getNews(
            @Query("country") String country,
            @Query("language") String language
            );

    @GET("everything")
    Call<List<Article>> getNewsSearch(
            @Query("q") String keyWord,
            @Query("language") String language,
            @Query("sortBy") String sortBy
    );
}
