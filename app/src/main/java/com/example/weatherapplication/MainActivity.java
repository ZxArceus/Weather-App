package com.example.weatherapplication;

import android.os.Bundle;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
TextView locationTemparature,locationName,locdate,typeweather,humidity,visibility,feellike,windspd,windpres,uvval;
SearchView searchLocation;
LottieAnimationView lottie;
ConstraintLayout cons;
String loc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        loc="";
        //at first showing a particular location data because location fetch is not implemented in this project
        String api_link="https://api.weatherapi.com/v1/current.json?key=17514326a8db4658a92181812240511&q="+"uluberia"+"&aqi=no";
        AndroidNetworking.initialize(this);
        AndroidNetworking.get(api_link)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONObject location=response.getJSONObject("location");
                            String loc=location.getString("name");
                            locationName.setText(loc);
                            JSONObject current=response.getJSONObject("current");

                            String temparature=current.getString("temp_c");
                            locdate.setText(location.getString("localtime"));


                            locationTemparature.setText(temparature+"°");
                            feellike.setText(current.getString("feelslike_c"));
                            uvval.setText(current.getString("uv"));
                            windpres.setText(current.getString("pressure_mb"));
                            windspd.setText(current.getString("wind_mph"));
                            humidity.setText(current.getString("humidity"));
                            visibility.setText(current.getString("vis_km"));
                            JSONObject condition=current.getJSONObject("condition");
                            String weathertype=condition.getString("text");
                            typeweather.setText(weathertype);
                            weatherchange(weathertype);
                        }catch (Exception e){
                            String s=e.toString();

                            Toast.makeText(MainActivity.this, ""+s, Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
        searchLocation.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                weatherSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });



    }
    //initialize properties
    void init(){
        locationTemparature=findViewById(R.id.exacttemp);
        locationName=findViewById(R.id.mainloc);
        searchLocation=findViewById(R.id.searchbtn);
        locdate=findViewById(R.id.datetime);
        typeweather=findViewById(R.id.weathertype);
        humidity=findViewById(R.id.humidityid);
        windspd=findViewById(R.id.windspeedid);
        windpres=findViewById(R.id.airpresid);
        visibility=findViewById(R.id.visibilityid);
        uvval=findViewById(R.id.uvid);
        feellike=findViewById(R.id.feelslikeid);
        cons=findViewById(R.id.main);
        lottie=findViewById(R.id.lottieanim);
    }
    //to show your entered location current weather
    void weatherSearch(String s){
        String api_link="https://api.weatherapi.com/v1/current.json?key=17514326a8db4658a92181812240511&q="+s+"&aqi=no";
        AndroidNetworking.initialize(this);
        AndroidNetworking.get(api_link)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONObject location=response.getJSONObject("location");
                            String loc=location.getString("name");
                            locationName.setText(loc);
                            JSONObject current=response.getJSONObject("current");

                            String temparature=current.getString("temp_c");
                            locationTemparature.setText(temparature);
                            locdate.setText(location.getString("localtime"));
                            JSONObject condition=current.getJSONObject("condition");
                            String weathertype=condition.getString("text");
                            typeweather.setText(weathertype);
                            feellike.setText(current.getString("feelslike_c"));
                            uvval.setText(current.getString("uv"));
                            windpres.setText(current.getString("pressure_mb"));
                            windspd.setText(current.getString("wind_mph"));
                            humidity.setText(current.getString("humidity"));
                            visibility.setText(current.getString("vis_km"));

                            weatherchange(weathertype.toLowerCase());
                        }catch (Exception e){
                            Toast.makeText(MainActivity.this, "api error", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
    //to chnage background according weather(Note: lottieanimations are not here proper)
    void weatherchange(String s){

        if(s.contains("sunny")||s.contains("clear")||s.contains("mist")){
            cons.setBackgroundResource(R.drawable.sunny);
            lottie.setAnimation(R.raw.sunnydayanim);
        }
        else if (s.contains("snow")||s.contains("frizz")) {
            cons.setBackgroundResource(R.drawable.snowy);
            lottie.setAnimation(R.raw.anim2);
        }
        else if(s.contains("thunder")||s.contains("heavy")){
            cons.setBackgroundResource(R.drawable.heavyrain);
            lottie.setAnimation(R.raw.rainanimation);

        }else if (s.contains("rain")||s.contains("driz")) {
            cons.setBackgroundResource(R.drawable.rainanim);
            lottie.setAnimation(R.raw.anim2);

        }  else if (s.contains("cloudy")||s.contains("dew")||s.contains("overcast")) {
            cons.setBackgroundResource(R.drawable.cloudy);
            lottie.setAnimation(R.raw.rainanimation);

        }
    }


}