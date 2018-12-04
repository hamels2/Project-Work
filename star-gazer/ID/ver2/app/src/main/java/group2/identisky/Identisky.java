package group2.identisky;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.plus.Plus;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class Identisky extends AppCompatActivity implements AdapterView.OnItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private Button Const;
    private Button Stat;
    private Button Move;
    private Button ZeroToThree;
    private Button FourToEight;
    private Button EightToTwelve;
    private Button TwelvePlus;
    private Button Next;
    private Button Date;
    private Button Barely;
    private Button Medium;
    private Button Fast;
    private Button Yes;
    private Button No;
    private Button Yes1;
    private Button No1;
    private static Button Done;
    private Button SetTime;
    private static Button Done1;
    private TextView title;
    private Spinner spin;
    private String[] astro = {"none", "Aquarius", "Pisces", "Aries", "Taurus", "Gemini", "Cancer",
            "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn"};
    double speed;
    private String select;
    private static GregorianCalendar date = new GregorianCalendar();
    double blink;
    double twinkle;
    protected GoogleApiClient Client;
    Location LastLocation;
    double  Latitude;
    double Longitude;
    private LocationRequest LocationRequest;
    private ImageButton Full;
    private ImageButton Top;
    private ImageButton Half;
    private double viewing;
    private double stars;
    private double category;
    ConstellationExpert constExpert;
    MovingObjectExpert movExpert;
    StarAndPlanetExpert starExpert;
    Map<String,Double> info = new HashMap<String, Double>();
    public static ArrayList<SkyObject> answers;
    ArrayList<SkyObject> constellations;
    ArrayList<SkyObject> allstars;
    DBHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identisky);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = new DBHandler(this);
        constellations = db.getAllConstellations();
        allstars = db.getAllStars();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Const = (Button) findViewById(R.id.Constellation);
        Stat = (Button) findViewById(R.id.Stationairy);
        Move = (Button) findViewById(R.id.Moving);
        ZeroToThree = (Button) findViewById(R.id.ZeroToThree);
        FourToEight = (Button) findViewById(R.id.FourToEight);
        EightToTwelve = (Button) findViewById(R.id.EightToTwelve);
        TwelvePlus = (Button) findViewById(R.id.TwelvePlus);
        Next = (Button) findViewById(R.id.Next);
        Date = (Button) findViewById(R.id.Date);
        Fast = (Button) findViewById(R.id.Fast);
        Barely = (Button) findViewById(R.id.barely);
        Medium = (Button) findViewById(R.id.Medium);
        Yes = (Button) findViewById(R.id.Yes);
        No = (Button) findViewById(R.id.No);
        Yes1 = (Button) findViewById(R.id.Yes1);
        No1 = (Button) findViewById(R.id.No1);
        Done = (Button) findViewById(R.id.Done);
        Done1 = (Button) findViewById(R.id.Done1);
        SetTime = (Button) findViewById(R.id.Time);
        title = (TextView) findViewById(R.id.textView);
        Full = (ImageButton) findViewById(R.id.imageButton);
        Half = (ImageButton) findViewById(R.id.imageButton1);
        Top = (ImageButton) findViewById(R.id.imageButton2);



        spin = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Identisky.this, android.R.layout.simple_selectable_list_item, astro);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);
        spin.setPrompt("Yes");
        spin.setVisibility(View.INVISIBLE);

        if (Client == null) {
            Client = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }



    }

    @Override
    protected void onStart() {
        super.onStart();
        Client.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Client.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        LastLocation = LocationServices.FusedLocationApi.getLastLocation(
                Client);
        if (LastLocation != null) {
            Latitude = LastLocation.getLatitude();
            Longitude = LastLocation.getLongitude();


        }
        if (LastLocation == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(Client, LocationRequest, this);

        }

    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int pos = spin.getSelectedItemPosition();
        TextView text = (TextView) spin.getSelectedView();
        select = text.getText().toString();

        switch (pos) {
            case 0:

                break;
            case 1:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void Run(View view) {

        if (view == Const) {
            Const.setVisibility(View.GONE);
            Stat.setVisibility(View.GONE);
            Move.setVisibility(View.GONE);
            title.setText("Number of Stars?");
            ZeroToThree.setVisibility(View.VISIBLE);
            FourToEight.setVisibility(View.VISIBLE);
            EightToTwelve.setVisibility(View.VISIBLE);
            TwelvePlus.setVisibility(View.VISIBLE);
            category = 1;

        }

        if (view == Move) {
            Const.setVisibility(View.GONE);
            Stat.setVisibility(View.GONE);
            Move.setVisibility(View.GONE);
            Medium.setVisibility(View.VISIBLE);
            Fast.setVisibility(View.VISIBLE);
            Barely.setVisibility(View.VISIBLE);
            title.setText("How fast is it?");
            category = 2;

        }

        if (view == Barely) {
            Medium.setVisibility(View.GONE);
            Fast.setVisibility(View.GONE);
            Barely.setVisibility(View.GONE);
            title.setText("Is it blinking?");
            Yes.setVisibility(View.VISIBLE);
            No.setVisibility(View.VISIBLE);
            speed = 1;

        }

        if (view == Medium) {
            Medium.setVisibility(View.GONE);
            Fast.setVisibility(View.GONE);
            Barely.setVisibility(View.GONE);
            title.setText("Is it blinking?");
            Yes.setVisibility(View.VISIBLE);
            No.setVisibility(View.VISIBLE);
            speed = 2;
        }

        if (view == Fast) {
            Medium.setVisibility(View.GONE);
            Fast.setVisibility(View.GONE);
            Barely.setVisibility(View.GONE);
            title.setText("Is it blinking?");
            Yes.setVisibility(View.VISIBLE);
            No.setVisibility(View.VISIBLE);
            speed = 3;
        }

        if (view == Yes) {
            Yes.setVisibility(View.GONE);
            No.setVisibility(View.GONE);
            blink = 1;
            Date.setVisibility(View.VISIBLE);
            title.setText("Set Date:");

        }

        if (view == No) {
            Yes.setVisibility(View.GONE);
            No.setVisibility(View.GONE);
            blink = 0;
            Date.setVisibility(View.VISIBLE);
            title.setText("Set Date:");

        }

        if (view == Stat) {
            Const.setVisibility(View.GONE);
            Stat.setVisibility(View.GONE);
            Move.setVisibility(View.GONE);
            Yes1.setVisibility(View.VISIBLE);
            No1.setVisibility(View.VISIBLE);
            title.setText("Does it twinkle?:");
            category = 3;
        }

        if (view == Yes1) {
            Yes1.setVisibility(View.GONE);
            No1.setVisibility(View.GONE);
            twinkle = 1;
            Date.setVisibility(View.VISIBLE);
            title.setText("Set Date:");

        }

        if (view == No1) {
            Yes1.setVisibility(View.GONE);
            No1.setVisibility(View.GONE);
            twinkle = 0;
            Date.setVisibility(View.VISIBLE);
            title.setText("Set Date:");

        }


        if (view == Stat) {
            Const.setVisibility(View.GONE);
            Stat.setVisibility(View.GONE);
            Move.setVisibility(View.GONE);

        }

        if (view == ZeroToThree) {

            ZeroToThree.setVisibility(View.GONE);
            FourToEight.setVisibility(View.GONE);
            EightToTwelve.setVisibility(View.GONE);
            TwelvePlus.setVisibility(View.GONE);
            title.setText("Astrology sign?");
            spin.setVisibility(View.VISIBLE);
            Next.setVisibility(View.VISIBLE);
            stars = 0;

        } else if (view == FourToEight) {

            ZeroToThree.setVisibility(View.GONE);
            FourToEight.setVisibility(View.GONE);
            EightToTwelve.setVisibility(View.GONE);
            TwelvePlus.setVisibility(View.GONE);
            title.setText("Astrology sign?");
            spin.setVisibility(View.VISIBLE);
            Next.setVisibility(View.VISIBLE);
            stars = 4;

        } else if (view == EightToTwelve) {

            ZeroToThree.setVisibility(View.GONE);
            FourToEight.setVisibility(View.GONE);
            EightToTwelve.setVisibility(View.GONE);
            TwelvePlus.setVisibility(View.GONE);
            title.setText("Astrology sign?");
            spin.setVisibility(View.VISIBLE);
            Next.setVisibility(View.VISIBLE);
            stars = 8;

        } else if (view == TwelvePlus) {
            ZeroToThree.setVisibility(View.GONE);
            FourToEight.setVisibility(View.GONE);
            EightToTwelve.setVisibility(View.GONE);
            TwelvePlus.setVisibility(View.GONE);
            title.setText("Astrology sign?");
            spin.setVisibility(View.VISIBLE);
            Next.setVisibility(View.VISIBLE);
            stars = 12;

        }
        if (view == Next) {
            spin.setVisibility(View.GONE);
            Next.setVisibility(View.GONE);
            Date.setVisibility(View.VISIBLE);
            title.setText("Set Date:");


        }
        if (view == Done) {
            Date.setVisibility(View.GONE);
            Done.setVisibility(View.GONE);
            title.setText("Set Time:");
            SetTime.setVisibility(View.VISIBLE);

        }
        if (view == Done1) {
            SetTime.setVisibility(View.GONE);
            Done1.setVisibility(View.GONE);
            title.setText("View of Sky:");
            Full.setVisibility(View.VISIBLE);
            Half.setVisibility(View.VISIBLE);
            Top.setVisibility(View.VISIBLE);

        }

        if(view == Full){
            viewing = 0.0;
            System.out.println("button");
            sendInfoToExperts();
            

        }

        if(view == Top){
            viewing = 60;
            sendInfoToExperts();

        }

        if(view == Half){
            viewing = 45;
            sendInfoToExperts();

        }

    }

    //only sends to constellation expert right now
    public void sendInfoToExperts(){

        info.put(Encryption.encrypt("category"), category);
        info.put(Encryption.encrypt("twinkling"), twinkle);
        info.put(Encryption.encrypt("blinking"), blink);
        info.put(Encryption.encrypt("speed"), speed);
        info.put(Encryption.encrypt("longitude"),Longitude);
        info.put(Encryption.encrypt("latitude"),Latitude);
        info.put(Encryption.encrypt("skyVisibility"), viewing);
        info.put(Encryption.encrypt("numStars"),stars);
        answers = new ArrayList<SkyObject>();
        constExpert = new ConstellationExpert(this);
        constExpert.assess(date, info, Encryption.encrypt(select), answers, constellations);
        starExpert = new StarAndPlanetExpert(this);
        starExpert.assess(date, info, Encryption.encrypt(select), answers, allstars);
        movExpert = new MovingObjectExpert(this);
        movExpert.assess(date,info,Encryption.encrypt(select),answers,allstars);
        Intent intent = new Intent(this, Results.class);
        startActivity(intent);
    }

    public static ArrayList<SkyObject> getAnswers() {return answers;}

    public static class DatePickerFragement extends android.app.DialogFragment implements android.app.DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final java.util.Calendar c = java.util.Calendar.getInstance();
            int year = c.get(java.util.Calendar.YEAR);
            int month = c.get(java.util.Calendar.MONTH);
            int day = c.get(java.util.Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), R.style.MyStyle, this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Done.setVisibility(View.VISIBLE);
            date.set(year,month,day);
        }
    }


    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Done1.setVisibility(View.VISIBLE);
            date.set(Calendar.HOUR_OF_DAY, hourOfDay);
            date.set(Calendar.MINUTE, minute);

        }

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragement();
        newFragment.show(getFragmentManager(), "datePicker");


    }
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }




}
