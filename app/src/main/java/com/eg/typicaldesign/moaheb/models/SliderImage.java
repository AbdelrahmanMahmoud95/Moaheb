package com.eg.typicaldesign.moaheb.models;

/**
 * Created by MAGIC on 5/30/2018.
 */
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SliderImage {

    @SerializedName("slider")
    @Expose
    private List<Slider> slider = null;

    public List<Slider> getSlider() {
        return slider;
    }

    public void setSlider(List<Slider> slider) {
        this.slider = slider;
    }

}