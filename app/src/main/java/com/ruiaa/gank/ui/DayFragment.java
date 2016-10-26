package com.ruiaa.gank.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruiaa.gank.BR;
import com.ruiaa.gank.R;
import com.ruiaa.gank.databinding.ItemGankBinding;
import com.ruiaa.gank.model.Gank;
import com.ruiaa.gank.ui.adapter.SimpleRecyclerAdapter;
import com.ruiaa.gank.ui.base.BaseFragment;
import com.ruiaa.gank.ui.viewmodel.DayModel;
import com.ruiaa.gank.util.DateUtil;
import com.ruiaa.gank.util.LogUtil;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by ruiaa on 2016/10/22.
 */

public class DayFragment extends BaseFragment {

    private static final String ARG_DATE = "arg_date";

    @BindView(R.id.day_recycler)
    RecyclerView recyclerView;

    private Date date;
    private GankActivity gankActivity;
    private DayModel dayModel;
    private SimpleRecyclerAdapter<Gank> adapter;

    public DayFragment() {
    }

    public static DayFragment newInstance(Date date) {
        DayFragment fragment = new DayFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_DATE, date.getTime());
        fragment.setArguments(args);
        return fragment;
    }

    private void parseArguments() {
        Bundle bundle = getArguments();
        date = new Date(bundle.getLong(ARG_DATE));
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseArguments();
        gankActivity = (GankActivity) getActivity();
        gankActivity.setTitle(DateUtil.toDateStringUse_(date));
        dayModel = new DayModel(date);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        initRecycler();
        setRecyclerItemListener();

        Subscription subscription = dayModel.loadGank()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    adapter.notifyDataSetChanged();
                },throwable -> LogUtil.e("init--",throwable));
        gankActivity.addSubscription(subscription);
    }

    private void initRecycler() {
        adapter = new SimpleRecyclerAdapter<>(
                gankActivity,
                R.layout.item_gank,
                dayModel.getGankList(),
                (holder, position, model) -> {
                    holder.getBinding().setVariable(BR.itemGank, model);
                });
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(gankActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setRecyclerItemListener() {
        adapter.setItemListenerBinding((holder, position, model) -> {
                    ItemGankBinding item = (ItemGankBinding) holder.getBinding();
                    item.itemGankText.setOnClickListener(v -> {
                        startActivity(WebActivity.newIntent(gankActivity, model.url));
                    });
                }
        );
    }

    @OnClick({R.id.day_video,R.id.day_video_layout})
    public void openVideo(){
        if (dayModel.getVideoUrl()!=null) {
            startActivity(VideoActivity.newIntent(gankActivity, dayModel.getVideoUrl()));
        }
    }
}
