package com.shrimpwongton.gpacalculator;

import java.util.ArrayList;

/**
 * Created by anthonywong on 6/29/15.
 */
public class Term {

    private String term;
    private ArrayList<Class> classes;

    public Term(String term) {
        this.term = term;
        classes = new ArrayList<Class>();
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

    @Override
    public String toString() {
        return "Term{" +
                "term='" + term + '\'' +
                '}';
    }
}
