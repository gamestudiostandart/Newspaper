package newspaper.gamestudiostandart.newspaper.activitys.main;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.arellomobile.mvp.presenter.InjectPresenter;


import java.util.ArrayList;
import java.util.Objects;

import newspaper.gamestudiostandart.newspaper.R;
import newspaper.gamestudiostandart.newspaper.activitys.BaseActivity;
import newspaper.gamestudiostandart.newspaper.activitys.search.SearchActivity;
import newspaper.gamestudiostandart.newspaper.fragments.NewsListFragment;
import newspaper.gamestudiostandart.newspaper.fragments.NewsViewPager;
import newspaper.gamestudiostandart.newspaper.model.FragmentNewsModel;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, MainActivityView {

    @InjectPresenter
    MainActivityPresenter presenter;

    private int idSelectedItem;

    private DrawerLayout drawer;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private Animation animation;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        AppBarLayout app_bar = (AppBarLayout) findViewById(R.id.app_bar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toolbar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.colorIcons), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setColorFilter(getResources().getColor(R.color.colorWhite));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activity = new Intent(MainActivity.this, SearchActivity.class);
                activity.putExtra(SearchActivity.ENUM_RESOURSES, (Parcelable) presenter.getCategory());
                startActivityForResult(activity, SearchActivity.REQWEST_CODE_RESULT);
            }
        });

        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset < 0) {
                    if (fab.getVisibility() == View.VISIBLE) {
                        animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab_animation_hide);
                        fab.startAnimation(animation);
                        fab.setVisibility(View.GONE);
                    }
                } else {
                    if (fab.getVisibility() != View.VISIBLE) {
                        animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab_animation_show);
                        fab.startAnimation(animation);
                        fab.setVisibility(View.VISIBLE);
                    }
                }
                toolbar.setAlpha(1.5f - ((float) Math.abs(verticalOffset) / ((float) appBarLayout.getTotalScrollRange() / 3)));
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        if (presenter.getChackList() != null) {
            setPages(presenter.getChackList());
        } else {
            navigationView.getMenu().getItem(0).setChecked(true);
            presenter.getListRresourses();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        int id = item.getItemId();
        if (id != idSelectedItem) {
            idSelectedItem = id;
            presenter.getListRresourses(updateMenuByNavigationId(id));
        } else {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void setList(ArrayList<FragmentNewsModel> list) {
        viewPager.setVisibility(View.VISIBLE);
        setPages(list);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchActivity.REQWEST_CODE_RESULT) {
            if (resultCode == RESULT_OK) {
                presenter.getListRresourses();
            }
        }
    }

    public void setPages(ArrayList<FragmentNewsModel> list) {
        toolbar.setTitle(String.valueOf(presenter.getCategory()));
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorIcons));
        if (list.size() != 0) {
            ArrayList<Fragment> myListFragments = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                myListFragments.add(NewsListFragment.newInstance(String.valueOf(list.get(i).getUrl())));
            }
            NewsViewPager newsViewPager = new NewsViewPager(getSupportFragmentManager(), myListFragments);
            viewPager.setAdapter(newsViewPager);
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setOffscreenPageLimit(list.size() - 1);
            for (int i = 0; i < list.size(); i++) {
                Objects.requireNonNull(tabLayout.getTabAt(i)).setText(list.get(i).getName());
            }
        }
    }
}