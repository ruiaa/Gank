package com.ruiaa.gank.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.ruiaa.gank.R;
import com.ruiaa.gank.http.GankRetrofit;
import com.ruiaa.gank.model.Category;
import com.ruiaa.gank.ui.base.ToolbarActivity;
import com.ruiaa.gank.util.LogUtil;
import com.ruiaa.gank.util.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends ToolbarActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.activity_main_nav)
    NavigationView navigationView;
    @BindView(R.id.activity_main_drawer)
    DrawerLayout drawerLayout;
    //    @BindView(R.id.main_header_image)
    ImageView headerImageView;

    private FragmentManager fragmentManager;

    private CategoryFragment currentFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fragmentManager = getFragmentManager();
        if (savedInstanceState == null) {
            if (currentFragment == null) {
                currentFragment = CategoryFragment.newInstance(Category.MEIZI);
            }
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.activity_main_frame, currentFragment);
            transaction.commit();
            setTitle(R.string.category_meizi);
        }
        initDrawer();
    }

    private void initDrawer() {
        setDrawerImage();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            switch (item.getItemId()) {
                case R.id.drawer_meizi: {
                    currentFragment = CategoryFragment.newInstance(Category.MEIZI);
                    setTitle(R.string.category_meizi);
                    break;
                }
                case R.id.drawer_android: {
                    currentFragment = CategoryFragment.newInstance(Category.ANDROID);
                    setTitle(R.string.category_android);
                    break;
                }
                case R.id.drawer_ios: {
                    currentFragment = CategoryFragment.newInstance(Category.IOS);
                    setTitle(R.string.category_ios);
                    break;
                }
                case R.id.drawer_front: {
                    currentFragment = CategoryFragment.newInstance(Category.FRONT);
                    setTitle(R.string.category_front);
                    break;
                }
                case R.id.drawer_expanding: {
                    currentFragment = CategoryFragment.newInstance(Category.EXPAND);
                    setTitle(R.string.category_expanding);
                    break;
                }
                case R.id.drawer_recommend: {
                    currentFragment = CategoryFragment.newInstance(Category.RECOMMEND);
                    setTitle(R.string.category_recommend);
                    break;
                }
                case R.id.drawer_video: {
                    currentFragment = CategoryFragment.newInstance(Category.VIDEO);
                    setTitle(R.string.category_video);
                    break;
                }
                case R.id.drawer_app: {
                    currentFragment = CategoryFragment.newInstance(Category.APP);
                    setTitle(R.string.category_app);
                    break;
                }
                default: {
                    currentFragment = CategoryFragment.newInstance(Category.MEIZI);
                    setTitle(R.string.category_meizi);
                    break;
                }
            }
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.activity_main_frame, currentFragment);
            transaction.commit();
            return true;
        });
    }

    private void setDrawerImage() {
        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_main_header, navigationView, false);
        if(Build.VERSION.SDK_INT>=19){
            headerView.setPadding(headerView.getPaddingLeft(),
                    headerView.getPaddingTop()+ StatusBarUtil.getStatusBarHeight(this),
                    headerView.getPaddingRight(),
                    headerView.getPaddingBottom());
        }
        navigationView.addHeaderView(headerView);
        headerImageView = (ImageView) headerView.findViewById(R.id.main_header_image);
        Glide.with(MainActivity.this)
                .load(R.mipmap.meizi_default)
                .asBitmap()
                .transform(new CropCircleTransformation(this))
                .into(headerImageView);
        GankRetrofit.getGankApi().getFirstMeiziData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(categoryData -> {
                    Glide.with(MainActivity.this)
                            .load(categoryData.results.get(0).url)
                            .asBitmap()
                            .placeholder(R.mipmap.meizi_default)
                            .error(R.mipmap.meizi_default)
                            .transform(new CropCircleTransformation(this))
                            .into(headerImageView);
                }, throwable -> {
                    LogUtil.e("setDrawerImage--", throwable);
                });
    }

    @OnClick(R.id.about)
    public void openAboutView(){
        new MaterialDialog.Builder(this)
                .title(R.string.about)
                .titleColorAttr(R.attr.colorPrimary)
                .content(R.string.about_content)
                .positiveText(R.string.close)
                .show();
    }
    @OnClick(R.id.theme)
    public void openThemeView(){
        new MaterialDialog.Builder(this)
                .title(R.string.theme)
                .titleColorAttr(R.attr.colorPrimary)
                .content("尚未实现")
                .positiveText(R.string.close)
                .show();
    }
}
