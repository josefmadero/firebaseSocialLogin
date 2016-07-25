package com.luisrdm.firebaseandsocialloginentregable.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.luisrdm.firebaseandsocialloginentregable.R;
import com.luisrdm.firebaseandsocialloginentregable.controller.MomaController;
import com.luisrdm.firebaseandsocialloginentregable.model.Artist;
import com.luisrdm.firebaseandsocialloginentregable.model.Painting;
import com.luisrdm.firebaseandsocialloginentregable.util.ResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Painting> paintingList;
    private List<Artist> artistList;
    private Context context;
    private RecyclerView recyclerViewHome;
    private MomaController momaController = new MomaController();
    private Bundle bundle;
    private Intent intent;
    private CallbackManager callbackManager;
    private GridLayoutManager gridLayout;
    LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);

        this.context = this;

        paintingList = new ArrayList<>();
        momaController.getArtists(new ResultListener<List<Artist>>() {
            @Override
            public void finish(List<Artist> resultado) {
                artistList = resultado;
                populatePaintingsList(resultado);

                Toast.makeText(context, "Tengo los artistas!", Toast.LENGTH_SHORT).show();

                startRecyclerView();
            }
        });

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.facebook_login_button);

        if(AccessToken.getCurrentAccessToken() != null){
            request();
        }

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(context, "FB Login Success", Toast.LENGTH_SHORT).show();
                request();
            }

            @Override
            public void onCancel() {
                Toast.makeText(context, "FB Login Cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(context, "FB Login Error", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void startRecyclerView (){

        recyclerViewHome = (RecyclerView) findViewById(R.id.recyclerView_activity_main);
        recyclerViewHome.setHasFixedSize(true);
        recyclerViewHome.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        PaintingsAdapter paintingsAdapter = new PaintingsAdapter(paintingList, artistList, context);

        recyclerViewHome.setAdapter(paintingsAdapter);

        PaintingListener paintingListener = new PaintingListener();
        paintingsAdapter.setOnClickListener(paintingListener);
    }

    private class PaintingListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Apretamos el elemento (onClickView del main) " + recyclerViewHome.getChildPosition(v), Toast.LENGTH_SHORT).show();
            intent = new Intent(context, Activity_detail_artist_and_painting.class);
            bundle = new Bundle();

            Painting actualPaint = paintingList.get(recyclerViewHome.getChildPosition(v));
            Artist actualArtist = new Artist();

            bundle.putString(Activity_detail_artist_and_painting.PAINTINGNAME, actualPaint.getName());

            for (Artist artist : artistList) {
                for (Painting artistPainting : artist.getPaints()) {
                    if (artistPainting.getImage().equals(actualPaint.getImage())) {
                        actualArtist = artist;
                        break;
                    }
                }
            }

            bundle.putString(Activity_detail_artist_and_painting.ARTISTNAME,actualArtist.getName() );

            momaController.getPainting(actualPaint.getImage(), context, new ResultListener<Uri> () {
                @Override
                public void finish(Uri resultado) {
                    bundle.putString(Activity_detail_artist_and_painting.PAINTINGURI, String.valueOf(resultado));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });


        }
    }

    public void populatePaintingsList (List<Artist> artistList){
        paintingList = new ArrayList<>();

        for (Artist actualArtist : artistList) {
            paintingList.addAll(actualArtist.getPaints());
        }
    }

    public void loginToSelfie (View view){
        Toast.makeText(MainActivity.this, "Click en button login.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(view.getContext(), ActivitySelfie.class);
        startActivity(intent);
    }


    private void request() {
        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        JSONObject jsonObject = response.getJSONObject();


                        try {
                            String name = jsonObject.getString("name");
                            JSONObject jsonObjectPicture = jsonObject.getJSONObject("picture");
                            JSONObject jsonObjectData = jsonObjectPicture.getJSONObject("data");
                            String imageUrl = jsonObjectData.getString("url");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
