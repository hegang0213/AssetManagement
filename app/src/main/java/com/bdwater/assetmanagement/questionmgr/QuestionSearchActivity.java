package com.bdwater.assetmanagement.questionmgr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.bdwater.assetmanagement.R;

public class QuestionSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_search);

        ((TextView)findViewById(R.id.textView)).setText(getIntent().getStringExtra("Query"));
    }
}
