package com.luisrdm.firebaseandsocialloginentregable.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.luisrdm.firebaseandsocialloginentregable.R;
import com.squareup.picasso.Picasso;

public class Activity_detail_artist_and_painting extends AppCompatActivity {

    public static final String PAINTINGNAME = "PaintingName";
    public static final String PAINTINGURI = "PaintingUri";
    public static final String ARTISTNAME = "ArtistName";

    private CallbackManager callbackManager;
    ShareButton shareButton;
    private String imageURL;
    private String artist;
    private String painting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_detail_artist_and_painting);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        ImageView paintingImage = (ImageView) findViewById(R.id.imageView_activityDetail_painting);
        TextView artistName = (TextView) findViewById(R.id.textView_activityDetail_artistName);
        TextView paintingName = (TextView) findViewById(R.id.textView_activityDetail_paintingName);

        imageURL = bundle.getString(PAINTINGURI);
        Picasso.with(this).load(imageURL).into(paintingImage);
        artist = bundle.getString(ARTISTNAME);
        artistName.setText(artist);
        painting = bundle.getString(PAINTINGNAME);
        paintingName.setText(painting);

        callbackManager = CallbackManager.Factory.create();
        shareButton = (ShareButton)findViewById(R.id.fb_share_button);

        configureShareButton();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void configureShareButton(){
        //Aca seteamos el contenido que queremos mostrar
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle("Hello MOMA Facebook")
                .setContentDescription("Artist: " + artist +" | Painting: " + painting)
                .setImageUrl(Uri.parse(imageURL))
                .build();

        //Aca asociamos el contenido que queremos mostrar al share button
        shareButton.setShareContent(linkContent);

    }



}
