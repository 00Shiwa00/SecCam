package de.ek.seccam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import de.ek.seccam.ui.foto.FotoFragment;
import de.ek.seccam.ui.foto.FotoViewModel;

public class FotoActivity extends AppCompatActivity {

    private FotoViewModel ViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this, new String[]{
                            Manifest.permission.CAMERA}
                    , 1);
        }
        else{
            Log.d("PERMISSION", "CAMERA GRANTED");
        }
        ActivityCompat.requestPermissions(
                (Activity) this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                2
        );
        Intent intent = getIntent();
        String userScreenName= intent.getStringExtra("userScreenName");
        String  userPassword = intent.getStringExtra("userPassword");
        if(userPassword != null)
            Log.d("Activity","set");
        setContentView(R.layout.foto_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new FotoFragment(userScreenName,userPassword))
                    .commitNow();
        }
        ViewModel = new ViewModelProvider(this).get(FotoViewModel.class);
    }
}