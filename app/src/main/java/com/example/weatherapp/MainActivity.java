package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText etCity;
    TextView tvResult;
    String url ="http://api.openweathermap.org/data/2.5/weather";
    String api = "ad8cfb3e037ca4805d1448a4465b2af5";
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etCity = findViewById(R.id.etCity);
        tvResult = findViewById(R.id.tvResult);
    }
    public void getWeatherDetails(View view) {
        String tempUrl = "";
        String city = etCity.getText().toString().trim();
        if (city.equals("")){
            tvResult.setText("City field cannot be empty");
        }
        else {
            tempUrl = url + "?q=" + city + "&units=imperial&appid=" + api;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String output = "";
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String description = jsonObjectWeather.getString("main");
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp");
                    double feelsLike = jsonObjectMain.getDouble("feels_like");
                    double minTemp = jsonObjectMain.getDouble("temp_min");
                    double maxTemp = jsonObjectMain.getDouble("temp_max");
                    int humidity = jsonObjectMain.getInt("humidity");
                    JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                    double windSpeed = jsonObjectWind.getDouble("speed");
                    String cityName = jsonResponse.getString("name");
                    tvResult.setTextColor(Color.rgb(255,255,255));
                    output += "Current weather of " + cityName + "\n" +
                              "Temp: " + df.format(temp) + "°F  " + description + "\n" +
                              "Feels Like: " + df.format(feelsLike) + "°F\n" +
                              "Min Temp: " +  df.format(minTemp) + "°F\n" +
                              "Max Temp: " +  df.format(maxTemp) + "°F\n" +
                              "Humidity: " + humidity + "%\n" +
                              "Wind Speed: " + windSpeed + "mi/h";
                    tvResult.setText(output);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}