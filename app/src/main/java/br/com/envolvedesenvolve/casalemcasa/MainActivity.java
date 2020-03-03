package br.com.envolvedesenvolve.casalemcasa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.envolvedesenvolve.casalemcasa.Adapter.ListItemAdapter;
import br.com.envolvedesenvolve.casalemcasa.Model.ToDo;
import br.com.envolvedesenvolve.casalemcasa.View.AboutFragment;
import br.com.envolvedesenvolve.casalemcasa.View.NewTaskActivity;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by Cristiano M. on 31/01/2020
 * Modified by Cristiano M. on 02/03/2020
 */

public class MainActivity extends AppCompatActivity {

    private String codeUUID;
    public String idUpdate = "";
    public boolean isUpdate = false;
    private int badgeCount;

    public EditText edtTitle, edtDescription;

    private SharedPreferences prefs;
    private FirebaseFirestore db;
    private RecyclerView listItem;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton fab;
    private ListItemAdapter adapter;

    List<ToDo> toDoList = new ArrayList<>();
//    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
//        dialog = new SpotsDialog(this);
        fab = findViewById(R.id.fab);

        prefs = getSharedPreferences("login", Context.MODE_PRIVATE);
        codeUUID = prefs.getString("codePrefs", "teste");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewTaskActivity.class));

//                if(!edtTitle.getText().toString().isEmpty() || !edtDescription.getText().toString().isEmpty()) {
//                    if (!isUpdate) {
//                        setData(edtTitle.getText().toString(), edtDescription.getText().toString());
//                    } else {
//                        updateData(edtTitle.getText().toString(), edtDescription.getText().toString());
//                        isUpdate = !isUpdate;
//                    }
//                } else {
//                    Toast.makeText(MainActivity.this, "Preencha os campos !", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        listItem = (RecyclerView) this.findViewById(R.id.listTodo);
        listItem.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listItem.setLayoutManager(layoutManager);

        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
//            case R.id.action_code:
//            FragmentManager fm = getSupportFragmentManager();
//            CodeFragment editNameDialogFragment = CodeFragment.newInstance();
//            editNameDialogFragment.show(fm, "fragment_edit_name");
//                break;
            case R.id.action_logoff:
                prefs = getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = prefs.edit();
                ed.putBoolean("statusPrefs", false);
                ed.commit();
                onBackPressed();
                break;
            case R.id.action_about:
                FragmentManager fm = getSupportFragmentManager();
                AboutFragment editNameDialogFragment = AboutFragment.newInstance("Some Title");
                editNameDialogFragment.show(fm, "fragment_edit_name");
                break;
//            case R.id.action_settings:
//                Log.d("MainActivity", "configurações");
//                Intent iConfig = new Intent(this, ConfigActivity.class);
//                startActivity(iConfig);
//                break;
            case R.id.action_close:
//                Log.d("MainActivity", "sair");
                finishAffinity();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void updateData(String title, String description) {
        db.collection(codeUUID).document(idUpdate)
                .update("title", title, "description", description)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Atualizado !", Toast.LENGTH_SHORT).show();
                    }
                });

        db.collection(codeUUID).document(idUpdate)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        loadData();
                    }
                });
    }

    private void loadData() {
        try {
//            badgeCount = 0;
//        dialog.show();
            if (toDoList.size() > 0) {
                toDoList.clear();
                badgeCount = 0;
            }
            db.collection(codeUUID)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                ToDo todo = new ToDo(doc.getString("id"),
                                        doc.getString("title"),
                                        doc.getString("description"));

                                toDoList.add(todo);
                                badgeCount += 1;
                                Log.e("MainActivity", "badge antes" + badgeCount);
                            }

                            // Exibe a quantidade de tarefas no ícone
                            ShortcutBadger.applyCount(getBaseContext(), badgeCount);
                            Log.e("MainActivity", "badge depois " + badgeCount);

                            adapter = new ListItemAdapter(MainActivity.this, toDoList);
                            listItem.setAdapter(adapter);
//                    dialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "ERRO " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("MainActivity", "teste " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals("APAGAR")) {
            deleteItem(item.getOrder());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteItem(int index) {
        db.collection(codeUUID)
                .document(toDoList.get(index).getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loadData();
                    }
                });
    }
}
