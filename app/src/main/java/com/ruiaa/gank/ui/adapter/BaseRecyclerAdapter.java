package com.ruiaa.gank.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by ruiaa on 2016/10/6.
 */

public abstract class BaseRecyclerAdapter extends RecyclerView.Adapter<BaseRecyclerAdapter.BindingHolder>{

    public static class BindingHolder extends RecyclerView.ViewHolder{
        private final ViewDataBinding binding;
        public BindingHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
        public ViewDataBinding getBinding() {
            return binding;
        }
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ViewDataBinding binding= DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                getItemLayoutId(),
                parent,
                false);
        return new BindingHolder(binding);
    }

    abstract protected int  getItemLayoutId();
    abstract protected Context getContext();
}
