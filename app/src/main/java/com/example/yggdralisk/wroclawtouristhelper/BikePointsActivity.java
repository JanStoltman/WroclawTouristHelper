package com.example.yggdralisk.wroclawtouristhelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by yggdralisk on 12.05.16.
 */
public class BikePointsActivity extends AppCompatActivity {
    private static final String FILE_NAME = "BikePoints.xls";
    ArrayList<BikePoint> bikePoints = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

    private void getData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.wroclaw.pl/open-data/")
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<ResponseBody> call = retrofitInterface.getBikePoints();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    File path = Environment.getExternalStorageDirectory();
                    File file = new File(path, FILE_NAME);
                    FileOutputStream outputStream = new FileOutputStream(file);
                    outputStream.write(response.body().string().getBytes());
                    outputStream.close();

                    readFile();
                } catch (IOException e) {
                    Log.e("WRITE", "Error while writing file!");
                    Log.e("WRITE", e.toString());

                    File path = Environment.getExternalStorageDirectory();
                    File file = new File(path, FILE_NAME);
                    if (file.exists()) {
                        readFile();
                    } else {
                        Toast.makeText(getApplicationContext(), "Write error!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("WRITE", "Error while downloading file!");

                File path = Environment.getExternalStorageDirectory();
                File file = new File(path, FILE_NAME);
                if (file.exists()) {
                    readFile();
                } else {
                    Toast.makeText(getApplicationContext(), "Connection error!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void readFile() {
        File path = Environment.getExternalStorageDirectory();
        File file = new File(path, FILE_NAME);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            HSSFWorkbook   myWorkBook = new HSSFWorkbook(fis);

            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            // We now need something to iterate through the cells
            Iterator<Row> rowIter = mySheet.rowIterator();
            while(rowIter.hasNext()) {

                HSSFRow myRow = (HSSFRow) rowIter.next();
                if (myRow.getRowNum() < 1) { //Bypassing first row with column names
                    continue;
                }

                BikePoint bikePoint = new BikePoint();

                Iterator<Cell> cellIter = myRow.cellIterator();
                while (cellIter.hasNext()) {
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    String cellValue = "";

                    if (myCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                        cellValue = myCell.getStringCellValue();
                    } else {
                        cellValue = String.valueOf(myCell.getNumericCellValue());
                    }

                    Log.v("CELLVAL", cellValue);

                    switch (myCell.getColumnIndex()) { // Set Bike point Values
                        case 0:
                            bikePoint.setLp(Integer.valueOf(cellValue));
                            break;
                        case 1:
                            bikePoint.setSystem(cellValue);
                            break;
                        case 2:
                            bikePoint.setLokalizacja(cellValue);
                            break;
                        case 3:
                            bikePoint.setSzerokoscGeo(Double.valueOf(cellValue));
                            break;
                        case 4:
                            bikePoint.setDlugoscGeo(Double.valueOf(cellValue));
                            break;
                        default:
                            break;
                    }
                }
                bikePoints.add(bikePoint);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
