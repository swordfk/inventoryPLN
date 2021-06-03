package id.example.repositorypln;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import id.example.repositorypln.model.Barang;

public class DashboardAdmin extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterBarangAdmin adapter;
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Barang> listBarang = new ArrayList<>();
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);

        searchView = findViewById(R.id.searchview);
        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(linearLayoutManager);

        getData();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
    }

    private void getData(){
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("barangs");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listBarang.clear();
                for (DataSnapshot ktpSnapshot : snapshot.getChildren()) {
                    Barang upload = ktpSnapshot.getValue(Barang.class);
                    upload.setKey(ktpSnapshot.getKey());
                    listBarang.add(upload);
                }
                adapter = new AdapterBarangAdmin(DashboardAdmin.this, listBarang);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DashboardAdmin.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}