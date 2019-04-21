package com.eg.typicaldesign.moaheb.models;

/**
 * Created by MAGIC on 4/11/2018.
 */


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Governorates {

    @SerializedName("governorates")
    @Expose
    private List<Governorate> governorates = null;

    public List<Governorate> getGovernorates() {
        return governorates;
    }

    public void setGovernorates(List<Governorate> governorates) {
        this.governorates = governorates;
    }

}
