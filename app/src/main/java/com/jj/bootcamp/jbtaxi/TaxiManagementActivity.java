package com.jj.bootcamp.jbtaxi;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jj.bootcamp.jbtaxi.domain.Taxi;
import com.jj.bootcamp.jbtaxi.domain.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaxiManagementActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText editName;
    private EditText editCarNumber;
    private User user;
    private Taxi taxi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_taxi_management);

        user = getIntent().getParcelableExtra("user");

        toolbar = (Toolbar) findViewById(R.id.edit_profile_toolbar);
        editName = (EditText) findViewById(R.id.edit_car_name);
        editCarNumber = (EditText) findViewById(R.id.edit_car_number);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.edit_save) {
                    String name = editName.getText().toString();
                    String carNumber = editCarNumber.getText().toString();

                    if (!name.isEmpty() && !carNumber.isEmpty()) {
                        registerTaxi(name, carNumber);
                    }
                }
                return false;
            }
        });

        loadTaxi();
    }

    private void loadTaxi() {
        if (user != null) {
            JBTaxiAPI.getInstance().getService().getTaxi(user).enqueue(new Callback<Taxi>() {
                @Override
                public void onResponse(Call<Taxi> call, Response<Taxi> response) {
                    if (response.isSuccessful()) {
                        taxi = response.body();
                        if (taxi != null) {
                            editName.setText(taxi.getName());
                            editCarNumber.setText(taxi.getCarNumber());
                        }
                    }
                }

                @Override
                public void onFailure(Call<Taxi> call, Throwable t) {

                }
            });
        }
    }

    private void registerTaxi(String name, String carNumber) {
        if (taxi == null) {
            taxi = new Taxi();
        }

        taxi.setUser(user);
        taxi.setName(name);
        taxi.setCarNumber(carNumber);

        JBTaxiAPI.getInstance().getService().registerTaxi(taxi).enqueue(new Callback<Taxi>() {
            @Override
            public void onResponse(Call<Taxi> call, Response<Taxi> response) {
                if (response.isSuccessful()) {
                    taxi = response.body();
                    if (taxi != null) {
                        editName.setText(taxi.getName());
                        editCarNumber.setText(taxi.getCarNumber());
                    }
                }
            }

            @Override
            public void onFailure(Call<Taxi> call, Throwable t) {

            }
        });
    }
}
