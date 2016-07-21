package com.luisrdm.firebaseandsocialloginentregable.controller;

import com.luisrdm.firebaseandsocialloginentregable.dao.ArtistDao;
import com.luisrdm.firebaseandsocialloginentregable.model.Artist;
import com.luisrdm.firebaseandsocialloginentregable.model.Painting;
import com.luisrdm.firebaseandsocialloginentregable.util.ResultListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by digitalhouse on 20/07/16.
 */
public class MomaController {

    private List<Painting> paintingsList = null;

    public void getArtists(final ResultListener<List<Artist>> listener){
        ArtistDao artistDao = new ArtistDao();
        artistDao.getArtistsFromFireBase(new ResultListener<List<Artist>>() {
            @Override
            public void finish(List<Artist> resultado) {
                populatePaintingsList(resultado);
                listener.finish(resultado);
            }
        });
    }

    public void populatePaintingsList (List<Artist> artistList){
        paintingsList = new ArrayList<>();

        for (Artist actualArtist : artistList) {
            paintingsList.addAll(actualArtist.getPaints());
        }
    }

    public List<Painting> getPaintingsList() {
        return paintingsList;
    }
}
