package com.bdwater.assetmanagement.questionmgr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bdwater.assetmanagement.R;
import com.bdwater.assetmanagement.common.AppCompatSwipeBackActivity;

public class QuestionDetailListActivity extends AppCompatSwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail_list);
    }
}
