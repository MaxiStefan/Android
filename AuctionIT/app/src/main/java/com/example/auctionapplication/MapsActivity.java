package com.example.auctionapplication;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        float zoom = 16.5f;
//        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        LatLng NMoRA = new LatLng(44.4393693, 26.0958443);
        LatLng GoEA = new LatLng(44.4388917, 26.0966312);
        LatLng SEAOC = new LatLng(44.4402589, 26.0979552);

        mMap.addMarker(new MarkerOptions().position(NMoRA).title("The National Romanian Museum"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(NMoRA));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(NMoRA, zoom));
        googleMap.setOnMarkerClickListener(this);
        PolylineOptions plo = new PolylineOptions();
        plo.add(NMoRA);
        plo.add(GoEA);
        plo.add(SEAOC);
        plo.color(Color.BLUE);
        plo.geodesic(true);
        plo.startCap(new RoundCap());
        plo.width(20);
        plo.jointType(JointType.BEVEL);

        mMap.addPolyline(plo);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(NMoRA)
                .title("NMoRA")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        //definire marker personalizat
        MarkerOptions markerOptions1 = new MarkerOptions();
        markerOptions1.position(GoEA)
                .title("Galeria de artă europeană")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        //definire marker personalizat
        MarkerOptions markerOptions2 = new MarkerOptions();
        markerOptions2.position(SEAOC)
                .title("Societatea Evaluatorilor de Artă și Obiecte de Colecție")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

        Marker m = mMap.addMarker(markerOptions);
        m.showInfoWindow();

        Marker m1 = mMap.addMarker(markerOptions1);
        m1.showInfoWindow();

        Marker m2 = mMap.addMarker(markerOptions2);
        m2.showInfoWindow();

    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}

