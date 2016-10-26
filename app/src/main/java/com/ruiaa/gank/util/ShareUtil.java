package com.ruiaa.gank.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by ruiaa on 2016/10/21.
 */

public class ShareUtil {

    public static void shareImage(Context context, String title, Uri uri) {
        Intent intent = new Intent();
        intent.setType("image/jpeg");
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(intent, title));
    }


    public static void shareText(Context context, String subjectText, String extraText) {
        Intent intent = new Intent();
        intent.setType("text/plain");
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, extraText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, subjectText));
    }

}
