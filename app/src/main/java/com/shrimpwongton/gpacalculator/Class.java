package com.shrimpwongton.gpacalculator;

/**
 * Created by anthonywong on 6/29/15.
 */
public class Class {

    private String className;
    private double units;
    private double unitWeighted;
    public enum Grades {
        APLUS,AREG,AMINUS,BPLUS,BREG,BMINUS,CPLUS,CREG,CMINUS,DPLUS,DREG,DMINUS,FREG
    }
    Grades grades;

    public double getUnits() {
        return units;
    }

    public double getUnitWeighted() {
        return unitWeighted;
    }

    public void setUnitWeighted(double unitWeighted) {
        this.unitWeighted = unitWeighted;
    }
}
