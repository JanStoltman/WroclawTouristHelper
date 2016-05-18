package com.example.yggdralisk.wroclawtouristhelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.Calendar;

import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

     LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        linearLayout = ButterKnife.findById(this,R.id.activity_main_linear_layut);

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);

        int picId;
        if(hour<10) picId = R.drawable.moring_pic;
        else if(hour < 19) picId = R.drawable.main_pic;
        else picId = R.drawable.evening_pic;

        Glide.with(getApplicationContext()).load(picId).asBitmap().into(new SimpleTarget<Bitmap>(1500, 1500) {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(getResources(),bitmap);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    linearLayout.setBackground(drawable);
                }
            }
        });
    }

    //-------------------------------------Buttons methods------------------------------
    public void moveToBikePoints(View view) {
        Intent intent = new Intent(this,BikePointsActivity.class);
        startActivity(intent);
    }

    public void moveToBusStops(View view) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://wroclaw.jakdojade.pl/")));
    }

    public void moveToMap(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://gis.um.wroc.pl/imap/?locale=pl&gui=classic&sessionID=223261")));
    }

    public void moveToInfo(View view) {
        Intent intent = new Intent(this,InfoActivity.class);
        startActivity(intent);
    }

    public void moveToEvents(View view) {
        Intent intent = new Intent(this,EventsActivity.class);
        startActivity(intent);
    }
}
