package com.luisrdm.firebaseandsocialloginentregable.controller;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.luisrdm.firebaseandsocialloginentregable.dao.ArtistDao;
import com.luisrdm.firebaseandsocialloginentregable.dao.PaintingDao;
import com.luisrdm.firebaseandsocialloginentregable.model.Artist;
import com.luisrdm.firebaseandsocialloginentregable.model.Painting;
import com.luisrdm.firebaseandsocialloginentregable.util.ResultListener;
import com.squareup.picasso.Picasso;

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

    public void getPainting(final Painting actualPainting, Context context, final ResultListener listener){
        if(actualPainting.getUri()==null) {
            paintingDao.getPaintingFromFirebase(actualPainting.getImage(), context, new ResultListener<Uri>() {
                @Override
                public void finish(Uri resultado) {
                    listener.finish(resultado);
                    actualPainting.setUri(resultado);
                }
            });
        } else {
            listener.finish(actualPainting.getUri());
        }

    }

    public void setUrlOfPaintingIfNull(final Painting actualPainting, final Context context, final ImageView paintingImage){
        if(actualPainting.getUri()== null ){
            getPainting(actualPainting, context, new ResultListener<Uri>() {
                @Override
                public void finish(Uri resultado) {
                    actualPainting.setUri(resultado);
                    Picasso.with(context).load(resultado).into(paintingImage);
                }
            });
        } else {
            Picasso.with(context).load(actualPainting.getUri()).into(paintingImage);
        }

    }

}
