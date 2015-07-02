package com.shrimpwongton.gpacalculator;

/**
 * Created by anthonywong on 6/29/15.
 */
public class Class {

    private String className;
    private double units;
    private int parentID, id;
    private double grade;

    public Class () {};
    public Class(int id, int parentID, String className, double units, double grade) {
        this.id = id;
        this.className = className;
        this.units = units;
        this.parentID = parentID;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getUnits() {
        return units;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setUnits(double units) {
        this.units = units;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}
