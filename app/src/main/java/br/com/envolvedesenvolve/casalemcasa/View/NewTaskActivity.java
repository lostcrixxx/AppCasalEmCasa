package br.com.envolvedesenvolve.casalemcasa.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import br.com.envolvedesenvolve.casalemcasa.MainActivity;
import br.com.envolvedesenvolve.casalemcasa.R;

public class NewTaskActivity extends AppCompatActivity {

    private String firstUUID, codeUUID;

    private TextView titlepage, addtitle, adddesc, adddate;
    private EditText edtTitle, edtDescription;
    private Button btnSaveTask, btnCancel;

    private FirebaseFirestore db;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        db = FirebaseFirestore.getInstance();

        addtitle = findViewById(R.id.add_title);
        adddesc = findViewById(R.id.add_desc);

        edtTitle = findViewById(R.id.edt_new_title);
        edtDescription = findViewById(R.id.edt_new_desc);

        btnSaveTask = findViewById(R.id.btn_save_task);
        btnCancel = findViewById(R.id.btn_cancel);

        prefs = getSharedPreferences("login", Context.MODE_PRIVATE);
        codeUUID = prefs.getString("codePrefs", "teste");
        Log.e("teste", "UUID " + codeUUID);

        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtTitle.getText().toString().isEmpty() || !edtDescription.getText().toString().isEmpty()) {
//                    if (!isUpdate) {
                    setData(edtTitle.getText().toString(), edtDescription.getText().toString());
//                    } else {
//                        updateData(edtTitle.getText().toString(), edtDescription.getText().toString());
//                        isUpdate = !isUpdate;
//                    }
                } else {
                    Toast.makeText(NewTaskActivity.this, "Preencha os campos !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setData(String title, String description) {
        String id = UUID.randomUUID().toString();
        Map<String, Object> todo = new HashMap<>();
        todo.put("id", id);
        todo.put("title", title);
        todo.put("description", description);

        db.collection(codeUUID).document(id)
                .set(todo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                edtTitle.setText("");
//                edtDescription.setText("");
                finish();
                Intent a = new Intent(NewTaskActivity.this, MainActivity.class);
                startActivity(a);
            }
        });
    }
}
