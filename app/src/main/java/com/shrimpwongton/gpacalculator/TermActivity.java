package com.shrimpwongton.gpacalculator;

import android.app.Activity;
import android.content.Intent;
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


public class TermActivity extends ActionBarActivity {

    EditText termText, class1Name, class1Unit, class2Name, class2Unit, class3Name, class3Unit, class4Name, class4Unit, class5Name, class5Unit, class6Name, class6Unit;
    Spinner class1Grade, class2Grade, class3Grade, class4Grade, class5Grade, class6Grade;
    FloatingActionButton fab;
    GPADatabase database = new GPADatabase(this);
    MenuItem delete;
    String termName;
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
        final Bundle extras = getIntent().getExtras();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (extras != null) {
            int id = extras.getInt("ID");
            termText.setText(database.getTerm(id).getTerm());
            fab.setImageResource(R.drawable.ic_save_white_24dp);
            termText.setSelection(termText.getText().length());
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( extras == null) {
                    termName = termText.getText().toString();
                    if (termName.matches("")) {
                        termText.setError("Term name cannot be empty");
                    } else {
                        Term t = new Term(termName);
                        database.addTerm(t);
                        t.setId(database.getTermCount());
                        database.close();
                        finish();
                    }
                }
                else
                {
                    termName = termText.getText().toString();
                    if ( termName.matches("")) {
                        termText.setError("Term name cannot be empty");
                    }
                    else {
                        Term t = (Term)getIntent().getSerializableExtra("TERM");
                        t.setTerm(termName);
                        database.updateTerm(t);
                        database.close();
                        finish();
                    }
                }

            }
        });
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
                Term t = (Term) getIntent().getSerializableExtra("TERM");
                database.deleteTerm(t);
            }
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
