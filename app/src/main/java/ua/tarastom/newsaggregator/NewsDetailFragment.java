package ua.tarastom.newsaggregator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.UUID;

import ua.tarastom.newsaggregator.models.Article;
import ua.tarastom.newsaggregator.models.ArticleLab;
import ua.tarastom.newsaggregator.utils.Utils;

public class NewsDetailFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {
    private ImageView imageView;
    private TextView appBarTitle, appbar_subtitle, date, time, title;
    private boolean isHideToolbarView = false;
    private FrameLayout date_behavior;
    private LinearLayout titleAppbar;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private Article article;

    private static final String ARG_ARTICLE_ID = "articleId";

    public NewsDetailFragment() {
        // Required empty public constructor
    }

    public static NewsDetailFragment newInstance(UUID articleId) {
        NewsDetailFragment fragment = new NewsDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ARTICLE_ID, articleId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            UUID articleId = (UUID) getArguments().getSerializable(ARG_ARTICLE_ID);
            article = ArticleLab.get(getActivity()).getArticle(articleId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_detail, container, false);
        toolbar = view.findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");
        appBarLayout = view.findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(this);
        date_behavior = view.findViewById(R.id.date_behavior);
        titleAppbar = view.findViewById(R.id.title_appbar);
        imageView = view.findViewById(R.id.backdrop);
        appBarTitle = view.findViewById(R.id.title_on_appbar);
        appbar_subtitle = view.findViewById(R.id.subtitle_on_appbar);
        date = view.findViewById(R.id.date);
        time = view.findViewById(R.id.time);
        title = view.findViewById(R.id.title);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(Utils.getRandomDrawbleColor());

        Glide.with(this)
                .load(article.getEnclosure())
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);

        appBarTitle.setText(article.getSource());
        appbar_subtitle.setText(article.getLink());
        date.setText(Utils.DateFormat(article.getPubDate()));
        title.setText(article.getTitle());
        String timeText = article.getSource() + " " + " \u2022 " + article.getTitleChannel() + " \u2022 " + Utils.DateToTimeFormat(article.getPubDate());
        time.setText(timeText);
        initWebView(view, article.getLink());
        return view;
    }

    private void initWebView(View view, String url) {
        WebView webView = view.findViewById(R.id.webView);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
            }
        });
        webView.loadUrl(url);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse(url));
//        startActivity(intent);
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
        if (percentage == 1f && isHideToolbarView) {
            date_behavior.setVisibility(View.GONE);
            titleAppbar.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;
        } else if (percentage < 1f && isHideToolbarView) {
            date_behavior.setVisibility(View.VISIBLE);
            titleAppbar.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_news, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.view_web) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(article.getLink()));
            startActivity(intent);
            return true;
        } else if (itemId == R.id.share) {
            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, article.getSource());
                String body = article.getTitle() + "\n" + article.getLink() + "\n" + "Share from the News Aggregator" + "\n";
                intent.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(intent, "Share with: "));
            } catch (Exception e) {
//                Toast.makeText(this, "Sorry, \ncannot be share...", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}