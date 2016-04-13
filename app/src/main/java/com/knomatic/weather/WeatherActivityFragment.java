package com.knomatic.weather;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.johnhiott.darkskyandroidlib.ForecastApi;
import com.johnhiott.darkskyandroidlib.RequestBuilder;
import com.johnhiott.darkskyandroidlib.models.Request;
import com.johnhiott.darkskyandroidlib.models.WeatherResponse;

import retrofit.client.Response;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * A placeholder fragment containing a simple view.
 */
public class WeatherActivityFragment extends Fragment {

    double longitude;
    double latitude;
    private static String APIKEY = "224d0188ffd203d6f87f59a0fef2ecb0";
    public static GpsTracker gpsTracker;
    private static final String TAG = "Fragment";
    TextView currentTemperature,currentSummary,currentHumidty, currentWindBe, currentWindSp;
    TextView mondayTemperature,thueTemperature, wedTemperature, thusTemperature, friTemperature;
    ErrorHandling errorHandling;

    public WeatherActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_weather, container, false);

        gpsTracker = new GpsTracker(getContext());
        errorHandling = new ErrorHandling(getContext());

        longitude = gpsTracker.getLongitude();
        latitude = gpsTracker.getLatitude();

        currentTemperature =(TextView)rootView.findViewById(R.id.currentTemperatureTextView);
        currentSummary =(TextView)rootView.findViewById(R.id.currentSummaryTextView);
        currentHumidty =(TextView)rootView.findViewById(R.id.currentHumidity);
        currentWindBe =(TextView)rootView.findViewById(R.id.currentWindBearing);
        currentWindSp =(TextView)rootView.findViewById(R.id.currentWindSpeed);


        mondayTemperature =(TextView)rootView.findViewById(R.id.mondayTemperature);
        thueTemperature =(TextView)rootView.findViewById(R.id.thuesTemperature);
        wedTemperature =(TextView)rootView.findViewById(R.id.wednesdayTemperature);
        thusTemperature =(TextView)rootView.findViewById(R.id.thursdayTemperature);
        friTemperature =(TextView)rootView.findViewById(R.id.fridayTemperature);



        ForecastApi.create(APIKEY);
        getWheater();


        return  rootView;
    }

    public void getWheater (){
        RequestBuilder weather = new RequestBuilder();
        Request request = new Request();
        request.setLat(String.valueOf(longitude));
        request.setLng(String.valueOf(latitude));
        request.setUnits(Request.Units.SI);
        request.setLanguage(Request.Language.ENGLISH);
        request.addExcludeBlock(Request.Block.DAILY);
        request.removeExcludeBlock(Request.Block.DAILY);

        weather.getWeather(request, new Callback<WeatherResponse>() {

            @Override
            public void success(WeatherResponse weatherResponse, Response response) {
                Log.d("Success", String.valueOf(response.getReason()));

                currentSummary.setText(weatherResponse.getCurrently().getSummary());
                currentTemperature.setText(String.valueOf(weatherResponse.getCurrently().getTemperature()) + "º");

                currentHumidty.setText(String.valueOf(weatherResponse.getCurrently().getHumidity()));
                currentWindBe.setText(String.valueOf(weatherResponse.getCurrently().getWindBearing()));
                currentWindSp.setText(String.valueOf(weatherResponse.getCurrently().getWindSpeed()));


                mondayTemperature.setText(String.valueOf(weatherResponse.getDaily().getData().get(0).getApparentTemperatureMin()+"º"));
                thueTemperature.setText(String.valueOf(weatherResponse.getDaily().getData().get(1).getApparentTemperatureMin()+"º"));
                wedTemperature.setText(String.valueOf(weatherResponse.getDaily().getData().get(2).getApparentTemperatureMin()+"º"));
                thusTemperature.setText(String.valueOf(weatherResponse.getDaily().getData().get(3).getApparentTemperatureMin()+"º"));
                friTemperature.setText(String.valueOf(weatherResponse.getDaily().getData().get(4).getApparentTemperatureMin()+"º"));

                Log.d(TAG, "temp" + weatherResponse.getCurrently().getTemperature() );

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d("Error while calling: ", retrofitError.getUrl());
                errorHandling.showSettingsAlert();
            }
        });
    }


}
