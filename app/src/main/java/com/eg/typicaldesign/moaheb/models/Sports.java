package com.eg.typicaldesign.moaheb.models;
/**
 * Created by MAGIC on 4/8/2018.
 */

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sports {

    @SerializedName("sports")
    @Expose
    private List<Sport> sports=null;

    public List<Sport> getSports() {
        return sports;
    }

    public void setSports(List<Sport> sports) {
        this.sports = sports;
    }

}
