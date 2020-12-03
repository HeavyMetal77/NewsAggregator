package ua.tarastom.newsaggregator.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ua.tarastom.newsaggregator.models.News;

public interface ApiInterface {

    @GET("top-headlines")
    Call<News> getNews(
            @Query("country") String country,
            @Query("apiKey") String apiKey

    );

}
