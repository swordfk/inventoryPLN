package id.example.repositorypln;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminItil extends AppCompatActivity {
    CardView pinjam, riwayat;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_itil);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        pinjam = findViewById(R.id.cdPinjam);
        riwayat = findViewById(R.id.cdRiwayat);

        pinjam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminItil.this, AdminActivity.class));
            }
        });
        riwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminItil.this, MainKepalaGudang.class);
                i.putExtra("query", "selesai");
                startActivity(i);
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
                Dialog dialog2 = new Dialog(AdminItil.this);
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
                                                        Toast.makeText(AdminItil.this, "Password berhasil di ubah", Toast.LENGTH_SHORT).show();
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