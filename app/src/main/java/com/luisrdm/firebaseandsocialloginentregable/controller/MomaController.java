package com.luisrdm.firebaseandsocialloginentregable.controller;

import com.luisrdm.firebaseandsocialloginentregable.dao.ArtistDao;
import com.luisrdm.firebaseandsocialloginentregable.model.Artist;
import com.luisrdm.firebaseandsocialloginentregable.util.ResultListener;

import java.util.List;

/**
 * Created by digitalhouse on 20/07/16.
 */
public class MomaController {

    public void getArtists(final ResultListener<List<Artist>> listener){
        ArtistDao artistDao = new ArtistDao();
        artistDao.getArtistsFromFireBase(new ResultListener<List<Artist>>() {
            @Override
            public void finish(List<Artist> resultado) {
                listener.finish(resultado);
            }
        });
    }

}
