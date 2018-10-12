package com.example.lakshan.weatherapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lakshan.weatherapp.api.RetrofitClient;
import com.example.lakshan.weatherapp.helpers.AppUtil;
import com.example.lakshan.weatherapp.model.CityDetails;
import com.example.lakshan.weatherapp.model.WeatherData;
import com.example.lakshan.weatherapp.services.EndPointService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    private Spinner citySpinner;
    private ArrayList<CityDetails> cityDetailsArrayList;
    private int cityId;
    private TextView cityName;
    private TextView cityIdTextView;
    private TextView descriptionTextView;
    private TextView tempValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName = (TextView) findViewById(R.id.city_name);
        cityIdTextView = (TextView) findViewById(R.id.city_id);
        descriptionTextView = (TextView) findViewById(R.id.server_description);
        tempValue = (TextView) findViewById(R.id.temp_value);
        citySpinner = (Spinner) findViewById(R.id.city_spinner);
        cityDetailsArrayList = new ArrayList<>();
        readJson();

        if (!AppUtil.checkNetworkConnection(MainActivity.this)) {
            final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
            AppUtil.confirmAlert(dialog, MainActivity.this, getResources().getString(R.string.no_network_title), getResources().getString(R.string.msg_turn_on_network),
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v1) {
                            // When click settings
                            AppUtil.showSystemSettingsDialog(MainActivity.this);
                            dialog.dismiss();
                        }
                    }, null, getResources().getString(R.string.text_settings), getResources().getString(R.string.text_cancel), true);

        }

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityId = cityDetailsArrayList.get(position).getCityId();
                callApi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //read data from localy stored Step1.json file and put it to arraylist
    private void readJson() {
        String json;
        try {
            InputStream inputStream = getAssets().open("Step1.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            json = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(getString(R.string.list));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                int cityId = object.getInt(getString(R.string.city_code));
                String cityName = object.getString(getString(R.string.city_name));
                CityDetails cityDetails = new CityDetails(cityId, cityName);
                this.cityDetailsArrayList.add(cityDetails);

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<String> loadingCities = new ArrayList<String>();
        for (int x = 0; x < cityDetailsArrayList.size(); x++) {
            loadingCities.add(cityDetailsArrayList.get(x).getCity());
        }
        setCitySpinner(loadingCities);
    }

    private void setCitySpinner(ArrayList<String> cities) {
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item,
                cities) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // Cast the spinner collapsed trip_item (non-popup trip_item) as a text view
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setGravity(Gravity.RIGHT);
                return textView;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView dropDownView = (TextView) super.getDropDownView(position, convertView, parent);
                // Set the text color of drop down items
                dropDownView.setPadding(16, 16, 16, 16);
                dropDownView.setGravity(Gravity.RIGHT);
                return dropDownView;
            }
        };
        citySpinner.setAdapter(cityAdapter);
    }

    private void callApi() {
        RetrofitClient.getInstance();
        EndPointService endPointService = RetrofitClient.getInstance().create(EndPointService.class);
        String units = getString(R.string.units);
        String app_id = getString(R.string.api_key);
        Call<WeatherData> call = endPointService.getWeatherData(cityId, units, app_id);

        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, retrofit2.Response<WeatherData> response) {
                if (response.code() == 200) {
                    WeatherData weatherData = response.body();
                    cityName.setText(response.body().getName());
                    cityIdTextView.setText(Integer.toString(weatherData.getId()));
                    descriptionTextView.setText(response.body().getWeather().get(0).getDescription());
                    tempValue.setText(Double.toString(weatherData.getMain().getTemp()));
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                System.out.println("api is not working" + t.getMessage());
            }
        });
    }

}
