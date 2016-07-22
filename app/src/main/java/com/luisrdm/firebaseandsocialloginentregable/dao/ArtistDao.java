package com.luisrdm.firebaseandsocialloginentregable.dao;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
                        Log.d("Async desde el DAO","exito");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("Async desde el DAO","fracaso");
                    }
                });
    }

}
