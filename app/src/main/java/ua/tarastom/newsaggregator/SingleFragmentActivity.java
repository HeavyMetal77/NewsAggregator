package ua.tarastom.newsaggregator;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
        BottomNavigationView bottomNavigationMenu = findViewById(R.id.bottom_navigation);
        bottomNavigationMenu.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.menu_page_1:
                    selectedFragment = NewsListFragment.newInstance("", "");
                    break;
                case R.id.menu_page_2:
                    selectedFragment = FilterFragment.newInstance("", "");
                    break;
                case R.id.menu_page_3:
                    selectedFragment = SavedPagesFragment.newInstance("", "");
                    break;
                case R.id.menu_page_4:
                    selectedFragment = PresetsFragment.newInstance("", "");
                    break;
            }
            fm.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        });
    }
}
