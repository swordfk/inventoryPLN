package id.example.repositorypln;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import id.example.repositorypln.model.Permintaan;

public class MainKepalaGudang extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    String kueri;
    private List<Permintaan> listPermintaan = new ArrayList<>();
    Button semua, sebulan;
    LinearLayout layout;
    ImageView export;
    TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_kepala_gudang);

        total = findViewById(R.id.totalharga);
        export = findViewById(R.id.download);
        layout = findViewById(R.id.layout);
        semua = findViewById(R.id.semua);
        sebulan = findViewById(R.id.sebulan);
        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(linearLayoutManager);

        kueri = getIntent().getStringExtra("query");

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportExcel();
            }
        });

        if (kueri.contains("selesai")){
            layout.setVisibility(View.VISIBLE);
        }else {
            layout.setVisibility(View.GONE);
        }

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("permintaan");

        getData();

        sebulan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseRef = FirebaseDatabase.getInstance().getReference("permintaanK");
                getDataSatuBulan();
                Toast.makeText(MainKepalaGudang.this, listPermintaan.size()+"", Toast.LENGTH_SHORT).show();
            }
        });

        semua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseRef = FirebaseDatabase.getInstance().getReference("permintaan");
                getData();
            }
        });
    }

    private void getData(){
        listPermintaan.clear();
        Query query = mDatabaseRef.orderByChild("statusPermintaan").equalTo(kueri);

        mDBListener = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listPermintaan.clear();
                for (DataSnapshot ktpSnapshot : snapshot.getChildren()) {
                    Permintaan upload = ktpSnapshot.getValue(Permintaan.class);
                    upload.setKey(ktpSnapshot.getKey());
                    listPermintaan.add(upload);
                }
                adapter = new AdapterBarangGudang(MainKepalaGudang.this, listPermintaan);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                int totalharga = 0;
                for (int i=0; i<listPermintaan.size(); i++){
                    totalharga += Integer.parseInt(listPermintaan.get(i).getPerolB());
                }

                total.setText(String.valueOf(totalharga));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainKepalaGudang.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataSatuBulan(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");

        String today = sdf.format(Calendar.getInstance().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date date = calendar.getTime();
        String oneMonth = sdf.format(date);


        listPermintaan.clear();
        Query query = mDatabaseRef.orderByChild("tanggal").startAt(oneMonth)
                .endAt(today);

        mDBListener = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listPermintaan.clear();
                for (DataSnapshot ktpSnapshot : snapshot.getChildren()) {
                    Permintaan upload = ktpSnapshot.getValue(Permintaan.class);
                    upload.setKey(ktpSnapshot.getKey());
                    listPermintaan.add(upload);
                }
                adapter = new AdapterBarangGudang(MainKepalaGudang.this, listPermintaan);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                int totalharga = 0;
                for (int i=0; i<listPermintaan.size(); i++){
                    totalharga += Integer.parseInt(listPermintaan.get(i).getPerolB());
                }

                total.setText(String.valueOf(totalharga));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainKepalaGudang.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void exportExcel(){
        //generate data
        StringBuilder data = new StringBuilder();
        data.append("Nama,Nama Barang,No Inventaris, Nilai Perol, Status Permintaan,Tanggal,Tempat");
        for(int i = 0; i<listPermintaan.size(); i++){
            data.append("\n"+listPermintaan.get(i).getNama()+
                    ","+listPermintaan.get(i).getNamaB()+
                    ","+listPermintaan.get(i).getNoInventarisB()+
                    ","+listPermintaan.get(i).getPerolB()+
                    ","+listPermintaan.get(i).getStatusPermintaan()+
                    ","+listPermintaan.get(i).getTanggal()+
                    ","+listPermintaan.get(i).getTempat());
        }

        try{
            //saving the file into device
            FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
            out.write((data.toString()).getBytes());
            out.close();

            //exporting
            Context context = getApplicationContext();
            File filelocation = new File(getFilesDir(), "data.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.repositorypln.fileprovider", filelocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent, "Send mail"));
        }
        catch(Exception e){
            e.printStackTrace();
        }


    }
}