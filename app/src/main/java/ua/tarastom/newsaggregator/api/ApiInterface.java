package ua.tarastom.newsaggregator.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ua.tarastom.newsaggregator.models.News;

public interface ApiInterface {

    @GET("find")
    Call<News> getFeed(
            @Query("key") String keyWord,
            @Query("country") String country,
            @Query("category") String category,
            @Query("language") String language,
            @Query("titleChannel") String titleChannel,
            @Query("page") int page,
            @Query("size") int size
    );
}
