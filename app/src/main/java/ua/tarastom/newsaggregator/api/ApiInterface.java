package ua.tarastom.newsaggregator.api;

import retrofit2.Call;
import retrofit2.http.GET;
import ua.tarastom.newsaggregator.models.ArticleResponse;

public interface ApiInterface {

    //5Channel
    @GET("rss")
    Call<ArticleResponse> getChannel();

//    @GET("top-headlines")
//    Call<ArticleResponse> getNews(
//            @Query("country") String country,
//            @Query("language") String language
//            );
//
//    @GET("everything")
//    Call<ArticleResponse> getNewsSearch(
//            @Query("q") String keyWord,
//            @Query("language") String language,
//            @Query("sortBy") String sortBy
//    );
}
