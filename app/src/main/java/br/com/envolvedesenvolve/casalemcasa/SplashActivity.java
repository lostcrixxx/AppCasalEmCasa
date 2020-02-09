package br.com.envolvedesenvolve.casalemcasa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Cristiano M. on 09/02/2020
 * Modified by Cristiano M. on 09/02/2020
 */

public class SplashActivity extends AppCompatActivity {

    String code;
    boolean statusLogin;
    Handler handler;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefs = getSharedPreferences("login", Context.MODE_PRIVATE);
        code = prefs.getString("codePrefs", "");
        statusLogin = prefs.getBoolean("statusPrefs", false);

        if(!code.equals("") && statusLogin) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        }
    }
}
