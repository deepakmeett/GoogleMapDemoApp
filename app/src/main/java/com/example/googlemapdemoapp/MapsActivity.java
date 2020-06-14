package com.example.googlemapdemoapp;
//import androidx.fragment.app.FragmentActivity;
//
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.location.LocationListener;
//import android.os.Bundle;
//import android.widget.Toast;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
//
//    private GoogleMap mMap;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate( savedInstanceState );
//        setContentView( R.layout.activity_maps );
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById( R.id.map );
//        mapFragment.getMapAsync( this );
//    }
//
//    /**
//     * Manipulates the map once available.
//     * This callback is triggered when the map is ready to be used.
//     * This is where we can add markers or lines, add listeners or move the camera. In this case,
//     * we just add a marker near Sydney, Australia.
//     * If Google Play services is not installed on the device, the user will be prompted to install
//     * it inside the SupportMapFragment. This method will only be triggered once the user has
//     * installed Google Play services and returned to the app.
//     */
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        // Add a marker in Loni and move the camera
//        LatLng loni = new LatLng( 28.7396358, 77.3031844 );
//        LatLng delhi = new LatLng( 28.7041, 77.1025 );
//        mMap.addMarker( new MarkerOptions().position( loni ).title( "Loni" ) );
//        mMap.addMarker( new MarkerOptions().position( delhi ).title( "Delhi" ) );
//        mMap.moveCamera( CameraUpdateFactory.newLatLng( loni ) );   
//        mMap.moveCamera( CameraUpdateFactory.newLatLng( delhi ) );
//    }
////    private String getAddress(double latitude, double longitude) {
////        String address = "";
////        Geocoder geocoder = new Geocoder( getApplicationContext(), Locale.getDefault() );
////        List<Address> addresses = null;
////        try {
////            addresses = geocoder.getFromLocation( latitude, longitude, 1 );
////            address = addresses.get( 0 ).getAddressLine( 0 );
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////        return address;
////    }
////25 Futa Rd, Loni, Ghaziabad, Uttar Pradesh 201102, India
////    private String getLatLong(String location) {
////        Geocoder geocoder = new Geocoder( this, Locale.getDefault() );
////        List<Address> addresses = null;
////        double lat = 28.7396358;
////        double lon = 77.3031844;
////        try {
////            addresses = geocoder.getFromLocation( lat, lon, 1 );
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////
////        String traced_actual_location = addresses.get( 0 ).getAddressLine( 0 );
////        String traced_actual_latitude = String.valueOf( lat);
////        String traced_actual_longitude = String.valueOf( lon);
////
////        final String locate = traced_actual_latitude + "° N," + " " + traced_actual_longitude + "° E";
////        return locate;
////    }
//}
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.example.googlemapdemoapp.NearByPlaces.GetNearbyPlacesData;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.ui.PlaceAutocompleteFragment;
import com.google.android.libraries.places.compat.ui.PlaceSelectionListener;

/**
 * Created by kodetr on 01/04/19.
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng sydney = new LatLng( -8.579892, 116.095239 );
    private static final int PROXIMITY_RADIUS_METERS = 15000;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_maps );
        mapFragment = (MapFragment) getFragmentManager().findFragmentById( R.id.map );
        mapFragment.getMapAsync( MapsActivity.this );
        setupAutoCompleteFragment();
    }

    private void setupAutoCompleteFragment() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById( R.id.place_autocomplete_fragment );
        autocompleteFragment.setOnPlaceSelectedListener( new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                sydney = place.getLatLng();
                String url = getUrl( sydney.latitude, sydney.longitude, "" );
                Object[] DataTransfer = new Object[2];
                DataTransfer[0] = mMap;
                DataTransfer[1] = url;
                Log.d( "onClick", url );
                GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                getNearbyPlacesData.execute( DataTransfer );
            }

            @Override
            public void onError(Status status) {
                Log.e( "Error", status.getStatusMessage() );
            }
        } );
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( sydney, 8.5f ) );
        mMap.addMarker( new MarkerOptions()
                                .position( sydney )
                                .title( "www.kodetr.com" )
                                .snippet( "lokasi saya" )
                                .icon( BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_RED ) ) );
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {
        StringBuilder googlePlacesUrl = new StringBuilder( getString( R.string.google_maps_key ) );
        googlePlacesUrl.append( "location=" + latitude + "," + longitude );
        googlePlacesUrl.append( "&radius=" + PROXIMITY_RADIUS_METERS );
        googlePlacesUrl.append( "&type=" + nearbyPlace );
        googlePlacesUrl.append( "&sensor=true" );
        googlePlacesUrl.append( "&key=" + getString( R.string.google_maps_key ) );
        Log.d( "getUrl", googlePlacesUrl.toString() );
        return (googlePlacesUrl.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMap != null) {
            mMap.clear();
        }
    }
}
