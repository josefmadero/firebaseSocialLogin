package com.luisrdm.firebaseandsocialloginentregable.view;

import android.content.Context;
import android.location.GpsStatus;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.luisrdm.firebaseandsocialloginentregable.R;
import com.luisrdm.firebaseandsocialloginentregable.model.Painting;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Painting> paintingList;
    private Context context;
    private RecyclerView recyclerViewHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = this;

        paintingList = new ArrayList<>();
        //Pido al controller la lista de pinturas

        recyclerViewHome = (RecyclerView) findViewById(R.id.recyclerView_activity_main);
        recyclerViewHome.setHasFixedSize(true);
        recyclerViewHome.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        PaintingsAdapter paintingsAdapter = new PaintingsAdapter(paintingList);

        PaintingListener paintingListener = new PaintingListener();
        paintingsAdapter.setOnClickListener(paintingListener);
    }

    private class PaintingListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Apretamos el elemento (onClickView del main) " + recyclerViewHome.getChildPosition(v), Toast.LENGTH_SHORT).show();
        }
    }
}
