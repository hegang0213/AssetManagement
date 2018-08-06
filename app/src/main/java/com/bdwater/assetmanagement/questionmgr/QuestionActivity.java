package com.bdwater.assetmanagement.questionmgr;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.bdwater.assetmanagement.R;
import com.bdwater.assetmanagement.common.AsyncSoapActivity;
import com.bdwater.assetmanagement.common.IconicsHelper;
import com.bdwater.assetmanagement.common.NetworkCardView;
import com.bdwater.assetmanagement.soap.SoapAsyncTask;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;

import org.json.JSONObject;

public class QuestionActivity extends AsyncSoapActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
    }


    @Override
    protected NetworkCardView onGetNetworkCarView() {
        return null;
    }

    @Override
    protected String onGetSoapMethodName() {
        return null;
    }

    @Override
    protected void onSoapAsyncTaskExecute(SoapAsyncTask task) {

    }

    @Override
    protected void onLoadSuccess(JSONObject result) {

    }
}
