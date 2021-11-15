package com.jj.bootcamp.jbtaxi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Retrofit;

public class SignInActivity extends AppCompatActivity {
    private EditText txtPhoneNumber;
    private Button fabNext;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        txtPhoneNumber = (EditText) findViewById(R.id.sign_up_phone_number);
        fabNext = (Button) findViewById(R.id.fab_signup_next);

        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
