package com.luisrdm.firebaseandsocialloginentregable.model;

/**
 * Created by digitalhouse on 20/07/16.
 */
public class Painting {
    private String image;
    private String name;

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


    @Override
    public String toString() {
        return "Painting{" +
                "image=" + image +
                ", name='" + name + '\'' +
                '}';
    }
}
