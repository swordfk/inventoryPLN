package id.example.repositorypln;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class splashscreen extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        checkPermission();

    }

    protected void checkPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                + ContextCompat.checkSelfPermission(
                this,Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){

            // Do something, when permissions not granted
            if(ActivityCompat.shouldShowRequestPermissionRationale(
                    this,Manifest.permission.CAMERA)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this,Manifest.permission.READ_PHONE_STATE)){
                // If we should give explanation of requested permissions

                // Show an alert dialog here with request explanation
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Camera and Location permissions are required to do the task.");
                builder.setTitle("Please grant those permissions");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(
                                splashscreen.this,
                                new String[]{
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.READ_PHONE_STATE,
                                },
                                MY_PERMISSIONS_REQUEST_CODE
                        );
                    }
                });
                builder.setNeutralButton("Cancel",null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                // Directly request for required permissions, without explanation
                ActivityCompat.requestPermissions(
                        splashscreen.this,
                        new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_PHONE_STATE,
                        },
                        MY_PERMISSIONS_REQUEST_CODE
                );
            }
        }else {
            // Do something, when permissions are already granted
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(splashscreen.this,Login.class);
                    splashscreen.this.startActivity(mainIntent);
                    splashscreen.this.finish();
                }
            }, 500);
            Toast.makeText(this,"Permissions already granted",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CODE:{
                // When request is cancelled, the results array are empty
                if(
                        (grantResults.length >0) &&
                                (grantResults[0]
                                        + grantResults[1]
                                        == PackageManager.PERMISSION_GRANTED
                                )
                ){
                    // Permissions are granted
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            /* Create an Intent that will start the Menu-Activity. */
                            Intent mainIntent = new Intent(splashscreen.this,Login.class);
                            splashscreen.this.startActivity(mainIntent);
                            splashscreen.this.finish();
                        }
                    }, 500);
                    Toast.makeText(this,"Permissions granted.",Toast.LENGTH_SHORT).show();
                }else {
                    // Permissions are denied
                    Toast.makeText(this,"Permissions denied.",Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}