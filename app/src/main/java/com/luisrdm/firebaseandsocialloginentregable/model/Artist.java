package com.luisrdm.firebaseandsocialloginentregable.model;

import java.util.List;

/**
 * Created by digitalhouse on 20/07/16.
 */
public class Artist {
    private String name;
    private List<Painting> paints;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Painting> getPaints() {
        return paints;
    }

    public void setPaints(List<Painting> paints) {
        this.paints = paints;
    }
}
