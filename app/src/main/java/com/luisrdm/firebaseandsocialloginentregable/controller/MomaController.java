package com.luisrdm.firebaseandsocialloginentregable.controller;

import android.content.Context;
import android.net.Uri;

import com.luisrdm.firebaseandsocialloginentregable.dao.ArtistDao;
import com.luisrdm.firebaseandsocialloginentregable.dao.PaintingDao;
import com.luisrdm.firebaseandsocialloginentregable.model.Artist;
import com.luisrdm.firebaseandsocialloginentregable.util.ResultListener;

import java.util.List;

/**
 * Created by digitalhouse on 20/07/16.
 */
public class MomaController {

    PaintingDao paintingDao = new PaintingDao();

    public void getArtists(final ResultListener<List<Artist>> listener){
        ArtistDao artistDao = new ArtistDao();
        artistDao.getArtistsFromFireBase(new ResultListener<List<Artist>>() {
            @Override
            public void finish(List<Artist> resultado) {
                listener.finish(resultado);
            }
        });
    }

    public void getPainting(String paintingName, Context context, final ResultListener listener){
        paintingDao.getPaintingFromFirebase(paintingName,context, new ResultListener<Uri>() {
            @Override
            public void finish(Uri resultado) {
                listener.finish(resultado);
            }
        });
    }

}
