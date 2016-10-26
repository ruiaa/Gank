package com.ruiaa.gank.ui.adapter;

import android.content.Context;

import java.util.List;

/**
 * Created by ruiaa on 2016/10/3.
 */

public class SimpleRecyclerAdapter<T> extends BaseRecyclerAdapter{

    private Context context;
    private List<T> list;
    private int itemLayoutId;
    private ItemDataBinding<T> itemDataBinding;
    private ItemListenerBinding<T> itemListenerBinding;
    private ItemType<T> itemType;

    public SimpleRecyclerAdapter() {
    }

    public SimpleRecyclerAdapter(Context context,int itemLayoutId, List<T> list, ItemDataBinding<T> itemDataBinding) {
        this.context=context;
        this.itemLayoutId = itemLayoutId;
        this.list = list;
        this.itemDataBinding = itemDataBinding;
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        final T model = list.get(position);

        if (itemDataBinding!=null){
            itemDataBinding.bindData(holder,position,model);
        }
        if (itemListenerBinding!=null){
            itemListenerBinding.bindListener(holder,position,model);
        }

        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (itemType!=null&&list!=null&&list.size()>position){
            return itemType.getType(list.get(position),position);
        }else {
            return super.getItemViewType(position);
        }
    }

    @Override
    protected int getItemLayoutId() {
        return itemLayoutId;
    }

    @Override
    protected Context getContext() {
        return context;
    }


    /*
     *
     *初始化
     *
     */
    public SimpleRecyclerAdapter setContext(Context context) {
        this.context=context;
        return this;
    }

    public SimpleRecyclerAdapter setItemLayoutId(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
        return this;
    }

    public SimpleRecyclerAdapter setList(List<T> list) {
        this.list = list;
        return this;
    }

    public SimpleRecyclerAdapter setItemDataBinding(ItemDataBinding<T> itemDataBinding) {
        this.itemDataBinding = itemDataBinding;
        return this;
    }

    public SimpleRecyclerAdapter setItemListenerBinding(ItemListenerBinding<T> itemListenerBinding) {
        this.itemListenerBinding = itemListenerBinding;
        return this;
    }

    public SimpleRecyclerAdapter setItemType(ItemType<T> itemType) {
        this.itemType = itemType;
        return this;
    }

    /*
     *
     * 修改数据(源)
     *
     */
    public List<T> getDataList(){
        return list;
    }

    public void changeDataSource(List<T> newList){
        list=newList;
        this.notifyDataSetChanged();
    }

    public void addItem(T model,int position){
        list.add(position,model);
        this.notifyItemInserted(position);
    }


    /*
     *
     * 接口
     *
     */
    public interface ItemDataBinding<T>{
        void bindData(BindingHolder holder, int position, T model);
    }

    public interface ItemListenerBinding<T>{
        void bindListener(BindingHolder holder, int position, T model);
    }

    public interface ItemType<T>{
        int getType(T model,int position);
    }
}
