package com.jj.bootcamp.jbtaxi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.jj.bootcamp.jbtaxi.domain.User;

public class ProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private LinearLayout userLayout;
    private LinearLayout taxiLayout;
    private TextView name;
    private Button btnLogout;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = getIntent().getParcelableExtra("user");

        toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        userLayout = (LinearLayout) findViewById(R.id.user_layout);
        taxiLayout = (LinearLayout) findViewById(R.id.taxi_layout);
        name = (TextView) findViewById(R.id.profile_name);
        btnLogout = (Button) findViewById(R.id.sign_out);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        userLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        taxiLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        name.setText(user.getName() + name.getText());

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("jbtaxi_preference", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("phone_number");
                editor.commit();
                ActivityCompat.finishAffinity(ProfileActivity.this);
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });
    }
}
