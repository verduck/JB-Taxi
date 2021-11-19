package com.jj.bootcamp.jbtaxi;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.jj.bootcamp.jbtaxi.domain.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private Dialog dialog;
    private Toolbar toolbar;
    private LinearLayout userLayout;
    private LinearLayout taxiLayout;
    private LinearLayout passLayout;
    private TextView name;
    private Button btnLogout;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_register_certification, null))
            .setMessage("택시운전자격증 등록")
            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            })
            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    EditText number = (EditText) dialog.findViewById(R.id.register_certification_number);
                    registerCertification(number.getText().toString());
                }
            });
        dialog = builder.create();

        user = getIntent().getParcelableExtra("user");
        toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        userLayout = (LinearLayout) findViewById(R.id.user_layout);
        taxiLayout = (LinearLayout) findViewById(R.id.taxi_layout);
        passLayout = (LinearLayout) findViewById(R.id.pass_layout);
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
                if (user.getCertificationNumber() == null) {
                    dialog.show();
                } else {
                    startTaxiManagementActivity();
                }
            }
        });

        passLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        name.setText(user.getName() + "님");

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

    @Override
    public void onStart() {
        super.onStart();
        loadUser();
    }

    private void loadUser() {
        JBTaxiAPI.getInstance().getService().getUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    name.setText(user.getName() + "님");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void registerCertification(String certificationNumber) {
        user.setCertificationNumber(certificationNumber);
        JBTaxiAPI.getInstance().getService().registerCertification(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
                dialog.dismiss();
                if (user.getCertificationNumber() != null) {
                    startTaxiManagementActivity();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void startTaxiManagementActivity() {
        Intent intent = new Intent(getApplicationContext(), TaxiManagementActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    public void setUser(User user) {
        this.user = user;
    }
}
