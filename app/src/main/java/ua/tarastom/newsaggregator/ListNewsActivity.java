package ua.tarastom.newsaggregator;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import ua.tarastom.newsaggregator.models.Article;

public class ListNewsActivity extends SingleFragmentActivity implements NewsListFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return NewsListFragment.newInstance("", "");
    }

    @Override
    public void onArticleSelected(Article article) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = NewsDetailActivity.newIntent(this, article.getArticleId());
            startActivity(intent);
        } else {
            Fragment newDetail = NewsDetailFragment.newInstance(article.getArticleId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }
}