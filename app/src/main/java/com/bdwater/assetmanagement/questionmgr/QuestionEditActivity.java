package com.bdwater.assetmanagement.questionmgr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.bdwater.assetmanagement.CApplication;
import com.bdwater.assetmanagement.R;
import com.bdwater.assetmanagement.common.AsyncSoapActivity;
import com.bdwater.assetmanagement.common.CustomProgressDialog;
import com.bdwater.assetmanagement.common.NetworkCardView;
import com.bdwater.assetmanagement.model.Site;
import com.bdwater.assetmanagement.model.TroubleNote;
import com.bdwater.assetmanagement.soap.SoapAsyncTask;
import com.bdwater.assetmanagement.soap.SoapClient;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionEditActivity extends AsyncSoapActivity {
    public static final int REQUEST_CODE_ADD_QUESTION = 10;
    public static final int REQUEST_CODE_EDIT_QUESTION = 11;

    public static final String PARAM_EDIT_MODE = "EditMode";
    public static final String PARAM_ID = "ID";
    public static final String PARAM_NAME = "Name";
    public static final String PARAM_SITE_ID = "SiteID";
    public static final int CREATE_MODE = 0;
    public static final int EDIT_MODE = 1;
    int EditMode = CREATE_MODE;

    static final int GET_SITE = 0;
    static final int GOTO_DONE = 1;
    int TASK_TYPE = 0;

    TroubleNote troubleNote = new TroubleNote();
    SiteListAdapter adapter;
    List<Site> sites = new ArrayList<>();
    CustomProgressDialog progressDialog;

    @BindView(R.id.coordinatorLayout)
    public CoordinatorLayout coordinatorLayout;
    @BindView(R.id.nameEditText)
    public EditText nameEditText;
    @BindView(R.id.swipeMenuRecyclerView)
    public SwipeMenuRecyclerView swipeMenuRecyclerView;
    @BindView(R.id.networkCardView)
    public NetworkCardView networkCardView;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_note_edit);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent.hasExtra(PARAM_EDIT_MODE)) {
            this.EditMode = intent.getIntExtra(PARAM_EDIT_MODE, CREATE_MODE);
            if(this.EditMode == EDIT_MODE) {
                troubleNote.TroubleNoteID = intent.getStringExtra(PARAM_ID);
                troubleNote.Name = intent.getStringExtra(PARAM_NAME);
                troubleNote.SiteID = intent.getStringExtra(PARAM_SITE_ID);

                nameEditText.setText(troubleNote.Name);
            }
            else
                troubleNote.TroubleNoteID = java.util.UUID.randomUUID().toString();
        }

        toolbar.setTitle(R.string.trouble_note);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SiteListAdapter(sites);
        swipeMenuRecyclerView.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                adapter.setSelected(position);
            }
        });
        swipeMenuRecyclerView.setAdapter(adapter);

        cancel();
        execute(GET_SITE);
    }
    void execute(int taskType) {
        this.TASK_TYPE = taskType;
        super.execute();
    }

    @Override
    protected NetworkCardView onGetNetworkCarView() {
        return this.networkCardView;
    }

    @Override
    protected String onGetSoapMethodName() {
        if(TASK_TYPE == GET_SITE)
            return SoapClient.GET_SITES_METHOD;
        return SoapClient.UPDATE_TROUBLE_NOTE_METHOD;
    }

    @Override
    protected void onLoading() {
        if(TASK_TYPE == GET_SITE) {
            super.onLoading();
        }

    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        if(TASK_TYPE == GOTO_DONE) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void showMessage(String message) {
        if(TASK_TYPE == GET_SITE)
            super.showMessage(message);
        else
            Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onSoapAsyncTaskExecute(SoapAsyncTask task) {
        if(TASK_TYPE == GET_SITE)
            task.execute();
        else {
            if(EditMode == CREATE_MODE)
                executeForCreate(task);
            else
                executeForEdit(task);
        }

    }
    void executeForCreate(SoapAsyncTask task) {
        troubleNote.Name = nameEditText.getText().toString();
        troubleNote.SiteID = adapter.getSelectedItem().SiteID;
        troubleNote.SiteName = adapter.getSelectedItem().Name;
        troubleNote.InputerID = ((CApplication)getApplication()).getUser().UserID;
        try {
            String s = troubleNote.toJsonString();
            task.execute("0", s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void executeForEdit(SoapAsyncTask task) {
        troubleNote.Name = nameEditText.getText().toString();
        troubleNote.SiteID = adapter.getSelectedItem().SiteID;
        troubleNote.SiteName = adapter.getSelectedItem().Name;
        try {
            String s = troubleNote.toJsonString();
            task.execute("1", s);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    boolean validate() {
        String name = nameEditText.getText().toString();
        if(name.trim().equals("")) {
            Snackbar.make(coordinatorLayout, getString(R.string.name_validate_message), Snackbar.LENGTH_LONG).show();
            return false;
        }
        if(adapter.getSelectedItem() == null) {
            Snackbar.make(coordinatorLayout, getString(R.string.site_validate_message), Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    void selectedSite() {
        for(int i = 0; i < sites.size(); i++) {
            Site site = sites.get(i);
            if(site.SiteID.equals(troubleNote.SiteID)) {
                adapter.setSelected(i);
            }
        }
    }
    @Override
    protected void onLoadSuccess(JSONObject result) {
        try {
            String dataString = result.getString("data");
            if (TASK_TYPE == GET_SITE) {
                JSONArray ja = new JSONArray(dataString);
                List<Site> current = Site.parseArray(ja);
                this.sites.clear();
                this.sites.addAll(current);
                adapter.notifyDataSetChanged();
                if(EditMode == EDIT_MODE)
                    selectedSite();
            }
            else{
//                if(EditMode == CREATE_MODE) {
//                    troubleNote.TroubleNoteID = new JSONObject(dataString).getString("TroubleNoteID");
                setResult(RESULT_OK);
                finish();
//                }

            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        MenuItem item = menu.add(Menu.NONE, 1, 1, getString(R.string.done));
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case 1:
                if(validate()) {
                    progressDialog = new CustomProgressDialog(this);
                    progressDialog.show();
                    execute(GOTO_DONE);
                }
                break;
        }
        return true;
    }
}
