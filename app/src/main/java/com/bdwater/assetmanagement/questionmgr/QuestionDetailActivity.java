package com.bdwater.assetmanagement.questionmgr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bdwater.assetmanagement.R;
import com.bdwater.assetmanagement.common.AsyncSoapNoUIActivity;
import com.bdwater.assetmanagement.common.CustomProgressDialog;
import com.bdwater.assetmanagement.model.Image;
import com.bdwater.assetmanagement.model.TroubleNoteDetail;
import com.bdwater.assetmanagement.model.TroubleNoteDetailStatus;
import com.bdwater.assetmanagement.soap.SoapAsyncTask;
import com.bdwater.assetmanagement.soap.SoapClient;
import com.google.gson.Gson;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import part3rd.ortiz.touch.ViewPagerExampleActivity;

public class QuestionDetailActivity extends AsyncSoapNoUIActivity {
    public static final String PARAM_TROUBLE_NOTE_DETAIL_JSON = "Json";
    public static final int RESULT_CODE = 30;
    public static final String RESULT_JSON = "Json";
    public static final int REQUEST_CODE = 30;

    private static final int GRID_SPAN_COUNT = 4;

    View view;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.statusNameTextView)
    public TextView statusNameTextView;
    @BindView(R.id.swipeMenuRecyclerView)
    public SwipeMenuRecyclerView swipeMenuRecyclerView;
    @BindView(R.id.contentTextView)
    public TextView contentTextView;
    @BindView(R.id.expiredDateTextView)
    public TextView expiredDateTextView;
    @BindView(R.id.nestedScrollView)
    public NestedScrollView nestedScrollView;
    @BindView(R.id.responseLayout)
    public LinearLayout responseLayout;
    @BindView(R.id.bottomLayout)
    public LinearLayout bottomLayout;
    @BindView(R.id.rejectButton)
    public Button rejectButton;
    @BindView(R.id.contentEditText)
    public EditText contentEditText;
    @BindView(R.id.sendButton)
    public Button sendButton;

    CustomProgressDialog progressDialog;

    TroubleNoteDetail troubleNoteDetail;
    TroubleNoteDetail current = new TroubleNoteDetail();
    int currentIndex;               // response index of note detail
    List<Image> images = new ArrayList<>();
    ImageListAdapter adapter;
    int mode;                       // call web service with create, edit or delete
    int status = 0;                 // note detail's final status
    boolean isChanged = false;      // the response was sent or was deleted, make it true

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = getLayoutInflater().inflate(R.layout.activity_question_detail, null);
        setContentView(view);

        ButterKnife.bind(this);
        this.init();

    }
    void init() {
        String json = getIntent().getStringExtra(PARAM_TROUBLE_NOTE_DETAIL_JSON);
        // gets note detail object from json string parameter of intent
        troubleNoteDetail = new Gson().fromJson(json, TroubleNoteDetail.class);

        // sets title
        toolbar.setTitle(R.string.trouble_note_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set status name and content of this question
        statusNameTextView.setText(troubleNoteDetail.StatusName);
        contentTextView.setText(troubleNoteDetail.Content);

        // fill images from note detail
        for(Image image : troubleNoteDetail.Images)
            images.add(image);
        adapter = new ImageListAdapter(images);
        adapter.setReadonly(true);

        // setup recycler view
        swipeMenuRecyclerView.setLayoutManager(new GridLayoutManager(this, GRID_SPAN_COUNT));
        swipeMenuRecyclerView.addItemDecoration(new SpacesItemDecoration(8, GRID_SPAN_COUNT));
        swipeMenuRecyclerView.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                ArrayList<String> images = new ArrayList<>();
                for(int i = 0; i < adapter.getItemCount(); i++) {
                    images.add(adapter.getItem(i).RemotePath);
                }
                Intent intent = new Intent(QuestionDetailActivity.this, ViewPagerExampleActivity.class);
                intent.putStringArrayListExtra(ViewPagerExampleActivity.PARAM_IMAGE_ARRAY, images);
                intent.putExtra(ViewPagerExampleActivity.PARAM_CURRENT, position);
                startActivity(intent);

            }
        });
        swipeMenuRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // adjusts height of scroll view when height of bottom layout was resized
        bottomLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)nestedScrollView.getLayoutParams();
                params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, bottomLayout.getHeight());
            }
        });

        // init current response
        current.TroubleNoteID = troubleNoteDetail.TroubleNoteID;
        current.TroubleNoteDetailID = java.util.UUID.randomUUID().toString();
        current.TopID = troubleNoteDetail.TroubleNoteDetailID;
        current.ParentID = troubleNoteDetail.getParentID();
        currentIndex = troubleNoteDetail.Children.length + 1;
        // fill response ui of children of note detail into layout
        this.fillResponses();
        nestedScrollView.post(new Runnable() {
            @Override
            public void run() {
                nestedScrollView.requestLayout();
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // reject charger's response
                execute(FunctionMode.CREATE, TroubleNoteDetailStatus.REJECT);
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // adding this response
                execute(FunctionMode.CREATE, isOwnResponse() ? TroubleNoteDetailStatus.DONE : TroubleNoteDetailStatus.RESPONSE);
            }
        });

        // if the bottom layout is visible or not
        // conditional include user's permission and user is owner or charger
        bottomLayout.setVisibility(troubleNoteDetail.Status != TroubleNoteDetailStatus.DONE ? View.VISIBLE: View.GONE);
        rejectButton.setVisibility(isOwnResponse() && troubleNoteDetail.Status != TroubleNoteDetailStatus.DONE
            ? View.VISIBLE : View.GONE);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                // closes soft keyboard if touch out of any edit text ui
                // makes user's sight clean
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
    // fill response's ui layout
    void fillResponses() {
        int i = 1;
        boolean isOwe;
        for (TroubleNoteDetail response: this.troubleNoteDetail.Children) {
            isOwe = i % 2 == 0;
            fillResponse(response, isOwe);  // fill response ui
            i++;
        }
    }
    // fill a response's layout ui by response object
    // isOwe is true indicates response by owe, or else by another people
    void fillResponse(TroubleNoteDetail response, boolean isOwe) {
        // inflates response view
        LinearLayout view = (LinearLayout)getLayoutInflater().inflate(R.layout.question_response_item, null);
        ((TextView)view.findViewById(R.id.textView)).setText(response.Content);
        ((TextView)view.findViewById(R.id.createDateTextView)).setText(response.CreateDate);

        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        // set layout gravity
        // puts child views of response item at left or right in layout
        view.setGravity(isOwe ? Gravity.LEFT : Gravity.RIGHT);

        // set gravity
        // puts response item at left or right into layout of this activity
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = isOwe ? Gravity.LEFT : Gravity.RIGHT;
        responseLayout.addView(view, lp);
    }
    // current response is by owner if response index is even,
    // else odd response is by charger
    boolean isOwnResponse() {
        return currentIndex % 2 == 0;
    }
    // mode is create, edit or delete,
    // status is processing, reply, done or reject of detail,
    // and then execute web service
    void execute(int mode, int status) {
        this.mode = mode;
        this.status = status;
        execute();
    }
    // set ui after user operate,
    // can is true make bottom layout gone
    void setCanSend(boolean can) {
        this.bottomLayout.setVisibility(can ? View.VISIBLE: View.GONE);
    }
    @Override
    protected String onGetSoapMethodName() {
        return SoapClient.UPDATE_RESPONSE_METHOD;
    }

    @Override
    protected void onLoading() {
        progressDialog = new CustomProgressDialog(this);
        progressDialog.show();
    }

    @Override
    protected void onLoaded() {
        progressDialog.dismiss();
    }

    @Override
    protected void onSoapAsyncTaskExecute(SoapAsyncTask task) {
        // prepare response data before it will be sent
        Editable editable = contentEditText.getText();
        current.Content = editable == null ? "": editable.toString();
        current.Status = this.status;
        current.StatusName = TroubleNoteDetailStatus.getName(this.status);
        current.CreateDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        troubleNoteDetail.Status = this.status;
        troubleNoteDetail.StatusName = TroubleNoteDetailStatus.getName(this.status);
        String s = new Gson().toJson(current, TroubleNoteDetail.class);

        // parameters
        // 1 - create, edit or delete
        // 2 - current status of note detail
        // 3 - json string of the current response
        task.execute(Integer.toString(this.mode), Integer.toString(this.status), s);
    }


    @Override
    protected void onLoadFailed(int code, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onLoadSuccess(JSONObject result) {
        this.setCanSend(false);
        Snackbar.make(view, getString(R.string.done), Snackbar.LENGTH_LONG).show();
        isChanged = true;
        if(this.mode == FunctionMode.CREATE) {
            // adds response into children array of note detail
            TroubleNoteDetail[] children = new TroubleNoteDetail[troubleNoteDetail.Children.length + 1];

            System.arraycopy(troubleNoteDetail.Children, 0, children, 0, troubleNoteDetail.Children.length);
            children[children.length - 1] = current;
            troubleNoteDetail.Children = children;

            // generate response ui
            fillResponse(current, isOwnResponse());

            // set result for caller's activity
            // this result is json string of note detail,
            // the caller will be received after this activity was closed
            Intent intent = new Intent();
            intent.putExtra(RESULT_JSON, new Gson().toJson(troubleNoteDetail, TroubleNoteDetail.class));
            setResult(RESULT_CODE, intent);
        }
    }

    @Override
    protected View getView() {
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    // send mode for create, change or delete?
    static class FunctionMode {
        public static final int CREATE = 0;
        public static final int DETETE = 2;
    }

}
