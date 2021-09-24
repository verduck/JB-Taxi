package com.jj.bootcamp.jbtaxi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://14.55.30.222:3000";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (phoneNumber != null) {

                        } else {
                            startActivity(new Intent(getApplication(), MapsActivity.class));
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.show();
                    }
                });

        queue.add(stringRequest);
    }
}
