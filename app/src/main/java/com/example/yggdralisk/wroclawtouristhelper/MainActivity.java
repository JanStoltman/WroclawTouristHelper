package com.example.yggdralisk.wroclawtouristhelper;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.activity_main_linear_layut) LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        ButterKnife.bind(this);

        linearLayout = ButterKnife.findById(this,R.id.activity_main_linear_layut);

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);

        int picId;
        if(hour<12) picId = R.drawable.moring_pic;
        else picId = R.drawable.main_pic;


        Glide.with(getApplicationContext()).load(picId).asBitmap().into(new SimpleTarget<Bitmap>(1500, 1500) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    linearLayout.setBackground(drawable);
                }
            }
        });
    }

    //-------------------------------------Buttons methods------------------------------
    public void moveToBikePoints(View view) {
    }

    public void moveToBusStops(View view) {
    }

    public void moveToMap(View view) {
    }

    public void moveToWiFiPoints(View view) {
    }

    public void moveToLibraries(View view) {
    }

    public void moveToInfo(View view) {
    }
}
