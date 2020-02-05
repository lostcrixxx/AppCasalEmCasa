package br.com.envolvedesenvolve.casalemcasa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
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
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.envolvedesenvolve.casalemcasa.Adapter.ListItemAdapter;
import br.com.envolvedesenvolve.casalemcasa.Model.ToDo;
import br.com.envolvedesenvolve.casalemcasa.View.AboutFragment;

/**
 * Created by Cristiano M. on 31/01/2020
 * Modified by Cristiano M. on 04/02/2020
 */

public class MainActivity extends AppCompatActivity {

    String firstUUID, codeUUID;
    public boolean isUpdate = false;
    public String idUpdate = "";
    private SharedPreferences prefs;

    public MaterialEditText title, description;
    FirebaseFirestore db;
    RecyclerView listItem;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton fab;
    ListItemAdapter adapter;

    List<ToDo> toDoList = new ArrayList<>();
//    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        db = FirebaseFirestore.getInstance();
//        dialog = new SpotsDialog(this);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        fab = findViewById(R.id.fab);

        prefs = getSharedPreferences("login", Context.MODE_PRIVATE);
        codeUUID = prefs.getString("edtCode", "teste");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isUpdate) {
                    setData(title.getText().toString(), description.getText().toString());
                } else {
                    updateData(title.getText().toString(), description.getText().toString());
                    isUpdate = !isUpdate;
                }
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
//            DialogFragment aboutFragment = new AboutFragment();

//            Bundle bundle = new Bundle();
//            bundle.putString("EXTRA_PATHFILE_ERROR", pathFileError);
//            aboutFragment.setArguments(bundle);
//            aboutFragment.show(getFragmentManager(), "dialog");

            startActivity(new Intent(getBaseContext(), AboutFragment.class));
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void updateData(String title, String description) {
        db.collection(codeUUID).document(idUpdate)
                .update("title", title, "description", description)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Updated !", Toast.LENGTH_SHORT).show();
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
                loadData();
            }
        });
    }

    private void loadData() {
        try {
//        dialog.show();
            if (toDoList.size() > 0) {
                toDoList.clear();
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
                            }
                            adapter = new ListItemAdapter(MainActivity.this, toDoList);
                            listItem.setAdapter(adapter);
//                    dialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
