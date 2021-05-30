package id.example.repositorypln;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import id.example.repositorypln.model.Permintaan;

public class PermintaanUser extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Permintaan> listPermintaan = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permintaan_user);

        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(linearLayoutManager);

        getData();
    }

    private void getData(){
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("permintaan");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listPermintaan.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Permintaan upload = dataSnapshot.getValue(Permintaan.class);
                    upload.setKey(dataSnapshot.getKey());
                    listPermintaan.add(upload);
                }
                adapter = new AdapterPermintaanUser(PermintaanUser.this, listPermintaan);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PermintaanUser.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}