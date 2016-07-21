package com.luisrdm.firebaseandsocialloginentregable.dao;

import android.content.Context;
import android.graphics.Movie;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.luisrdm.firebaseandsocialloginentregable.model.Artist;
import com.luisrdm.firebaseandsocialloginentregable.model.ArtistContainer;
import com.luisrdm.firebaseandsocialloginentregable.util.ResultListener;

import java.util.List;

/**
 * Created by digitalhouse on 20/07/16.
 */
public class ArtistDao {

    private DatabaseReference mDatabase;

    public void getArtistsFromFireBase(final ResultListener<List<Artist>> listener) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArtistContainer artistContainer = dataSnapshot.getValue(ArtistContainer.class);
                        listener.finish(artistContainer.getArtists());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

}
