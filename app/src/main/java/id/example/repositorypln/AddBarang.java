package id.example.repositorypln;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("barangs");

        harga = findViewById(R.id.harga);
        nama = findViewById(R.id.namaBarang);
        inventaris = findViewById(R.id.noBarang);
        status = findViewById(R.id.status);
        keterangan = findViewById(R.id.keterangan);
        tambahBarang = findViewById(R.id.tambahBarang);

        if (keyB != null){
            if (!keyB.contains("")){
                tambahBarang.setText("Edit Barang");
            }
        }

        tambahBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (keyB != null){
                    if (!keyB.contains("")){
                        editbarang(namaB, noB, statusB, ketB, keyB,  hargaB);
                    }
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
        finish();
    }

    private void editbarang(String nama, String no, String status, String ket, String key,String harga){
        Barang barang = new Barang(nama, no, status, ket, harga);

        ref.child(ket).setValue(barang);
        finish();
    }
}