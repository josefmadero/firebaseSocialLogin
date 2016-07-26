package com.luisrdm.firebaseandsocialloginentregable.model;

import android.net.Uri;

import java.net.URL;

/**
 * Created by digitalhouse on 20/07/16.
 */
public class Painting {
    private String image;
    private String name;
    private Uri imageUri = null;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getUri() {
        return imageUri;
    }

    public void setUri(Uri uri) {
        this.imageUri = uri;
    }

    @Override
    public String toString() {
        return "Painting{" +
                "image=" + image +
                ", name='" + name + '\'' +
                '}';
    }
}
