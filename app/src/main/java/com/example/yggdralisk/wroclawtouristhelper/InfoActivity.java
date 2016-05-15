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
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import butterknife.ButterKnife;

/**
 * Created by yggdralisk on 15.05.16.
 */
public class InfoActivity extends AppCompatActivity{

    ImageView logoPic;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity_layout);

        logoPic = ButterKnife.findById(this,R.id.info_logo_iv);
        relativeLayout = ButterKnife.findById(this,R.id.info_relative_layout);

        Glide.with(this).load(R.drawable.logo_otwarte_dane).fitCenter().into(logoPic);

        Glide.with(getApplicationContext()).load(R.drawable.lloyd_ewp).asBitmap().into(new SimpleTarget<Bitmap>(1500, 500) {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(getResources(),bitmap);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    relativeLayout.setBackground(drawable);
                }
            }
        });
    }

    public void openOpenDataSite(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.wroclaw.pl/open-data")));
    }
}
