package com.ruiaa.gank.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ruiaa.gank.R;
import com.ruiaa.gank.ui.base.ToolbarActivity;
import com.ruiaa.gank.util.ShareUtil;
import com.ruiaa.gank.util.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoViewAttacher;

import static com.ruiaa.gank.util.ResUtil.getString;

public class PictureActivity extends ToolbarActivity {

    private static final String IMAGE_URL = "img_url";
    private static final String IMAGE_DESC = "img_desc";

    @BindView(R.id.activity_picture_image)
    ImageView imageView;
    @BindView(R.id.activity_picture)
    RelativeLayout rootLayout;
    private PhotoViewAttacher photoViewAttacher;

    private String imgUrl;
    private String imgDesc;

    private Uri imgSaveUri = null;

    public static Intent newIntent(Context context, String imgUrl, String desc) {
        Intent intent = new Intent(context, PictureActivity.class);
        intent.putExtra(IMAGE_URL, imgUrl);
        intent.putExtra(IMAGE_DESC, desc);
        return intent;
    }

    private void parseIntent() {
        imgUrl = getIntent().getStringExtra(IMAGE_URL);
        imgDesc = getIntent().getStringExtra(IMAGE_DESC);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent();
        setContentView(R.layout.activity_picture);
        ButterKnife.bind(this);
        parseIntent();
        setTitle(imgDesc);
        Glide.with(this)
                .load(imgUrl)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        imageView.setImageDrawable(resource);
                        initPhotoAttacher();
                    }
                });
    }


    private void initPhotoAttacher() {
        photoViewAttacher = new PhotoViewAttacher(imageView);
        photoViewAttacher.setOnViewTapListener((view, v, v1) -> hideOrShowToolbar());
    }

    private void savePicture() {
        if (imgSaveUri == null) {
            Subscription s = saveImageAndGetPathObservable(this, imgUrl, imgDesc)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(uri -> {
                        imgSaveUri = uri;
                        File appDir = new File(Environment.getExternalStorageDirectory(), "Meizhi");
                        String msg = String.format(getString(R.string.tip_picture_has_save_to),
                                appDir.getAbsolutePath());
                        ToastUtil.showShort(msg);
                    }, error -> ToastUtil.showLong(error.getMessage() + "\n再试试..."));
            addSubscription(s);
        }else{
            File appDir = new File(Environment.getExternalStorageDirectory(), "Meizhi");
            String msg = String.format(getString(R.string.tip_picture_has_save_to),
                    appDir.getAbsolutePath());
            ToastUtil.showShort(msg);
        }
    }

    private void sharePicture() {
        if (imgSaveUri == null) {
            Subscription s = saveImageAndGetPathObservable(this, imgUrl, imgDesc)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(uri -> {
                        imgSaveUri = uri;
                        ShareUtil.shareImage(this, getString(R.string.share_meizhi_to), uri);
                    }, error -> ToastUtil.showLong(error.getMessage()));
            addSubscription(s);
        } else {
            ShareUtil.shareImage(this, getString(R.string.share_meizhi_to), imgSaveUri);
        }
    }

    public static Observable<Uri> saveImageAndGetPathObservable(Context context, String url, String title) {
        return Observable
                .create(new Observable.OnSubscribe<Bitmap>() {
                    @Override
                    public void call(final Subscriber<? super Bitmap> subscriber) {
                        Glide.with(context).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                if (resource == null) {
                                    subscriber.onError(new Exception("无法下载到图片"));
                                }
                                subscriber.onNext(resource);
                                subscriber.onCompleted();
                            }
                        });
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .flatMap(bitmap -> {
                    File appDir = new File(Environment.getExternalStorageDirectory(), "Meizhi");
                    if (!appDir.exists()) {
                        appDir.mkdir();
                    }
                    String fileName = title.replace('/', '-') + ".jpg";
                    File file = new File(appDir, fileName);
                    try {
                        FileOutputStream outputStream = new FileOutputStream(file);
                        assert bitmap != null;
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Uri uri = Uri.fromFile(file);
                    // 通知图库更新
                    Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                    context.sendBroadcast(scannerIntent);

                    return Observable.just(uri);
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_picture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_picture_save: {
                savePicture();
                return true;
            }
            case R.id.menu_picture_share: {
                sharePicture();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean canTurnBack() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        photoViewAttacher.cleanup();
    }
}
