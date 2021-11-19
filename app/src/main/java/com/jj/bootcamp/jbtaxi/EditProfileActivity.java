package com.jj.bootcamp.jbtaxi;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.jj.bootcamp.jbtaxi.domain.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText editPhoneNumber;
    private EditText editName;
    private EditText editBirth;
    private EditText editCertificationNumber;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        user = getIntent().getParcelableExtra("user");

        toolbar = (Toolbar) findViewById(R.id.edit_profile_toolbar);
        editPhoneNumber = (EditText) findViewById(R.id.edit_phone_number);
        editName = (EditText) findViewById(R.id.edit_name);
        editBirth = (EditText) findViewById(R.id.edit_birth);
        editCertificationNumber = (EditText) findViewById(R.id.edit_certification_number);

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
                    String birth = editBirth.getText().toString();
                    if (name == "") {
                        Toast.makeText(getApplicationContext(), "이름을 입력하세요", Toast.LENGTH_SHORT);
                        return false;
                    }
                    user.setName(name);
                    if (birth != "" && editBirth.getText().length() != 0) {
                        LocalDate birthDate = LocalDate.parse(birth, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        user.setBirth(birthDate);
                    }

                    Call<User> call = JBTaxiAPI.getInstance().getService().updateUser(user);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                user = response.body();
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });
                }
                return true;
            }
        });

        editPhoneNumber.setText(user.getPhoneNumber());
        editName.setText(user.getName());
        if (user.getBirth() != null) {
            editBirth.setText(user.getBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        editBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new DatePickerFragment();
                dialogFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        if (user.getCertificationNumber() != null) {
            editCertificationNumber.setText(user.getCertificationNumber());
        }
    }

    public void processDatePickerResult(int year, int month, int day) {
        user.setBirth(LocalDate.of(year, month, day));
        editBirth.setText(user.getBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }


}
