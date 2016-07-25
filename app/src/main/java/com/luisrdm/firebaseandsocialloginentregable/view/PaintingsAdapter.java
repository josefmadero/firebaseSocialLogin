package com.luisrdm.firebaseandsocialloginentregable.view;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.luisrdm.firebaseandsocialloginentregable.R;
import com.luisrdm.firebaseandsocialloginentregable.controller.MomaController;
import com.luisrdm.firebaseandsocialloginentregable.model.Artist;
import com.luisrdm.firebaseandsocialloginentregable.model.Painting;
import com.luisrdm.firebaseandsocialloginentregable.util.ResultListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by digitalhouse on 20/07/16.
 */
public class PaintingsAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private List<Painting> paintingsList;
    private List<Artist> artistList;
    private View.OnClickListener listener;
    private static Context context;
    private static ImageView paintingImage;
    private static TextView paintingName;
    private static TextView artistName;

    public PaintingsAdapter(List<Painting> paintingList, List<Artist> artistList, Context context) {
        this.context = context;
        this.paintingsList = paintingList;
        this.artistList = artistList;
    }

    public void setOnClickListener(View.OnClickListener unListener) {
        listener = unListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_detail_home, parent, false);
        PaintingsViewHolder paintingsViewHolder = new PaintingsViewHolder(itemView);
        itemView.setOnClickListener(this);

        return paintingsViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Painting painting = paintingsList.get(position);
        Artist artist = new Artist();
        for (Artist actualArtist : artistList) {
            if(actualArtist.getPaints().contains(painting)){
                artist = actualArtist;
                break;
            }
        }

        PaintingsViewHolder paintingsViewHolder = (PaintingsViewHolder) holder;
        paintingsViewHolder.bindPainting(artist, painting);
    }

    @Override
    public int getItemCount() {
        return paintingsList.size();
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v);
    }

    private static class PaintingsViewHolder extends RecyclerView.ViewHolder{



        public PaintingsViewHolder(View itemView) {
            super(itemView);
            paintingImage = (ImageView) itemView.findViewById(R.id.imageView_recyclerViewDetail_painting_home);
            paintingName = (TextView) itemView.findViewById(R.id.textView_recyclerViewDetail_paintingName);
            artistName = (TextView) itemView.findViewById(R.id.textView_recyclerViewDetail_artistName);

        }

        public void bindPainting(Artist actualArtist, Painting actualPainting){

            artistName.setText(actualArtist.getName());
            paintingName.setText(actualPainting.getName());

            MomaController momaController = new MomaController();
            momaController.getPainting(actualPainting.getImage(), context, new ResultListener<Uri>() {
                @Override
                public void finish(Uri resultado) {
                    Picasso.with(context).load(resultado).into(paintingImage);
                }
            });
        }
    }
}
