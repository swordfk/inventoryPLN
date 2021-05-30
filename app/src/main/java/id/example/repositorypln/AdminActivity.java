package id.example.repositorypln;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import id.example.repositorypln.model.Barang;

public class AdminActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Barang> listBarang = new ArrayList<>();
    ImageView add;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        add = findViewById(R.id.imageAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, AddBarang.class));
            }
        });
        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(linearLayoutManager);

        getData();
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
                adapter = new AdapterBarangAdmin2(AdminActivity.this, listBarang);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.logout:
                mAuth.signOut();
                finish();
                break;

            case R.id.changePassword:
                //Do Logout
                Dialog dialog2 = new Dialog(AdminActivity.this);
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog2.setContentView(R.layout.dialog_ubahpassword);
                dialog2.getWindow().setGravity(Gravity.CENTER);
                dialog2.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                EditText newPassword = dialog2.findViewById(R.id.password);
                dialog2.show();

                Button konfirmasi = dialog2.findViewById(R.id.konfirmasi);
                konfirmasi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(user.getEmail(), "user123");
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            user.updatePassword(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        dialog2.cancel();
                                                        Toast.makeText(AdminActivity.this, "Password berhasil di ubah", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Log.d("TAG", "Error password not updated");
                                                    }
                                                }
                                            });
                                        } else {
                                            Log.d("TAG", "Error auth failed");
                                        }
                                    }
                                });
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}