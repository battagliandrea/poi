package com.andreadev.poi.views.home.fragments;


import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.andreadev.poi.R;
import com.andreadev.poi.helper.NavigationHelper;
import com.andreadev.poi.models.Poi;
import com.andreadev.poi.helper.HomeFragmentCallback;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MapFragment extends Fragment implements HomeFragmentCallback, OnMapReadyCallback {

    @InjectView(R.id.mapview)
    MapView mapview;
    private GoogleMap map;

    private List<Poi> markersList;
    private HashMap<Marker, String> mHashMap = new HashMap<Marker, String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.inject(this, view);
        mapview.onCreate(savedInstanceState);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mapview!=null)
            mapview.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mHashMap!=null)
            mHashMap.clear();
        if(mapview!=null)
            mapview.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if(mapview!=null)
            mapview.onLowMemory();
    }

    @Override
    public void onResumeFragment() {
        requirePermission();
    }

    @Override
    public void setData(List<Poi> data) {
        markersList = new ArrayList<>();
        markersList.addAll(data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    private void requirePermission(){
        Dexter.checkPermissions(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    mapview.getMapAsync(MapFragment.this);
                } else {
                    //TODO alert dialog
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);

        MapsInitializer.initialize(getActivity());

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(45.07358, 7.6601), 12);//Turin lat/lng
        map.animateCamera(cameraUpdate);

        if(markersList!=null){
            for (Poi p : markersList) {
                Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(p.lat, p.lng)).title(p.name).snippet(p.address));
                mHashMap.put(marker, p.id);
            }
        }

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                NavigationHelper.navigateToDetails(getActivity(), mHashMap.get(marker), marker.getTitle());
            }
        });
    }
}
