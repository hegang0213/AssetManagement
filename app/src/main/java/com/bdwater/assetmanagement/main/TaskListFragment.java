package com.bdwater.assetmanagement.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bdwater.assetmanagement.R;
import com.bdwater.assetmanagement.common.SearchViewUtils;
import com.bdwater.assetmanagement.taskManagement.TaskSearchActivity;

public class TaskListFragment extends Fragment {
    SearchView searchView;

    public TaskListFragment() {
        // Required empty public constructor
    }


    public static TaskListFragment newInstance() {
        TaskListFragment fragment = new TaskListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_list, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.task_list_menu, menu);
        setSearchView(menu);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.equals(""))
                    return true;

                Intent intent = new Intent(getActivity(), TaskSearchActivity.class);
                intent.putExtra("Query", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    private void setSearchView(Menu menu) {
        MenuItem item = menu.findItem(R.id.searchView);
        searchView = SearchViewUtils.CreateSearchView(getActivity(), getString(R.string.task_query_hint));
        item.setActionView(searchView);
    }
    // TODO: Rename method, update argument and hook method into UI event

}
