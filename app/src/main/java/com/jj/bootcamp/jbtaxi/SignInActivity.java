package com.jj.bootcamp.jbtaxi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.jj.bootcamp.jbtaxi.adapter.CountryCodesAdapter;
import com.jj.bootcamp.jbtaxi.domain.User;
import com.jj.bootcamp.jbtaxi.service.HeyTaxiService;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInActivity extends AppCompatActivity {
    private Spinner countryCode;
    private EditText phoneNumber;
    private FloatingActionButton nextFab;
    private Retrofit retrofit;
    private HeyTaxiService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        countryCode = (Spinner) findViewById(R.id.sign_up_country_code);
        phoneNumber = (EditText) findViewById(R.id.sign_up_phone_number);
        nextFab = (FloatingActionButton) findViewById(R.id.fab_sign_up_next);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://14.55.30.117:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(HeyTaxiService.class);

        final CountryCodesAdapter ccAdapter = new CountryCodesAdapter(this, android.R.layout.simple_list_item_1, android.R.layout.simple_spinner_dropdown_item);
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        Set<String> ccSet = getSupportedRegions(phoneNumberUtil);

        for (String cc : ccSet) {
            ccAdapter.add(cc);
        }
        ccAdapter.sort(new Comparator<CountryCodesAdapter.CountryCode>() {
            public int compare(CountryCodesAdapter.CountryCode lhs, CountryCodesAdapter.CountryCode rhs) {
                return lhs.regionName.compareTo(rhs.regionName);
            }
        });
        countryCode.setAdapter(ccAdapter);
        countryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ccAdapter.setSelected(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Phonenumber.PhoneNumber myNum = getMyNumber(this);
        if (myNum != null) {
            CountryCodesAdapter.CountryCode cc = new CountryCodesAdapter.CountryCode();
            cc.regionCode = phoneNumberUtil.getRegionCodeForNumber(myNum);
            if (cc.regionCode == null)
                cc.regionCode = phoneNumberUtil.getRegionCodeForCountryCode(myNum.getCountryCode());
            countryCode.setSelection(ccAdapter.getPositionForId(cc));
            phoneNumber.setText(String.valueOf(myNum.getNationalNumber()));
        }
        else {
            final TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            final String regionCode = tm.getSimCountryIso().toUpperCase(Locale.US);
            CountryCodesAdapter.CountryCode cc = new CountryCodesAdapter.CountryCode();
            cc.regionCode = regionCode;
            cc.countryCode = phoneNumberUtil.getCountryCodeForRegion(regionCode);
            countryCode.setSelection(ccAdapter.getPositionForId(cc));
        }

        phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        nextFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                user.setPhoneNumber(phoneNumber.getText().toString());
                Call<User> call = service.signIn(user);
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
        });
    }

    @SuppressWarnings("unchecked")
    private Set<String> getSupportedRegions(PhoneNumberUtil util) {
        try {
            return (Set<String>) util.getClass()
                    .getMethod("getSupportedRegions")
                    .invoke(util);
        } catch (NoSuchMethodException e) {
            try {
                return (Set<String>) util.getClass()
                        .getMethod("getSupportedCountries")
                        .invoke(util);
            } catch (Exception helpme) {
                // ignored
            }
        } catch (Exception e) {
            // ignored
        }
        return new HashSet<>();
    }

    @SuppressLint("HardwareIds")
    public Phonenumber.PhoneNumber getMyNumber(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String regionCode = tm.getSimCountryIso().toUpperCase(Locale.US);
            return PhoneNumberUtil.getInstance().parse(tm.getLine1Number(), regionCode);
        }
        catch (Exception e) {
            return null;
        }
    }
}
