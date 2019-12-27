package com.example.weatherapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {


    Button btn;
    TextView tv;
    EditText et;
    String access_key = "7053bcab08266f2416c00442e68c7985";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.button);
        tv = findViewById(R.id.textView);
        et = findViewById(R.id.city);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et.getText().toString().isEmpty())
                    Toast.makeText(
                            getApplication().getBaseContext(),
                            "whoops... Enter city, please!",
                            Toast.LENGTH_LONG).show();
                Download dl = new Download();
                dl.execute(et.getText().toString());
                }
        });
    }

    private class Download extends AsyncTask<String, Void, String> {


        HttpURLConnection hurl; //null


        @Override
        protected String doInBackground(String... cities) {
            String city = cities[0];
            try {
                URL url = new URL(
                        "http://api.weatherstack.com/current?" +
                                "access_key=" + access_key +
                                "&query=" + city);
                hurl = (HttpURLConnection) url.openConnection();
                hurl.setRequestMethod("GET");
                hurl.connect();

                InputStream input = hurl.getInputStream();

                Scanner sc = new Scanner(input);

                StringBuilder buff = new StringBuilder();
                while(sc.hasNextLine()) {
                    buff.append(sc.nextLine());
                }
                return buff.toString();

            } catch (IOException e) {
                Log.e("RRREEE", e.toString());
                Toast.makeText(getBaseContext(),
                        "Error! See JSON...",
                        Toast.LENGTH_LONG).show();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(s);
            String prettyJsonString = gson.toJson(je);
            tv.setText(prettyJsonString);
        }
    }
}
