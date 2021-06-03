package id.example.repositorypln;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.camera.CameraSettings;

import java.util.List;

import id.example.repositorypln.model.Permintaan;

public class ScanBarang extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseRef2;
    private DatabaseReference mDatabaseRef3;
    Permintaan barang = new Permintaan();
    FirebaseDatabase database;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barang);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("permintaan");
        mDatabaseRef3 = FirebaseDatabase.getInstance().getReference("permintaanK");
        mDatabaseRef2 = FirebaseDatabase.getInstance().getReference("barangs");
        DecoratedBarcodeView qrView = findViewById(R.id.qr_scanner_view);
        CameraSettings s = new CameraSettings();
        s.setRequestedCameraId(0); // front/back/etc
        qrView.getBarcodeView().setCameraSettings(s);
        qrView.resume();

        qrView.decodeSingle(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                Dialog dialog2 = new Dialog(ScanBarang.this);
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog2.setContentView(R.layout.dialog_scan);
                dialog2.getWindow().setGravity(Gravity.CENTER);
                dialog2.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView nama = dialog2.findViewById(R.id.namaBarang);
                TextView no = dialog2.findViewById(R.id.noInventaris);
                TextView status = dialog2.findViewById(R.id.statusBarang);
                Button konfirmasi = dialog2.findViewById(R.id.konfirmasi);

                Query query = mDatabaseRef.orderByChild("noInventarisB").equalTo(result.getText());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            barang = snapshot.getValue(Permintaan.class);
                            barang.setKey(snapshot.getKey());

                            nama.setText(barang.getNamaB());
                            no.setText(barang.getNoInventarisB());
                            status.setText(barang.getStatusB());

                            Log.i("TAG", "onDataChange: "+barang.key);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });
                dialog2.show();

                konfirmasi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Permintaan permintaan = new Permintaan(barang.getTanggal(), barang.getNama(), barang.getTempat(), "selesai",
                                barang.getNamaB(), barang.getKeyB(), barang.getNoInventarisB(),
                                barang.getStatusB(), barang.getKeteranganB(), barang.getPerolB());

                        mDatabaseRef3.child(mDatabaseRef3.push().getKey()).setValue(permintaan);
                        mDatabaseRef.child(barang.key).setValue(permintaan);
                        mDatabaseRef2.child(barang.keyB).removeValue();

                        dialog2.cancel();
                        finish();
                    }
                });
                // do your thing with result
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {}
        });
    }

}