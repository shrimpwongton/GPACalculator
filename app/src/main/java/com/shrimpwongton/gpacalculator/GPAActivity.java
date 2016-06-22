package com.shrimpwongton.gpacalculator;

import android.app.Application;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
    TextView GPA;
    FloatingActionButton fab;
    GPADatabase database = new GPADatabase(this);
    ClassDatabase classData = new ClassDatabase(this);
    View v1;
    ImageView trend;
    SharedPreferences sharedPrefs;
    double oldGPA = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition fade = new Fade();
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            fade.excludeTarget(android.R.id.navigationBarBackground, true);
            fade.excludeTarget(R.id.header, true);
            fade.excludeTarget(R.id.action_bar_container, true);
            getWindow().setExitTransition(fade);
            getWindow().setEnterTransition(fade);
        }
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        setTitle("GPA Calculator");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpa);
        trend = (ImageView) findViewById(R.id.trend);
        if ( sharedPrefs.getBoolean("trending_pref", true) )
            trend.setVisibility(View.INVISIBLE);
        else
            trend.setVisibility(View.VISIBLE);
        updateList();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GPAActivity.this, TermActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(GPAActivity.this, fab, "profile");
                startActivity(i, options.toBundle());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition fade = new Fade();
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            fade.excludeTarget(android.R.id.navigationBarBackground, true);
            fade.excludeTarget(R.id.header, true);
            fade.excludeTarget(R.id.action_bar_container, true);
            getWindow().setExitTransition(fade);
            getWindow().setEnterTransition(fade);
        }
        updateTermGPA();
        updateList();
        updateCumuGPA();
        listTerms.setClickable(true);
        listTerms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Term t = (Term) listTerms.getItemAtPosition(position);
                Intent i = new Intent(GPAActivity.this, TermActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(GPAActivity.this, fab, "profile");
                i.putExtra("ID", t.getId());
                i.putExtra("TERM", t);
                startActivity(i, options.toBundle());
            }
        });
        listTerms.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {
                new AlertDialog.Builder(GPAActivity.this)
                        .setTitle("Delete " + ((Term)listTerms.getItemAtPosition(pos)).getTerm() + "?")
                        .setMessage("Are you sure you want to delete this term?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Term t = (Term) listTerms.getItemAtPosition(pos);
                                List<Class> classes = classData.getAllClassesWithParentID(t.getId());
                                if (classes != null) {
                                    for (Class a : classes) {
                                        classData.deleteClass(a);
                                    }
                                }
                                database.deleteTerm((Term) listTerms.getItemAtPosition(pos));
                                updateCumuGPA();
                                updateList();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();

                return true;
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
            textView.setVisibility(View.VISIBLE);
            listTerms.setAdapter(null);
            database.close();
        }
    }

    public void updateTermGPA () {
        ArrayList<Term> allTerms = (ArrayList)database.getAllTerms();
        double total = 0.0;
        double totalUnits = 0.0;
        for ( Term a: allTerms) {
            ArrayList<Class> classes = (ArrayList)classData.getAllClassesWithParentID(a.getId());
            for ( Class b: classes) {
                total += b.getGrade() * b.getUnits();
                totalUnits += b.getUnits();
            }
            a.setGPA(total/totalUnits);
            database.updateTerm(a);
            total = 0.0;
            totalUnits = 0.0;
        }
        database.close();
    }

    public void updateCumuGPA() {
        GPA = (TextView) findViewById(R.id.GPA);
        trend = (ImageView) findViewById(R.id.trend);
        ArrayList<Class> allClasses = (ArrayList)classData.getAllClasses();
        if ( allClasses.size() == 0 ) {
            GPA.setText("- . - -");
            trend.setImageResource(R.drawable.ic_trending_flat_white_24dp);
            if ( sharedPrefs.getBoolean("trending_pref", true) )
                trend.setVisibility(View.INVISIBLE);
            else
                trend.setVisibility(View.VISIBLE);
            oldGPA = 0.0;
        }
        else {
            double total = 0.0;
            double totalUnits = 0.0;
            for (Class a : allClasses) {
                total += a.getGrade() * a.getUnits();
                totalUnits += a.getUnits();
            }
            if (total / totalUnits > oldGPA) {
                trend.setImageResource(R.drawable.ic_trending_up_white_24dp);
                oldGPA = total / totalUnits;
            } else if (total / totalUnits < oldGPA) {
                trend.setImageResource(R.drawable.ic_trending_down_white_24dp);
                oldGPA = total / totalUnits;
            } else {
                trend.setImageResource(R.drawable.ic_trending_flat_white_24dp);
                oldGPA = total / totalUnits;
            }
            if ( sharedPrefs.getBoolean("trending_pref", true) )
                trend.setVisibility(View.INVISIBLE);
            else
                trend.setVisibility(View.VISIBLE);

            GPA.setText(String.format("%.2f", total / totalUnits));
        }
        classData.close();
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
        if (id == R.id.action_about) {
            String versionName = BuildConfig.VERSION_NAME;
            AlertDialog.Builder dialog = new AlertDialog.Builder(GPAActivity.this);
            dialog.setTitle("About");
            dialog.setMessage( "Version " + versionName +
                    "\n\n© 2015 Anthony Wong "
            );
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialog.show();
            return true;
        }
        else if (id==R.id.action_clear) {
            int a = database.getTermCount();
            if (a != 0) {
                new AlertDialog.Builder(GPAActivity.this)
                        .setTitle("Delete All Terms")
                        .setMessage("Are you sure you want to delete all terms?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                ArrayList<Term> terms = (ArrayList) database.getAllTerms();
                                for (Term a : terms) {
                                    database.deleteTerm(a);
                                }
                                ArrayList<Class> classes = (ArrayList) classData.getAllClasses();
                                for ( Class a: classes) {
                                    classData.deleteClass(a);
                                }
                                updateCumuGPA();
                                updateList();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
            else {
                Toast.makeText(GPAActivity.this, "There are no terms to delete", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        else if ( id == R.id.action_setting) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivityForResult(i, 1);
        }

        return super.onOptionsItemSelected(item);
    }
}
