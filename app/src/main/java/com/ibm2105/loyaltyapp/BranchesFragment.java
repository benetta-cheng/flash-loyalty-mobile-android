package com.ibm2105.loyaltyapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class BranchesFragment extends Fragment {
    GoogleMap mGoogleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=  inflater.inflate(R.layout.fragment_branches, container, false);
        SupportMapFragment supportMapFragment=(SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MapsInitializer.initialize(getContext());
                mGoogleMap = googleMap;
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                //ADD MORE MARKERS IF NEEDED
                googleMap.addMarker(new MarkerOptions().position(new LatLng(3.1466,101.6958)).title("Flash Convenient Store"));
                CameraPosition ConvenientStore= CameraPosition.builder().target(new LatLng(3.1466,101.6958)).zoom(16).bearing(0).tilt(45).build();
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(ConvenientStore));

                googleMap.addMarker(new MarkerOptions().position(new LatLng(3.1174,101.6781)).title("Flash Convenient Store"));
                CameraPosition ConvenientStore2= CameraPosition.builder().target(new LatLng(3.1174,101.6781)).zoom(16).bearing(0).tilt(45).build();
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(ConvenientStore2));

                googleMap.addMarker(new MarkerOptions().position(new LatLng(3.1332,101.6871)).title("Flash Convenient Store"));
                CameraPosition ConvenientStore3= CameraPosition.builder().target(new LatLng(3.1332,101.6871)).zoom(16).bearing(0).tilt(45).build();
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(ConvenientStore3));

                googleMap.addMarker(new MarkerOptions().position(new LatLng(3.0733,101.6079)).title("Flash Convenient Store"));
                CameraPosition ConvenientStore4= CameraPosition.builder().target(new LatLng(3.0733,101.6079)).zoom(16).bearing(0).tilt(45).build();
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(ConvenientStore3));
        }
        });

        return view;
    }
}