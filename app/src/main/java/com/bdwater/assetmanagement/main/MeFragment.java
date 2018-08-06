package com.bdwater.assetmanagement.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bdwater.assetmanagement.CApplication;
import com.bdwater.assetmanagement.LoginActivity;
import com.bdwater.assetmanagement.R;
import com.bdwater.assetmanagement.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {
    View view;
    User user;

    @BindView(R.id.nameTextView)
    TextView nameTextView;
    @BindView(R.id.siteNameTextView)
    TextView siteNameTextView;
    @BindView(R.id.logoutButton)
    Button logoutButton;

    public MeFragment() {
        // Required empty public constructor
    }

    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        CApplication app = (CApplication)getActivity().getApplication();
        user = app.getUser();
        init();
        return view;
    }
    void init() {
        nameTextView.setText(user.Name);
        siteNameTextView.setText("[" + user.getSiteName() + "]");
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

}
