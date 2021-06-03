package id.example.repositorypln;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import id.example.repositorypln.model.Barang;

public class AddBarang extends AppCompatActivity {
    EditText nama, inventaris, status, keterangan, harga;
    Button tambahBarang;
    FirebaseDatabase database;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_barang);

        String namaB = getIntent().getStringExtra("nama");
        String noB = getIntent().getStringExtra("no");
        String statusB = getIntent().getStringExtra("status");
        String ketB = getIntent().getStringExtra("ket");
        String keyB = getIntent().getStringExtra("key");
        String hargaB  = getIntent().getStringExtra("harga");
        boolean edit = getIntent().getBooleanExtra("edit", false);

        Log.i("TAG", "onCreate: "+keyB);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("barangs");

        harga = findViewById(R.id.harga);
        nama = findViewById(R.id.namaBarang);
        inventaris = findViewById(R.id.noBarang);
        status = findViewById(R.id.status);
        keterangan = findViewById(R.id.keterangan);
        tambahBarang = findViewById(R.id.tambahBarang);

        if (edit){
            tambahBarang.setText("Edit Barang");
            harga.setText(hargaB);
            nama.setText(namaB);
            inventaris.setText(noB);
            status.setText(statusB);
            keterangan.setText(ketB);
        }

        tambahBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit){
                    editbarang(nama.getText().toString(), inventaris.getText().toString(), status.getText().toString(), keterangan.getText().toString(),keyB, harga.getText().toString());
                }else {
                    addBarang(nama.getText().toString(), inventaris.getText().toString(), status.getText().toString(), keterangan.getText().toString(), harga.getText().toString());
                }
            }
        });
    }

    private void addBarang(String nama, String no, String status, String ket, String harga){
        String uploadId = ref.push().getKey();
        Barang barang = new Barang(nama, no, status, ket, harga);

        ref.child(uploadId).setValue(barang);
        startActivity(new Intent(AddBarang.this, AdminActivity.class));
        finish();
    }

    private void editbarang(String nama, String no, String status, String ket, String key,String harga){
        Barang barang = new Barang(nama, no, status, ket, harga);

        ref.child(key).setValue(barang);

        startActivity(new Intent(AddBarang.this, AdminActivity.class));
        finish();
    }
}