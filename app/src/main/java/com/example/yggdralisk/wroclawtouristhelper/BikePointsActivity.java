package com.example.yggdralisk.wroclawtouristhelper;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by yggdralisk on 12.05.16.
 */
public class BikePointsActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getData();
    }

    private void getData()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.wroclaw.pl/open-data/")
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<ResponseBody> call = retrofitInterface.getBikePoints();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String fileName = "NikePoints.xlsx";

                try {
                    File path = Environment.getExternalStorageDirectory();
                    File file = new File(path, fileName);
                    FileOutputStream outputStream = new FileOutputStream(file);
                    outputStream.write(response.body().string().getBytes());
                    outputStream.close();
                } catch (IOException e) {
                    Log.e("WRITE", "Error while writing file!");
                    Log.e("WRITE", e.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("WRITE", "Error while writing file!");
            }
        });
    }
}
