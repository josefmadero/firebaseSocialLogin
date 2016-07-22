package com.luisrdm.firebaseandsocialloginentregable.view;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.luisrdm.firebaseandsocialloginentregable.R;
import com.luisrdm.firebaseandsocialloginentregable.controller.MomaController;
import com.luisrdm.firebaseandsocialloginentregable.model.Painting;
import com.luisrdm.firebaseandsocialloginentregable.util.ResultListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by digitalhouse on 20/07/16.
 */
public class PaintingsAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private List<Painting> paintingsList;
    private View.OnClickListener listener;
    private static Context context;

    public PaintingsAdapter(List<Painting> paintingList, Context context) {
        this.context = context;
        this.paintingsList = paintingList;
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
        PaintingsViewHolder paintingsViewHolder = (PaintingsViewHolder) holder;
        paintingsViewHolder.bindPainting(painting);
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

        ImageView paintingImage;

        public PaintingsViewHolder(View itemView) {
            super(itemView);
            paintingImage = (ImageView) itemView.findViewById(R.id.imageView_recyclerViewDetail_painting_home);
        }

        public void bindPainting(Painting actualPainting){
            MomaController momaController = new MomaController();
            momaController.getPainting(actualPainting.getImage(), context, new ResultListener<Uri>() {
                @Override
                public void finish(Uri resultado) {
                    Picasso.with(context).load(resultado).into(paintingImage);
                }
            });
            //TODO obtener imagen
        }
    }
}
