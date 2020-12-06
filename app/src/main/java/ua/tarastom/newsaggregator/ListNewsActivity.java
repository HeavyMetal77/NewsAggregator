package ua.tarastom.newsaggregator;

import androidx.fragment.app.Fragment;

public class ListNewsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return NewsListFragment.newInstance("", "");
    }
}