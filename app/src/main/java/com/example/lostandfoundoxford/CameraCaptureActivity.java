package com.example.lostandfoundoxford;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class CameraCaptureActivity extends AppCompatActivity {
    ImageView itemImage;
    ProgressBar progressBar;
    CheckBox policeProtected;
    DatabaseReference reference;
    private Button submit;
    private Button takePic;
    private Bitmap image;
    private StorageReference mStorageReference;
    private FirebaseDatabase rootNode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture_activity);

        mStorageReference = FirebaseStorage.getInstance().getReference();

        itemImage = findViewById(R.id.imageContainer);
        progressBar = findViewById(R.id.progressBar);
        submit = findViewById(R.id.submit);
        takePic = findViewById(R.id.takepic);
        policeProtected = findViewById(R.id.policeCheck);


        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(camera, 0);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                upload();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            image = (Bitmap) data.getExtras().get("data");

            itemImage.setImageBitmap(image);
            itemImage.setVisibility(View.VISIBLE);
        }

    }

    private void upload() {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        final String random = UUID.randomUUID().toString();
        StorageReference imageRef = mStorageReference.child("image/" + random);

        byte[] b = stream.toByteArray();
        imageRef.putBytes(b)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        Toast.makeText(CameraCaptureActivity.this, "Image Upload Successful", Toast.LENGTH_SHORT).show();

                        taskSnapshot.getMetadata().getReference().getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Double longitude = null;
                                        Double latitude = null;
                                        Uri link = uri;

                                        ActivityCompat.requestPermissions(CameraCaptureActivity.this, new String[]
                                                {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                                        //Check if GPS is on
                                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                                            //enable GPS if off
                                            onGPS();
                                        } else {
                                            //when gps is already on
                                            if (ActivityCompat.checkSelfPermission(CameraCaptureActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CameraCaptureActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                                // TODO: Consider calling
                                                //    ActivityCompat#requestPermissions
                                                // here to request the missing permissions, and then overriding
                                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                //                                          int[] grantResults)
                                                // to handle the case where the user grants the permission. See the documentation
                                                // for ActivityCompat#requestPermissions for more details.
                                                return;
                                            }
                                            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                                            latitude = locationGPS.getLatitude();
                                            longitude = locationGPS.getLongitude();
                                            if(latitude!=null && longitude!=null)
                                            Toast.makeText(CameraCaptureActivity.this, "Current Location Details Captured", Toast.LENGTH_SHORT).show();


                                        }


                                        rootNode = FirebaseDatabase.getInstance();
                                        reference = rootNode.getReference("FoundItemData");

                                        if (random != null) {
                                            FoundItemModel model = new FoundItemModel(random, "", link.toString(), latitude, longitude, policeProtected.isChecked());
                                            reference.child(random).setValue(model, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                    Toast.makeText(CameraCaptureActivity.this, "Data Uploading Complete...Thank You", Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.GONE);

                                                }
                                            });

                                        } else {
                                            Toast.makeText(CameraCaptureActivity.this, "Please click a picture of the Found item", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }

                                    }

                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        Toast.makeText(CameraCaptureActivity.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });

    }

    private void onGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
