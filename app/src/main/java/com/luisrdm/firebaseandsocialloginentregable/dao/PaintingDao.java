package com.luisrdm.firebaseandsocialloginentregable.dao;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.luisrdm.firebaseandsocialloginentregable.util.ResultListener;

/**
 * Created by izu on 21/07/2016.
 */
public class PaintingDao {

    Context context;
    Uri paintingRefUri;
    StorageReference storageRef;

    public void getPaintingFromFirebase (final String paintingRoute, final Context context, final ResultListener<Uri> listener){
        this.context = context;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://fir-andsociallogin.appspot.com/"+paintingRoute);

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                paintingRefUri = uri;
                listener.finish(uri);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(context, "Falló bajar la imagen.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Uri getPaintingRefUri() {
        return paintingRefUri;
    }

    public void setPaintingRefUri(Uri paintingRefUri) {
        this.paintingRefUri = paintingRefUri;
    }
}
