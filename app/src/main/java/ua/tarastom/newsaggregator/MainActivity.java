package ua.tarastom.newsaggregator;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import ua.tarastom.newsaggregator.models.Article;
import ua.tarastom.newsaggregator.utils.parsers.ParserRSS5Channel;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static final String API_KEY = "ecfec3848d274309b22c5b9dac6a46df";
    private RecyclerView recyclerView;
    private List<Article> articles = new ArrayList<>();
    private ArticleAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView topHeadLines;
    private ConstraintLayout errorLayout;
    private ImageView errorImage;
    private TextView errorTitle, errorMessage;
    private Button buttonRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.purple_500);
        topHeadLines = findViewById(R.id.topHeadLines);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        onLoadingSwipeRefresh("");
        errorLayout = findViewById(R.id.error_layout);
        errorImage = findViewById(R.id.imageViewError);
        errorTitle = findViewById(R.id.error_title);
        errorMessage = findViewById(R.id.error_message);
        buttonRetry = findViewById(R.id.buttonRetry);
    }

//    public void loadJson(final String keyWord) {
//        errorLayout.setVisibility(View.GONE);
//        topHeadLines.setVisibility(View.INVISIBLE);
//        swipeRefreshLayout.setRefreshing(true);
//        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
//        String country = Utils.getCountry();
//        String language = Utils.getLanguage();
//        Call<News> call;
//        if (keyWord.length() > 0) {
//            call = apiInterface.getNewsSearch(keyWord, language, "publishedAt", API_KEY);
//        } else {
//            call = apiInterface.getNews(country, API_KEY);
//        }
//        call.enqueue(new Callback<News>() {
//            @Override
//            public void onResponse(Call<News> call, Response<News> response) {
//                if (response.isSuccessful() && response.body().getArticles() != null) {
//                    if (!articles.isEmpty()) {
//                        articles.clear();
//                    }
//                    articles = response.body().getArticles();
//                    adapter = new ArticleAdapter(MainActivity.this);
//                    adapter.setArticles(articles);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
//                    initListener();
//                    recyclerView.setAdapter(adapter);
//                    topHeadLines.setVisibility(View.VISIBLE);
//                    swipeRefreshLayout.setRefreshing(false);
//                } else {
//                    topHeadLines.setVisibility(View.INVISIBLE);
//                    swipeRefreshLayout.setRefreshing(false);
//                    String errorCode;
//                    switch (response.code()) {
//                        case 404:
//                            errorCode = "404 not found";
//                            break;
//                        case 500:
//                            errorCode = "500 server broken";
//                            break;
//                        default:
//                            errorCode = "unknown error";
//                            break;
//                    }
//                    showErrorMessage(R.drawable.no_result, "No result", "Please try again\n" + errorCode);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<News> call, Throwable t) {
//                topHeadLines.setVisibility(View.INVISIBLE);
//                swipeRefreshLayout.setRefreshing(false);
//                showErrorMessage(R.drawable.no_result, "Oops...", "Network failure, please try again\n" + t.toString());
//
//            }
//        });
//    }

    public void loadJsonRSS(final String keyWord) {
        errorLayout.setVisibility(View.GONE);
        topHeadLines.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(true);

        articles = loadRSS();
        if (!articles.isEmpty()) {
            adapter = new ArticleAdapter(MainActivity.this);
            adapter.setArticles(articles);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
            initListener();
            recyclerView.setAdapter(adapter);
            topHeadLines.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        } else {
            topHeadLines.setVisibility(View.INVISIBLE);
            swipeRefreshLayout.setRefreshing(false);
            showErrorMessage(R.drawable.no_result, "No result, unknown error", "Please try again\n");
        }
    }

    private void initListener() {
        adapter.setOnItemClickListener((view, position) -> {
            ImageView imageView = view.findViewById(R.id.img_news);
            Intent intent = new Intent(MainActivity.this, NewsDetailActivity.class);
            Article article = articles.get(position);
            intent.putExtra("url", article.getUrl());
            intent.putExtra("title", article.getTitle());
            intent.putExtra("img", article.getUrlToImage());
            intent.putExtra("source", article.getSource());
            intent.putExtra("date", article.getPublishedAt());
            intent.putExtra("author", article.getAuthor());

            Pair<View, String> pair = Pair.create(imageView, ViewCompat.getTransitionName(imageView));
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, pair);
            startActivity(intent, activityOptionsCompat.toBundle());
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search latest News...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 2) {
                    onLoadingSwipeRefresh(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                loadJson(newText);
                return false;
            }
        });
        searchMenuItem.getIcon().setVisible(false, false);
        return true;
    }

    @Override
    public void onRefresh() {
        loadJsonRSS("");
    }

    private void onLoadingSwipeRefresh(final String keyword) {
        swipeRefreshLayout.post(() -> loadJsonRSS(keyword));
    }

    private void showErrorMessage(int imageView, String title, String message) {
        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
        }
        errorImage.setImageResource(imageView);
        errorTitle.setText(title);
        errorMessage.setText(message);
        buttonRetry.setOnClickListener(view -> onLoadingSwipeRefresh(""));
    }

    private List<Article> loadRSS() {
//        String url = "https://censor.net/includes/resonance_uk.xml";
//        String url = "https://www.radiosvoboda.org/api/zii$p_ejg$py";
//        String url = "http://k.img.com.ua/rss/ua/all_news2.0.xml";
        String url = "https://www.5.ua/novyny/rss";
        ParserRSS5Channel parserRSS = new ParserRSS5Channel(url);
        Thread thread = new Thread(parserRSS);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return parserRSS.getArticles();
    }
}