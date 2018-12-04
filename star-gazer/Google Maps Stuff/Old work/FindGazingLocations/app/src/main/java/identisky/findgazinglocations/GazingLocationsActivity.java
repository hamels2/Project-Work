package identisky.findgazinglocations;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

public class GazingLocationsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gazing_locations);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


    }


    //
    //* Manipulates the map once available.
    //* This callback is triggered when the map is ready to be used.
    //* This is where we can add markers or lines, add listeners or move the camera. In this case,
    //* we just add a marker near Sydney, Australia.
    //* If Google Play services is not installed on the device, the user will be prompted to install
    //* it inside the SupportMapFragment. This method will only be triggered once the user has
    //* installed Google Play services and returned to the app.
    //
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        double top3Elev[][] = new double[3][3];

        try {
            top3Elev = ElevationFinder.gridSearch(-34, 151);
        } catch (IOException e) {
            System.out.println("Error");
        }

        LatLng elevMarkers[] = new LatLng[3];

        for (int i = 0; i < 3; i++) {
            elevMarkers[i] = new LatLng(top3Elev[i][0], top3Elev[i][1]);
            mMap.addMarker(new MarkerOptions().position(elevMarkers[i]).title(
                    "Elevation = " + String.valueOf(top3Elev[i][2])));
        }

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}