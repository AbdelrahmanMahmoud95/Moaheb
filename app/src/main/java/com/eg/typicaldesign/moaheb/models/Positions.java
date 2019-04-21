

/**
 * Created by MAGIC on 4/10/2018.
 */
package com.eg.typicaldesign.moaheb.models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Positions {

    @SerializedName("positions")
    @Expose
    private List<Position> positions = null;

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

}