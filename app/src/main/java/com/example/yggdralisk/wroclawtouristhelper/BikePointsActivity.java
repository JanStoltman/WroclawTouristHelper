package com.example.yggdralisk.wroclawtouristhelper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.IOException;
import java.io.InputStream;
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
public class BikePointsActivity extends AppCompatActivity
        implements OnMapReadyCallback {
    ArrayList<BikePoint> bikePoints = new ArrayList<>();
    GoogleMap googleMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bike_points_layout);
        getData();
        setMap();
    }

    private void setMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.bike_points_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        googleMap.setIndoorEnabled(false);
        for (BikePoint b :
                bikePoints) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(b.getSzerokoscGeo(), b.getDlugoscGeo()))
                    .title(b.getLokalizacja()));
        }

        Location location = getUserLocation();
        LatLng latLng;
        if(location == null){ latLng = new LatLng(51.06415, 17.03369);}  //If permission is denied set location to Pl.Grunwaldzki TODO:Upgrade location get
        else latLng =  new LatLng(location.getLatitude(), location.getLongitude());

        //Set map to user current location
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                latLng, 13));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



    }

    private Location getUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // Location wasn't found, check the next most accurate place for the current location
        if (myLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            String provider = lm.getBestProvider(criteria, true);
            myLocation = lm.getLastKnownLocation(provider);
        }

        return myLocation;//Return user location
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
                readFile(response.body().byteStream());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("WRITE", "Error while downloading file!");
                Toast.makeText(getApplicationContext(), "Connection error!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void readFile(InputStream inputStream) {
        try {
            HSSFWorkbook myWorkBook = new HSSFWorkbook(inputStream);

            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            // We now need something to iterate through the cells
            Iterator<Row> rowIter = mySheet.rowIterator();
            while (rowIter.hasNext()) {

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
                            bikePoint.setLp(Double.valueOf(cellValue));
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

            addPointsToMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addPointsToMap() {
        if(googleMap != null){
            for (BikePoint b :
                    bikePoints) {
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(b.getSzerokoscGeo(), b.getDlugoscGeo()))
                        .title(b.getLokalizacja()));
            }
        }
    }
}
