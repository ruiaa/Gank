package com.ruiaa.gank.ui;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.ruiaa.gank.R;
import com.ruiaa.gank.ui.base.ToolbarActivity;

import java.util.Date;

public class GankActivity extends ToolbarActivity {

    private static final String OPEN_DATE="open_date";

    private Date openDate=null;
    private FragmentManager fragmentManager;
    private DayFragment currentFragment;

    public static Intent newIntent(Context context,Date date){
        Intent intent=new Intent(context,GankActivity.class);
        intent.putExtra(OPEN_DATE,date);
        return intent;
    }

    private void parseIntent() {
        openDate=(Date)getIntent().getSerializableExtra(OPEN_DATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gank);
        parseIntent();
        fragmentManager=getFragmentManager();
        if (savedInstanceState==null){
            currentFragment=DayFragment.newInstance(openDate);
            FragmentTransaction transaction=fragmentManager.beginTransaction();
            transaction.add(R.id.activity_gank_frame,currentFragment);
            transaction.commit();
        }
    }

    @Override
    protected boolean canTurnBack() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_picture,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

        }
        return super.onOptionsItemSelected(item);
    }
}
