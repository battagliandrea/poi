package com.andreadev.poi.views.home.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andreadev.poi.R;
import com.andreadev.poi.helper.NavigationHelper;
import com.andreadev.poi.helper.OnItemSelectedListener;
import com.andreadev.poi.models.Poi;
import com.andreadev.poi.views.home.HomeActivity;
import com.andreadev.poi.views.adapters.ListAdapter;
import com.andreadev.poi.helper.HomeFragmentCallback;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ListPOIFragment extends Fragment implements HomeFragmentCallback {


    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;

    private ListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.inject(this, view);

        setupRecyclerView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private void setupRecyclerView(){
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new ListAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemSelectedListener(new OnItemSelectedListener<Poi>() {
            @Override
            public void onItemSelected(Poi item, int position) {
                NavigationHelper.navigateToDetails(getActivity(), item.id, item.name);
            }
        });
    }

    @Override
    public void onResumeFragment() {

    }

    @Override
    public void setData(List<Poi> data) throws NullPointerException{
        adapter.setPoi(data);
    }
}
