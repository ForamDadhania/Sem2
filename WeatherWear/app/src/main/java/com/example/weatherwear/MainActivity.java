package com.example.weatherwear;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends WearableActivity {

    EditText editText;
    Button button,convert;
    TextView result,description1,description2;
    ImageView img;
    String image = "";


class Weather extends AsyncTask<String, Void, String>{@Override
protected String doInBackground(String... address) {
    try {
        URL url = new URL(address[0]);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        Log.v("connection","con");

        InputStream is = connection.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);

        int data = isr.read();
        String content = "";
        char ch;
        while(data != -1){
            ch = (char) data;

            content = content + ch;
            data = isr.read();

        }
        return content;
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }


    return null;
}}
           public void search(View view){


                String cName = editText.getText().toString();

                String content;

                Weather weather = new Weather();
                try {
                    content = weather.execute("https://openweathermap.org/data/2.5/weather?q=" + cName + "&appid=b6907d289e10d714a6e88b30761fae22").get();
                    JSONObject jo = new JSONObject(content);
                    String weatherData = jo.getString("weather");
                    String mainArray = jo.getString("main");
                    Log.i("weatherData",weatherData);

                    JSONArray array = new JSONArray(weatherData);



                    String main = "";
                    String description = "";
                    String temperature = "";


                    for (int i=0; i<array.length(); i++){
                        JSONObject weatherPart = array.getJSONObject(i);
                        main = weatherPart.getString("main");
                        description = weatherPart.getString("description");
                        image = weatherPart.getString("icon");
                    }

                    JSONObject mainJason = new JSONObject(mainArray);
                    temperature = mainJason.getString("temp");



                    Log.i("Foram main", main);
                    Log.i("Foram description",description);

                        result.setText("Weather: " + main);
                        description1.setText("Description: " + description);
//                        description2.setText("Temperature: " + temperature);



                        final float tempNumber = Float.parseFloat(temperature);
                        float fahrenheit = (tempNumber * 9/5) + 32;

                        String back = Float.toString(fahrenheit);
                        description2.setText("Temperature: " + back + "f");

                        convert.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                description2.setText("Temperature: " + tempNumber + "c");
                            }
                        });
                        updateImage();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }



            public void updateImage(){
                Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try  {
                                try {
                                    String iconURL = "http://openweathermap.org/img/w/" + image + ".png";
                                    URL url = new URL(iconURL);
                                    try {
                                        Bitmap btmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                        img.setImageBitmap(btmp);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    thread.start();
            }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Enables Always-on
        setAmbientEnabled();
        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);
        convert = (Button) findViewById(R.id.convert);
        result = (TextView) findViewById(R.id.result);
        description1 = (TextView) findViewById(R.id.description1);
        description2 = (TextView) findViewById(R.id.description2);
        img = (ImageView)findViewById(R.id.img);

    }



    }



