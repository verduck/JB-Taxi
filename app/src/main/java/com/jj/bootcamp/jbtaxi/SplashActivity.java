package com.jj.bootcamp.jbtaxi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jj.bootcamp.jbtaxi.domain.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    private String phoneNumber;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("서버에 연결할 수 없습니다.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        dialog = builder.create();

        SharedPreferences sharedPreferences = getSharedPreferences("jbtaxi_preference", MODE_PRIVATE);
        phoneNumber = sharedPreferences.getString("phone_number", null);

        if (phoneNumber == null) {
            startActivity(new Intent(getApplication(), SignInActivity.class));
        } else {
            User user = new User();
            user.setPhoneNumber(phoneNumber);
            Call<User> call = JBTaxiAPI.getInstance().getService().signIn(user);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        User res = response.body();
                        SharedPreferences sharedPreferences = getSharedPreferences("jbtaxi_preference", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("phone_number", res.getPhoneNumber());
                        editor.commit();
                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        intent.putExtra("user", res);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "서버에 연결할 수 없습니다", Toast.LENGTH_SHORT);
                }
            });
        }
        finish();
    }
}
