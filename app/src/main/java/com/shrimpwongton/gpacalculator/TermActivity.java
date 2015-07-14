package com.shrimpwongton.gpacalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class TermActivity extends ActionBarActivity {

    EditText termText, class1Name, class1Unit, class2Name, class2Unit, class3Name, class3Unit, class4Name, class4Unit, class5Name, class5Unit, class6Name, class6Unit;
    Spinner class1Grade, class2Grade, class3Grade, class4Grade, class5Grade, class6Grade;
    FloatingActionButton fab;
    GPADatabase database = new GPADatabase(this);
    ClassDatabase claDatabase = new ClassDatabase(this);
    MenuItem delete;
    String termName, class1NameText, class2NameText, class3NameText, class4NameText, class5NameText, class6NameText, class1UnitText, class2UnitText, class3UnitText, class4UnitText, class5UnitText, class6UnitText;
    double c1g, c2g, c3g, c4g, c5g, c6g;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setTitle("");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);
    }

    @Override
    protected void onResume() {
        super.onResume();
        termText = (EditText) findViewById(R.id.editTerm);
        class1Name = (EditText) findViewById(R.id.class1Class);
        class1Unit = (EditText) findViewById(R.id.class1Unit);
        class1Grade = (Spinner) findViewById(R.id.class1Grade);
        class2Name = (EditText) findViewById(R.id.class2Class);
        class2Unit = (EditText) findViewById(R.id.class2Unit);
        class2Grade = (Spinner) findViewById(R.id.class2Grade);
        class3Name = (EditText) findViewById(R.id.class3Class);
        class3Unit = (EditText) findViewById(R.id.class3Unit);
        class3Grade = (Spinner) findViewById(R.id.class3Grade);
        class4Name = (EditText) findViewById(R.id.class4Class);
        class4Unit = (EditText) findViewById(R.id.class4Unit);
        class4Grade = (Spinner) findViewById(R.id.class4Grade);
        class5Name = (EditText) findViewById(R.id.class5Class);
        class5Unit = (EditText) findViewById(R.id.class5Unit);
        class5Grade = (Spinner) findViewById(R.id.class5Grade);
        class6Name = (EditText) findViewById(R.id.class6Class);
        class6Unit = (EditText) findViewById(R.id.class6Unit);
        class6Grade = (Spinner) findViewById(R.id.class6Grade);
        final Bundle extras = getIntent().getExtras();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (extras != null) {
            Term t = (Term)getIntent().getSerializableExtra("TERM");
            int id = extras.getInt("ID");
            termText.setText(database.getTerm(id).getTerm());
            fab.setImageResource(R.drawable.ic_save_white_24dp);
            termText.setSelection(termText.getText().length());
            restoreClasses((ArrayList)claDatabase.getAllClassesWithParentID(id));
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                termName = termText.getText().toString();
                class1NameText = class1Name.getText().toString();
                class2NameText = class2Name.getText().toString();
                class3NameText = class3Name.getText().toString();
                class4NameText = class4Name.getText().toString();
                class5NameText = class5Name.getText().toString();
                class6NameText = class6Name.getText().toString();
                class1UnitText = class1Unit.getText().toString();
                class2UnitText = class2Unit.getText().toString();
                class3UnitText = class3Unit.getText().toString();
                class4UnitText = class4Unit.getText().toString();
                class5UnitText = class5Unit.getText().toString();
                class6UnitText = class6Unit.getText().toString();
                c1g = toDouble(class1Grade.getSelectedItem().toString());
                c2g = toDouble(class2Grade.getSelectedItem().toString());
                c3g = toDouble(class3Grade.getSelectedItem().toString());
                c4g = toDouble(class4Grade.getSelectedItem().toString());
                c5g = toDouble(class5Grade.getSelectedItem().toString());
                c6g = toDouble(class6Grade.getSelectedItem().toString());
                if ( extras == null) {
                    if (termName.matches("")) {
                        termText.setError("Please add a term name.");
                    }
                    else if (
                            !((!class1NameText.matches("") && !class1UnitText.matches("")) ||
                                    (!class2NameText.matches("") && !class2UnitText.matches("")) ||
                                    (!class3NameText.matches("") && !class3UnitText.matches("")) ||
                                    (!class4NameText.matches("") && !class4UnitText.matches("")) ||
                                    (!class5NameText.matches("") && !class5UnitText.matches("")) ||
                                    (!class6NameText.matches("") && !class6UnitText.matches("")))
                            ) {
                        Toast.makeText(TermActivity.this, "Please add at least 1 class", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Term t = new Term(termName);
                        database.addTerm(t);
                        t.setId((int) database.getLastID());
                        database.close();
                        addClass(t);
                        claDatabase.close();
                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                            finishAfterTransition();
                        else
                            finish();
                    }
                }
                else
                {
                    termName = termText.getText().toString();
                    if ( termName.matches("")) {
                        termText.setError("Please add a term name.");
                    }
                    else if (!((!class1NameText.matches("") && !class1UnitText.matches("")) ||
                            (!class2NameText.matches("") && !class2UnitText.matches("")) ||
                            (!class3NameText.matches("") && !class3UnitText.matches("")) ||
                            (!class4NameText.matches("") && !class4UnitText.matches("")) ||
                            (!class5NameText.matches("") && !class5UnitText.matches("")) ||
                            (!class6NameText.matches("") && !class6UnitText.matches("")))) {
                        Toast.makeText(TermActivity.this, "Please add at least 1 class.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Term t = (Term)getIntent().getSerializableExtra("TERM");
                        t.setTerm(termName);
                        database.updateTerm(t);
                        database.close();
                        updateClass(t.getId());
                        claDatabase.close();
                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                            finishAfterTransition();
                        else
                            finish();
                    }
                }

            }
        });
    }

    public void restoreClasses(ArrayList<Class> classList) {
        int i = classList.size();
        if ( i >= 1 ) {
            class1Name.setText(classList.get(0).getClassName());
            class1Unit.setText(Double.toString(classList.get(0).getUnits()));
            class1Grade.setSelection(toPosition(classList.get(0).getGrade()));
        }
        if ( i >= 2 ) {
            class2Name.setText(classList.get(1).getClassName());
            class2Unit.setText(Double.toString(classList.get(1).getUnits()));
            class2Grade.setSelection(toPosition(classList.get(1).getGrade()));
        }
        if ( i >= 3 ) {
            class3Name.setText(classList.get(2).getClassName());
            class3Unit.setText(Double.toString(classList.get(2).getUnits()));
            class3Grade.setSelection(toPosition(classList.get(2).getGrade()));
        }
        if ( i >= 4 ) {
            class4Name.setText(classList.get(3).getClassName());
            class4Unit.setText(Double.toString(classList.get(3).getUnits()));
            class4Grade.setSelection(toPosition(classList.get(3).getGrade()));
        }
        if ( i >= 5 ) {
            class5Name.setText(classList.get(4).getClassName());
            class5Unit.setText(Double.toString(classList.get(4).getUnits()));
            class5Grade.setSelection(toPosition(classList.get(4).getGrade()));
        }
        if ( i >= 6 ) {
            class6Name.setText(classList.get(5).getClassName());
            class6Unit.setText(Double.toString(classList.get(5).getUnits()));
            class6Grade.setSelection(toPosition(classList.get(5).getGrade()));
        }
    }

    public void updateClass(int i) {
        ArrayList<Class> classes = (ArrayList)claDatabase.getAllClassesWithParentID(i);
        int j = classes.size();
        if ( !class1NameText.matches("") && !class1UnitText.matches("") && j >= 1 ) {
            Class c = classes.get(0);
            c.setClassName(class1NameText);
            c.setUnits(Double.parseDouble(class1UnitText));
            c.setGrade(c1g);
            claDatabase.updateClass(c);
        }
        else if ( !class1NameText.matches("") && !class1UnitText.matches("") ) {
            Class c = new Class(i, class1NameText, Double.parseDouble(class1UnitText), c1g);
            claDatabase.addClass(c);
        }
        if ( !class2NameText.matches("") && !class2UnitText.matches("") && j >= 2) {
            Class c = classes.get(1);
            c.setClassName(class2NameText);
            c.setUnits(Double.parseDouble(class2UnitText));
            c.setGrade(c2g);
            claDatabase.updateClass(c);
        }
        else if (!class2NameText.matches("") && !class2UnitText.matches("")){
            Class c = new Class(i, class2NameText, Double.parseDouble(class2UnitText), c2g);
            claDatabase.addClass(c);
        }
        if ( !class3NameText.matches("") && !class3UnitText.matches("") && j >= 3) {
            Class c = classes.get(2);
            c.setClassName(class3NameText);
            c.setUnits(Double.parseDouble(class3UnitText));
            c.setGrade(c3g);
            claDatabase.updateClass(c);
        }
        else if (!class3NameText.matches("") && !class3UnitText.matches("")) {
            Class c = new Class(i, class3NameText, Double.parseDouble(class3UnitText), c3g);
            claDatabase.addClass(c);
        }
        if ( !class4NameText.matches("") && !class4UnitText.matches("") && j >= 4) {
            Class c = classes.get(3);
            c.setClassName(class4NameText);
            c.setUnits(Double.parseDouble(class4UnitText));
            c.setGrade(c4g);
            claDatabase.updateClass(c);
        }
        else if (!class4NameText.matches("") && !class4UnitText.matches("")) {
            Class c = new Class(i, class4NameText, Double.parseDouble(class4UnitText), c4g);
            claDatabase.addClass(c);
        }
        if ( !class5NameText.matches("") && !class5UnitText.matches("") && j >= 5) {
            Class c = classes.get(4);
            c.setClassName(class5NameText);
            c.setUnits(Double.parseDouble(class5UnitText));
            c.setGrade(c5g);
            claDatabase.updateClass(c);
        }
        else if (!class5NameText.matches("") && !class5UnitText.matches("")) {
            Class c = new Class(i, class5NameText, Double.parseDouble(class5UnitText), c5g);
            claDatabase.addClass(c);
        }
        if ( !class6NameText.matches("") && !class6UnitText.matches("") && j >= 6) {
            Class c = classes.get(5);
            c.setClassName(class6NameText);
            c.setUnits(Double.parseDouble(class6UnitText));
            c.setGrade(c6g);
            claDatabase.updateClass(c);
        }
        else if (!class6NameText.matches("") && !class6UnitText.matches("")) {
            Class c = new Class(i, class6NameText, Double.parseDouble(class6UnitText), c6g);
            claDatabase.addClass(c);
        }
    }

    public void addClass (Term t) {
        int i = t.getId();
        if ( !class1NameText.matches("") && !class1UnitText.matches("") ) {
            Class c = new Class(i, class1NameText, Double.parseDouble(class1UnitText), c1g);
            claDatabase.addClass(c);
        }
        if ( !class2NameText.matches("") && !class2UnitText.matches("") ) {
            Class c = new Class(i, class2NameText, Double.parseDouble(class2UnitText), c2g);
            claDatabase.addClass(c);
        }
        if ( !class3NameText.matches("") && !class3UnitText.matches("") ) {
            Class c = new Class(i, class3NameText, Double.parseDouble(class3UnitText), c3g);
            claDatabase.addClass(c);
        }
        if ( !class4NameText.matches("") && !class4UnitText.matches("") ) {
            Class c = new Class(i, class4NameText, Double.parseDouble(class4UnitText), c4g);
            claDatabase.addClass(c);
        }
        if ( !class5NameText.matches("") && !class5UnitText.matches("") ) {
            Class c = new Class(i, class5NameText, Double.parseDouble(class5UnitText), c5g);
            claDatabase.addClass(c);
        }
        if ( !class6NameText.matches("") && !class6UnitText.matches("") ) {
            Class c = new Class(i, class6NameText, Double.parseDouble(class6UnitText), c6g);
            claDatabase.addClass(c);
        }
    }

    public int toPosition(Double d) {
        if ( d == 4.001 ) return 0;
        else if ( d == 4.0 ) return 1;
        else if ( d == 3.7 ) return 2;
        else if ( d == 3.3 ) return 3;
        else if ( d == 3.0 ) return 4;
        else if ( d == 2.7 ) return 5;
        else if ( d == 2.3 ) return 6;
        else if ( d == 2.0 ) return 7;
        else if ( d == 1.7 ) return 8;
        else if ( d == 1.3 ) return 9;
        else if ( d == 1.0 ) return 10;
        else if ( d == 0.7 ) return 11;
        else return 12;
    }
    public double toDouble(String s) {
        switch (s) {
            case "A+": return 4.001;
            case "A": return 4.0;
            case "A-": return 3.7;
            case "B+": return 3.3;
            case "B": return 3.0;
            case "B-": return 2.7;
            case "C+": return 2.3;
            case "C": return 2.0;
            case "C-": return 1.7;
            case "D+": return 1.3;
            case "D": return 1.0;
            case "D-": return 0.7;
            case "F": return 0.0;
        }
        return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_term, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Delete the Term, go back to main activity.
        if (id == R.id.action_delete) {
            Bundle extras = getIntent().getExtras();
            if ( extras != null ) {
                supportFinishAfterTransition();
                Term t = (Term) getIntent().getSerializableExtra("TERM");
                database.deleteTerm(t);
                ArrayList<Class> a = (ArrayList)claDatabase.getAllClassesWithParentID(t.getId());
                for ( Class b: a) {
                    claDatabase.deleteClass(b);
                }
            }
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                finishAfterTransition();
            else
                finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
