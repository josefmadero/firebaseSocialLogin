package com.luisrdm.firebaseandsocialloginentregable.model;

import java.util.List;

/**
 * Created by digitalhouse on 20/07/16.
 */
public class Painting {
    private Integer singlePaintingID;
    private String painterName;

    public Integer getSinglePaintingID() {
        return singlePaintingID;
    }

    public void setSinglePaintingID(Integer singlePaintingID) {
        this.singlePaintingID = singlePaintingID;
    }

    public String getPainterName() {
        return painterName;
    }

    public void setPainterName(String painterName) {
        this.painterName = painterName;
    }
}
