package com.eg.typicaldesign.moaheb.models;

/**
 * Created by MAGIC on 4/18/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Years {

    @SerializedName("y")
    @Expose
    private int y;
    @SerializedName("m")
    @Expose
    private int m;
    @SerializedName("d")
    @Expose
    private int d;
    @SerializedName("h")
    @Expose
    private int h;
    @SerializedName("i")
    @Expose
    private int i;
    @SerializedName("s")
    @Expose
    private int s;
    @SerializedName("f")
    @Expose
    private double f;
    @SerializedName("weekday")
    @Expose
    private int weekday;
    @SerializedName("weekday_behavior")
    @Expose
    private int weekdayBehavior;
    @SerializedName("first_last_day_of")
    @Expose
    private int firstLastDayOf;
    @SerializedName("invert")
    @Expose
    private int invert;
    @SerializedName("days")
    @Expose
    private int days;
    @SerializedName("special_type")
    @Expose
    private int specialType;
    @SerializedName("special_amount")
    @Expose
    private int specialAmount;
    @SerializedName("have_weekday_relative")
    @Expose
    private int haveWeekdayRelative;
    @SerializedName("have_special_relative")
    @Expose
    private int haveSpecialRelative;

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public int getWeekdayBehavior() {
        return weekdayBehavior;
    }

    public void setWeekdayBehavior(int weekdayBehavior) {
        this.weekdayBehavior = weekdayBehavior;
    }

    public int getFirstLastDayOf() {
        return firstLastDayOf;
    }

    public void setFirstLastDayOf(int firstLastDayOf) {
        this.firstLastDayOf = firstLastDayOf;
    }

    public int getInvert() {
        return invert;
    }

    public void setInvert(int invert) {
        this.invert = invert;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getSpecialType() {
        return specialType;
    }

    public void setSpecialType(int specialType) {
        this.specialType = specialType;
    }

    public int getSpecialAmount() {
        return specialAmount;
    }

    public void setSpecialAmount(int specialAmount) {
        this.specialAmount = specialAmount;
    }

    public int getHaveWeekdayRelative() {
        return haveWeekdayRelative;
    }

    public void setHaveWeekdayRelative(int haveWeekdayRelative) {
        this.haveWeekdayRelative = haveWeekdayRelative;
    }

    public int getHaveSpecialRelative() {
        return haveSpecialRelative;
    }

    public void setHaveSpecialRelative(int haveSpecialRelative) {
        this.haveSpecialRelative = haveSpecialRelative;
    }

}