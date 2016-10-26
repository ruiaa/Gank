package com.ruiaa.gank.util;

import android.content.Context;
import android.support.annotation.StyleRes;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.TextAppearanceSpan;

import com.ruiaa.gank.R;

/**
 * Created by ruiaa on 2016/10/21.
 */

public class StringStyles {

    private static Context context;

    public static void register(Context appContext) {
        context = appContext.getApplicationContext();
    }

    public static SpannableString format(String text,@StyleRes int style) {
        if (text==null){
            return null;
        }
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new TextAppearanceSpan(context,style), 0, text.length(),0);
        return spannableString;
    }

    public static CharSequence noteFormat(String text,String note) {
        SpannableStringBuilder builder=new SpannableStringBuilder(text).append(format(note,R.style.NoteTextStyle));
        return builder.subSequence(0,builder.length());
    }

}
