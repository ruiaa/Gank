package com.ruiaa.gank.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruiaa.gank.BR;
import com.ruiaa.gank.R;
import com.ruiaa.gank.databinding.ItemGankBinding;
import com.ruiaa.gank.databinding.ItemMeiziBinding;
import com.ruiaa.gank.model.Category;
import com.ruiaa.gank.model.Gank;
import com.ruiaa.gank.ui.adapter.SimpleRecyclerAdapter;
import com.ruiaa.gank.ui.base.BaseFragment;
import com.ruiaa.gank.ui.viewmodel.CategoryModel;
import com.ruiaa.gank.ui.widget.MultiSwipeRefreshLayout;
import com.ruiaa.gank.util.DateUtil;
import com.ruiaa.gank.util.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

/**
 * Created by ruiaa on 2016/10/22.
 */

public class CategoryFragment extends BaseFragment {

    public static final String CATEGORY = "category";

    @BindView(R.id.category_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.category_swipe)
    MultiSwipeRefreshLayout swipeRefreshView;

    private MainActivity mainActivity;

    private Category category = Category.MEIZI;
    private CategoryModel categoryModel;

    SimpleRecyclerAdapter<Gank> adapter;

    public CategoryFragment() {
    }

    public static CategoryFragment newInstance(Category category) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        if (category == null) {
            args.putString(CATEGORY, Category.MEIZI.name());
        } else {
            args.putString(CATEGORY, category.name());
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = Category.valueOf(getArguments().getString(CATEGORY, Category.MEIZI.name()));
        }
        categoryModel = new CategoryModel(category);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);

        initRecycler();
        initSwipeRefresh();

        return view;
    }

    private void initRecycler() {
        adapter = new SimpleRecyclerAdapter<>(
                mainActivity,
                category == Category.MEIZI ? R.layout.item_meizi : R.layout.item_gank,
                categoryModel.getGankList(),
                (holder, position, model) -> {
                    holder.getBinding().setVariable(category == Category.MEIZI ? BR.itemMeizi : BR.itemGank, model);
                });
        recyclerView.setAdapter(adapter);

        if (category == Category.MEIZI) {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            setMeiziItemListener();
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
            setGankItemListener();
        }
    }

    private void initSwipeRefresh() {

        swipeRefreshView.setRefreshing(true);
        Subscription sub = categoryModel.loadMore()
                .subscribe(s -> {
                    adapter.notifyDataSetChanged();
                    swipeRefreshView.setRefreshing(false);
                }, throwable -> {
                    LogUtil.e("initSwipeRefresh--", throwable);
                    swipeRefreshView.setRefreshing(false);
                });
        mainActivity.addSubscription(sub);

        swipeRefreshView.setRefreshHandler(() -> {
            Subscription subscription = categoryModel.refresh()
                    .subscribe(s -> {
                        adapter.notifyDataSetChanged();
                        swipeRefreshView.setRefreshing(false);
                    }, throwable -> {
                        swipeRefreshView.setRefreshing(false);
                        LogUtil.e("initSwipeRefresh--", throwable);
                    });
            mainActivity.addSubscription(subscription);
        });

        swipeRefreshView.setLoadMoreHandler(recyclerView, () -> {
            swipeRefreshView.setLoadingMore(true);
            Subscription subscription = categoryModel.loadMore()
                    .subscribe(s -> {
                        adapter.notifyDataSetChanged();
                        swipeRefreshView.setLoadingMore(false);
                    }, throwable -> {
                        swipeRefreshView.setLoadingMore(false);
                        LogUtil.e("initSwipeRefresh--", throwable);
                    });
            mainActivity.addSubscription(subscription);
        });
    }

    private void setMeiziItemListener() {
        adapter.setItemListenerBinding((holder, position, model) -> {
                    ItemMeiziBinding item = (ItemMeiziBinding) holder.getBinding();
                    item.itemMeiziImg.setOnClickListener(v -> {
                        startActivity(
                                PictureActivity.newIntent(mainActivity, model.url,
                                        DateUtil.toDateStringUse_(model.publishedAt))
                        );
                    });
                    item.itemMeiziText.setOnClickListener(v -> {
                        startActivity(GankActivity.newIntent(mainActivity, model.publishedAt));
                    });
                }
        );
    }

    private void setGankItemListener() {
        adapter.setItemListenerBinding((holder, position, model) -> {
                    ItemGankBinding item = (ItemGankBinding) holder.getBinding();
                    item.itemGankText.setOnClickListener(v -> {
                        startActivity(WebActivity.newIntent(mainActivity, model.url));
                    });
                }
        );
        adapter.setItemType((model, position) -> {
            if (model.label == null) {
                return 2;
            } else {
                return 3;
            }
        });
    }
}
