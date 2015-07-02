package com.shrimpwongton.gpacalculator;

import android.app.Application;
import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class GPAActivity extends ActionBarActivity {

    ListView listTerms;
    TextView textView;
    FloatingActionButton fab;
    GPADatabase database = new GPADatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("GPA Calculator");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpa);
        updateList();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GPAActivity.this, TermActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
        listTerms.setClickable(true);
        listTerms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Term t = (Term)listTerms.getItemAtPosition(position);
                Intent i = new Intent(GPAActivity.this, TermActivity.class);
                i.putExtra("ID", t.getId());
                i.putExtra("TERM", t);
                startActivity(i);
            }
        });
    }

    public void updateList() {
        listTerms = (ListView) findViewById(R.id.listTerms);
        textView = (TextView) findViewById(R.id.noTermString);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToListView(listTerms);
        if ( database.getTermCount() != 0 ) {
            database.close();
            textView.setVisibility(View.INVISIBLE);
            ArrayList<Term> terms = (ArrayList) database.getAllTerms();
            ArrayAdapter<Term> adapter = new ArrayAdapter<Term>(GPAActivity.this, R.layout.list_item, terms);
            listTerms.setAdapter(adapter);
        }
        else
        {
            database.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gpa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
