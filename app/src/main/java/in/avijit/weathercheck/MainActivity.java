package in.avijit.weathercheck;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private TextView weatherResult;
    private Button button;
    private EditText editText;
    private RequestQueue requestQueue;
    private String baseUrl = "https://api.openweathermap.org/data/2.5/weather?q=";
    private String apiKey = "&appid=86542bb9a0d72783caada8994e13edc1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherResult = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        editText = findViewById(R.id.cityname);

        requestQueue = VolleySingleton.getInstance(this).getmRequestQueue();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().equals("")){
                    editText.setError("Please Enter City Name");
                }
                else{
                    String city = editText.getText().toString();
                    String finalUrl = baseUrl + city + apiKey;
//                    Log.i("Url","url : "+finalUrl);

                    getWeather(finalUrl);
                }
            }
        });
    }

    private void getWeather(String url){
        final JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject coord = response.getJSONObject("coord");
                    double lon = coord.getDouble("lon");
                    double lat = coord.getDouble("lat");
                    weatherResult.setText("Lon : "+String.valueOf(lon)+" Lat : "+ String.valueOf(lat)+"\n");
                    JSONArray weather = response.getJSONArray("weather");
                    JSONObject wea = weather.getJSONObject(0);
                    weatherResult.append("Weather : "+wea.getString("main")+"\n");
                    JSONObject temp = response.getJSONObject("main");
                    weatherResult.append("Temparature : "+temp.getString("temp")+" Feels like : "+temp.getString("feels_like")+"\nHumidity : "+temp.getString("humidity")+"\n");
                    JSONObject wind = response.getJSONObject("wind");
                    weatherResult.append("Wind Speed : "+wind.getString("speed")+" Degree : "+wind.getString("deg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getApplicationContext(),"Please check",Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        requestQueue.add(objectRequest);

    }
}