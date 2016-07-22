package com.luisrdm.firebaseandsocialloginentregable.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.luisrdm.firebaseandsocialloginentregable.R;
import com.luisrdm.firebaseandsocialloginentregable.controller.MomaController;
import com.luisrdm.firebaseandsocialloginentregable.model.Artist;
import com.luisrdm.firebaseandsocialloginentregable.model.Painting;
import com.luisrdm.firebaseandsocialloginentregable.util.ResultListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Painting> paintingList;
    private List<Artist> artistList;
    private Context context;
    private RecyclerView recyclerViewHome;
    private Boolean haveArtists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = this;

        paintingList = new ArrayList<>();
        final MomaController momaController = new MomaController();
        momaController.getArtists(new ResultListener<List<Artist>>() {
            @Override
            public void finish(List<Artist> resultado) {
                artistList = resultado;
                populatePaintingsList(resultado);
                haveArtists = true;

                Toast.makeText(context, "Tengo los artistas!", Toast.LENGTH_SHORT).show();

                startRecyclerView();
            }
        });


    }

    private void startRecyclerView (){
        recyclerViewHome = (RecyclerView) findViewById(R.id.recyclerView_activity_main);
        recyclerViewHome.setHasFixedSize(true);
        recyclerViewHome.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        PaintingsAdapter paintingsAdapter = new PaintingsAdapter(paintingList);

        recyclerViewHome.setAdapter(paintingsAdapter);

        PaintingListener paintingListener = new PaintingListener();
        paintingsAdapter.setOnClickListener(paintingListener);
    }

    private class PaintingListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Apretamos el elemento (onClickView del main) " + recyclerViewHome.getChildPosition(v), Toast.LENGTH_SHORT).show();
        }
    }

    public void populatePaintingsList (List<Artist> artistList){
        paintingList = new ArrayList<>();

        for (Artist actualArtist : artistList) {
            paintingList.addAll(actualArtist.getPaints());
        }
    }
}
