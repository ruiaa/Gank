package com.ruiaa.gank.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by ruiaa on 2016/10/21.
 */

public class Util {
    public static void copyToClipBoard(Context context, String text, String success) {
        ClipData clipData = ClipData.newPlainText("gank_copy", text);
        ClipboardManager manager = (ClipboardManager) context.getSystemService(
                Context.CLIPBOARD_SERVICE);
        manager.setPrimaryClip(clipData);
        Toast.makeText(context, success, Toast.LENGTH_SHORT).show();
    }
}
