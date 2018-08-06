package com.bdwater.assetmanagement.questionmgr;

import android.content.Intent;
import android.graphics.Rect;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.bdwater.assetmanagement.R;
import com.bdwater.assetmanagement.common.AsyncSoapNoUIActivity;
import com.bdwater.assetmanagement.common.CustomProgressDialog;
import com.bdwater.assetmanagement.common.DensityUtil;
import com.bdwater.assetmanagement.model.Image;
import com.bdwater.assetmanagement.model.Site;
import com.bdwater.assetmanagement.model.TroubleNoteDetail;
import com.bdwater.assetmanagement.soap.QuestionDetailSoapAsyncTask;
import com.bdwater.assetmanagement.soap.SoapAsyncTask;
import com.bdwater.assetmanagement.soap.UpdateTroubleNoteDetailArgs;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.werb.pickphotoview.PickPhotoView;
import com.werb.pickphotoview.util.PickConfig;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionDetailEditActivity extends AsyncSoapNoUIActivity {
    public static final int REQUEST_CODE_ADD_CHILD = 20;
    public static final int REQUEST_CODE_EDIT_CHILD = 21;

    public static final String PARAM_EDIT_MODE = "EditMode";
    public static final String PARAM_TROUBLE_NOTE_ID = "TroubleNoteID";
    public static final String PARAM_TROUBLE_NOTE_NAME = "TroubleNoteName";
    public static final String PARAM_TROUBLE_NOTE_DETAIL_JSON = "TroubleNoteDetailJson";
    public static final int CREATE_MODE = 0;
    public static final int EDIT_MODE = 1;
    private static final int PICK_UP_MAX = 8;
    private static final int GRID_SPAN_COUNT = 4;
    int EditMode = CREATE_MODE;
    String troubleNoteID;
    String troubleNoteName;

    QuestionDetailSoapAsyncTask task;
    CoordinatorLayout view;
    ImageListAdapter adapter;
    List<Image> images = new ArrayList<>();
    TroubleNoteDetail troubleNoteDetail = new TroubleNoteDetail();
    CustomProgressDialog progressDialog;
    TimePickerDialog timePickerDialog;
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.swipeMenuRecyclerView)
    public SwipeMenuRecyclerView swipeMenuRecyclerView;
    @BindView(R.id.enableExpiredSwitch)
    public Switch enableExpiredSwitch;
    @BindView(R.id.expiredEditText)
    public EditText expiredEditText;
    @BindView(R.id.contentEditText)
    public EditText contentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = (CoordinatorLayout)getLayoutInflater().inflate(R.layout.activity_trouble_note_detail_edit, null);
        setContentView(view);

        ButterKnife.bind(this);

        this.init();
    }

    void init() {
        Intent intent = getIntent();
        // get parameters
        if(intent.hasExtra(PARAM_EDIT_MODE))
            this.EditMode = intent.getIntExtra(PARAM_EDIT_MODE, CREATE_MODE);
        troubleNoteID = intent.getStringExtra(PARAM_TROUBLE_NOTE_ID);
        troubleNoteName = intent.getStringExtra(PARAM_TROUBLE_NOTE_NAME);
        troubleNoteDetail.TroubleNoteID = troubleNoteID;
        if(this.EditMode == CREATE_MODE)
            troubleNoteDetail.TroubleNoteDetailID = java.util.UUID.randomUUID().toString();
        else {
            String s = intent.getStringExtra(PARAM_TROUBLE_NOTE_DETAIL_JSON);
            TypeAdapter<TroubleNoteDetail> typeAdapter = new Gson().getAdapter(TroubleNoteDetail.class);
            try {
                this.troubleNoteDetail = typeAdapter.fromJson(s);
                for(Image image : this.troubleNoteDetail.Images) {
                    image.From = Image.FROM_REMOTE;
                    image.IsUploaded = true;
                    images.add(image);
                }
                contentEditText.setText(troubleNoteDetail.Content);
            } catch (IOException e) {
                e.printStackTrace();
                Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        }

        toolbar.setTitle(troubleNoteName);          // set title by note's name
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        images.add(Image.IconForAdd());             // add a add icon into list
        adapter = new ImageListAdapter(images);
        swipeMenuRecyclerView.setLayoutManager(new GridLayoutManager(this, GRID_SPAN_COUNT));
        swipeMenuRecyclerView.addItemDecoration(new SpacesItemDecoration(8, GRID_SPAN_COUNT));
        swipeMenuRecyclerView.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if(adapter.getItemViewType(position) == ImageListAdapter.ADD) {
                    // it's add icon
                    new PickPhotoView.Builder(QuestionDetailEditActivity.this)
                            .setPickPhotoSize(PICK_UP_MAX - images.size() + 1)                  // select image size
                            .setClickSelectable(true)             // click one image immediately close and return image
                            .setShowCamera(true)                  // is show camera
                            .setSpanCount(4)                      // span count
                            .setLightStatusBar(true)              // lightStatusBar used in Android M or higher
                            .setStatusBarColor(android.R.color.white)     // statusBar color
                            .setToolbarColor(android.R.color.white)       // toolbar color
                            .setToolbarTextColor(android.R.color.black)   // toolbar text color
                            .setSelectIconColor(android.R.color.holo_blue_bright)     // select icon color
                            .setShowGif(false)                    // is show gif
                            .start();
                }
                else {
                    // it's normal item
                }
            }
        });
        swipeMenuRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        enableExpiredSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                expiredEditText.setEnabled(b);
            }
        });
        expiredEditText.setFocusable(false);
        expiredEditText.setFocusableInTouchMode(false);
        expiredEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show(getSupportFragmentManager(), "year_month_day");
            }
        });
        int textSize = DensityUtil.dip2px(this, getResources().getDimension(R.dimen.textSize));
        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
        timePickerDialog = new TimePickerDialog.Builder()
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        Date d = new Date(millseconds);
                        expiredEditText.setText(sf.format(d));
                    }
                })
                .setTitleStringId("")
                .setThemeColor(getResources().getColor(R.color.colorPrimary))
                .setCurrentMillseconds(System.currentTimeMillis())
                .setMinMillseconds(System.currentTimeMillis())
                .setMinMillseconds(System.currentTimeMillis() + tenYears)
                .setType(Type.YEAR_MONTH_DAY)
                .setWheelItemTextSize(textSize)
                .build();
    }

    @Override
    protected View getView() {
        return view;
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        MenuItem item = menu.add(Menu.NONE, 1, 1, getString(R.string.done));
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        return true;
    }    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case 1:
                submit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    void submit() {
        troubleNoteDetail.Content = contentEditText.getText().toString();
        UpdateTroubleNoteDetailArgs args = new UpdateTroubleNoteDetailArgs();
        args.progressLength = adapter.getRealItemCount() + 1;
        args.entry = troubleNoteDetail;
        args.images = images.toArray(new Image[images.size()]);
        args.mode = this.EditMode;
        progressDialog = new CustomProgressDialog(this);
        progressDialog.show();
        task = new QuestionDetailSoapAsyncTask(listener) {
            @Override
            protected void onProgressUpdate(final Integer... values) {
                super.onProgressUpdate(values);
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        if(progressDialog.isShowing())
                            progressDialog.setPercent(values[0]);
                    }
                });
            }
        };
        task.execute(args);
    }
    // submit task is completed
    QuestionDetailSoapAsyncTask.OnCompletedListener listener = new QuestionDetailSoapAsyncTask.OnCompletedListener() {
        @Override
        public void onCompleted(QuestionDetailSoapAsyncTask.SoapResult result) {
            progressDialog.dismiss();
            if(result.code == 0) {

            }
            else {
                Snackbar.make(view, result.message, Snackbar.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0){
            return;
        }
        if(data == null){
            return;
        }
        if (requestCode == PickConfig.INSTANCE.getPICK_PHOTO_DATA()) {
            ArrayList<String> selectPaths = (ArrayList<String>) data.getSerializableExtra(PickConfig.INSTANCE.getINTENT_IMG_LIST_SELECT());
            List<Image> current = new ArrayList<>();
            for(String path : selectPaths) {
                Image image = new Image();
                image.ImageID = java.util.UUID.randomUUID().toString();
                image.LocalPath = path;
                current.add(image);
            }
            int insert = adapter.getAddPosition();
            images.addAll(insert, current);
            if(images.size() > PICK_UP_MAX)
                adapter.removeAddView();
            else
                adapter.insertAddView();
            adapter.notifyDataSetChanged();
        }
    }


}
