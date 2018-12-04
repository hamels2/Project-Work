package group2.identisky;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = new DBHandler(this);
        if (db.getStarsCount() == 0 || db.getConstellationsCount() == 0 || db.getPlanetsCount() == 0) {
            InputStream io1 = getResources().openRawResource(R.raw.database);
            ArrayList<SkyObject> starsList = CSVFile.readStarCSV(io1);
            for (int i = 0; i < starsList.size(); i++) {
                db.addSkyOBject(starsList.get(i));
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void GoToIdentisky(View view){
        Intent intent = new Intent(this, Identisky.class);
        startActivity(intent);
    }


    public void GoToStarSpots(View view){
        Intent intent = new Intent(this, StarSpots.class);
        startActivity(intent);
    }
}
