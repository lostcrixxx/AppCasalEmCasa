package br.com.envolvedesenvolve.casalemcasa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

/**
 * Created by Cristiano M. on 02/02/2020
 * Modified by Cristiano M. on 09/02/2020
 */

public class LoginActivity extends AppCompatActivity {

    String code;

    Button btnLogin;
    EditText edtCode;
    TextView txtClickHere, txtCode;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        changeStatusBarColor();

        edtCode = findViewById(R.id.edtCode);
        btnLogin = findViewById(R.id.btnLogin);
        txtClickHere = findViewById(R.id.txtClickHere);
        txtCode = findViewById(R.id.txtCode);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtCode.getText().toString().isEmpty() || !edtCode.getText().toString().equals("")) {
                    if(edtCode.getText().toString().length() >= 8) {
                        String tmpCode = edtCode.getText().toString();
                        connect(tmpCode);
                    } else{
                        Toast.makeText(getBaseContext(), "Código deve conter 8 caracteres !", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Preencha o campo !", Toast.LENGTH_LONG).show();
                }
            }
        });

        txtClickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtCode.getText().toString().isEmpty()){
                    String id1 = UUID.randomUUID().toString();
                    String firstUUID = id1.split("-")[0];
                    Log.e("UUI", "UUI " + id1);
                    Log.e("UUI", "UUI last " + firstUUID);
                    connect(firstUUID);
                } else {
                    Toast.makeText(getBaseContext(), "Já existe uma lista criada !", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void connect(String code){
        Checkable chkLogin = findViewById(R.id.chkLogin);
        boolean statusLogin = false;
        if(chkLogin.isChecked()){
            statusLogin = true;
        }

        prefs = getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString("codePrefs", code);
        ed.putBoolean("statusPrefs", statusLogin);
        ed.commit();

        startActivity(new Intent(getBaseContext(), MainActivity.class));
    }

    private void changeStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
    }

    public void onLoginClick(View View) {
        startActivity(new Intent(this, RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
    }

    @Override
    protected void onResume() {
        super.onResume();
        prefs = getSharedPreferences("login", Context.MODE_PRIVATE);
        code = prefs.getString("codePrefs", "");
        edtCode.setText(code);
        txtCode.setText(code);
    }
}
