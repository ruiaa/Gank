package com.ruiaa.gank.ui.widget;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by ruiaa on 2016/10/18.
 */

public class ImgViewGlide extends ImageView {
    public ImgViewGlide(Context context) {
        super(context);
    }

    public ImgViewGlide(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImgViewGlide(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width=MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width,width);
    }

    @BindingAdapter("imageUrl")
    public static void setImgUrl(ImageView imageView,String url){
        Glide.with(imageView.getContext())
                .load(url)
                .crossFade()
                .centerCrop()
                .into(imageView);
    }
}
