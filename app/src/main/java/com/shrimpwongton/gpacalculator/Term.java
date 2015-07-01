package com.shrimpwongton.gpacalculator;

import java.util.ArrayList;

/**
 * Created by anthonywong on 6/29/15.
 */
public class Term {

    private int id;
    private String term;
    private double GPA;
    private ArrayList<Class> classes;

    public Term() {

    }
    public Term(String term) {
        this.term = term;
        classes = new ArrayList<Class>();
    }

    public Term(int id, String term, double GPA) {
        this.GPA = GPA;
        this.id = id;
        this.term = term;
    }

    public Term(String term, double GPA) {
        this.GPA = GPA;
        this.term = term;
    }

    public String getTerm() {
        return term;
    }

    public ArrayList<Class> getClasses() {
        return classes;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void addClass(Class newClass) {
        classes.add(newClass);
    }

    public void deleteClass() {
        classes.remove(classes.size() -1);
    }

    public double getGPA() {
        return GPA;
    }

    public void setGPA(double GPA) {
        this.GPA = GPA;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Calculate the GPA
    public void calculateGPA () { double total = 0.0; for ( Class c : classes ) { total += c.getUnits(); }}
    @Override
    public String toString() {
        return term + "                 " + GPA;
    }
}
