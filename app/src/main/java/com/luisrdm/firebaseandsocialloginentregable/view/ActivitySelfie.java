package com.luisrdm.firebaseandsocialloginentregable.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.luisrdm.firebaseandsocialloginentregable.R;
import com.luisrdm.firebaseandsocialloginentregable.util.EasyImage;

import java.io.ByteArrayOutputStream;

public class ActivitySelfie extends AppCompatActivity {

    Context context;
    private EasyImage easyImage;
    private StorageReference storageRef;
    private StorageReference imagesRef;
    private Integer counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie);

        this.context = this;

        easyImage = EasyImage.getInstance();

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        easyImage.with(this).into(imageView);

        FirebaseStorage storage = FirebaseStorage.getInstance();


        storageRef = storage.getReferenceFromUrl("gs://fir-andsociallogin.appspot.com/");
        imagesRef = storageRef.child("selfie00"+counter+".jpg");

    }

    public void showImage(View view){
        easyImage.openSelector(this);
    }

    public void uploadImage (View view){
        Bitmap bitmap = easyImage.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "Failure :(", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "Success!!!!! ;)", Toast.LENGTH_SHORT).show();
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        easyImage.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        easyImage.onRequestPermissionResult(requestCode, permissions, grantResults);
    }
}
