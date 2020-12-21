package ua.tarastom.newsaggregator;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.tarastom.newsaggregator.api.ApiClient;
import ua.tarastom.newsaggregator.api.ApiInterface;
import ua.tarastom.newsaggregator.models.Article;
import ua.tarastom.newsaggregator.models.ArticleLab;
import ua.tarastom.newsaggregator.models.News;

public class NewsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private List<Article> articles = new ArrayList<>();
    private ArticleAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView topHeadLines;
    private ConstraintLayout errorLayout;
    private ImageView errorImage;
    private TextView errorTitle, errorMessage;
    private Button buttonRetry;
    private String keyWord = "";
    private String country = "ua";
    private String category = "";
    private String language = "";
    private String titleChannel = "";
    private int page = 0;
    private int size = 20;
    private boolean isLoading = false;

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Callbacks start
    private Callbacks callbacks;



    interface Callbacks{
        void onArticleSelected(Article article);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }
    //Callbacks end
    //Теперь  у  NewsListFragment  имеется  механизм  вызова  методов  активности-хоста.
    // Неважно,  какая  активность  является  хостом,  —  если  она  реализует NewsListFragment.Callbacks,
    // внутренняя  реализация  NewsListFragment  будет работать одинаково. Она выполняет непроверяемое пре-
    //образование своей активности к NewsListFragment.Callbacks. Это означает, что активность-хост
    // должна реализовать NewsListFragment.Callbacks. В самой зависимости нет ничего плохого, но ее важно документировать.

    public NewsListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NewsListFragment newInstance(String param1, String param2) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.purple_500);
        topHeadLines = view.findViewById(R.id.topHeadLines);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        onLoadingSwipeRefresh(keyWord);
        errorLayout = view.findViewById(R.id.error_layout);
        errorImage = view.findViewById(R.id.imageViewError);
        errorTitle = view.findViewById(R.id.error_title);
        errorMessage = view.findViewById(R.id.error_message);
        buttonRetry = view.findViewById(R.id.buttonRetry);

        adapter = new ArticleAdapter(getContext());
        adapterInitListener();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));


        return view;
    }

    public void loadRssByRetrofit(final String keyWord) {
        errorLayout.setVisibility(View.GONE);
        topHeadLines.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(true);
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<News> call = apiInterface.getFeed(keyWord, country, category, language, titleChannel, page, size);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful()) {
                    if (!articles.isEmpty()) {
                        articles.clear();
                    }
                    if (response.body() != null) {
                        articles = response.body().getEmbedded().getArticles();
                    } else {
                        articles = new ArrayList<>();
                    }
                    ArticleLab.setArticleList(articles);
                    adapter.setArticles(articles);
                    recyclerView.setAdapter(adapter);
                    topHeadLines.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    page ++;
                    isLoading = true;
                } else {
                    topHeadLines.setVisibility(View.INVISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    String errorCode;
                    switch (response.code()) {
                        case 404:
                            errorCode = "404 not found";
                            break;
                        case 500:
                            errorCode = "500 server broken";
                            break;
                        default:
                            errorCode = "unknown error";
                            break;
                    }
                    showErrorMessage("No result", "Please try again\n" + errorCode);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                topHeadLines.setVisibility(View.INVISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                showErrorMessage("Oops...", "Network failure, please try again\n" + t.toString());
            }
        });
    }

    private void adapterInitListener() {
        adapter.setOnItemClickListener((view, position) -> {
            Article article = articles.get(position);
            callbacks.onArticleSelected(article);
        });
        adapter.setOnReachEndListener(() -> {
            if (!isLoading) {
                loadRssByRetrofit(keyWord);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        searchMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setQueryHint("Search latest news...");
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
                // Here is where we are going to implement the filter logic -  loadJson(newText);
                return true;
            }
        });
        searchMenuItem.getIcon().setVisible(true, false);
    }

    @Override
    public void onRefresh() {
        loadRssByRetrofit(keyWord);
    }

    private void onLoadingSwipeRefresh(final String keyword) {
        swipeRefreshLayout.post(() -> loadRssByRetrofit(keyword));
    }

    private void showErrorMessage(String title, String message) {
        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
        }
        errorImage.setImageResource(R.drawable.no_result);
        errorTitle.setText(title);
        errorMessage.setText(message);
        buttonRetry.setOnClickListener(view -> onLoadingSwipeRefresh(""));
    }
}