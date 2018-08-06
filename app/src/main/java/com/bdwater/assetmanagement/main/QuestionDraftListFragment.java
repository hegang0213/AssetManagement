package com.bdwater.assetmanagement.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bdwater.assetmanagement.R;
import com.bdwater.assetmanagement.common.AsyncSoapNoUIFragment;
import com.bdwater.assetmanagement.common.CustomProgressDialog;
import com.bdwater.assetmanagement.common.IconicsHelper;
import com.bdwater.assetmanagement.common.PaddingDecoration;
import com.bdwater.assetmanagement.model.TroubleNote;
import com.bdwater.assetmanagement.model.TroubleNoteDetail;
import com.bdwater.assetmanagement.model.TroubleNoteStatus;
import com.bdwater.assetmanagement.questionmgr.QuestionDetailActivity;
import com.bdwater.assetmanagement.questionmgr.QuestionDetailEditActivity;
import com.bdwater.assetmanagement.questionmgr.QuestionEditActivity;
import com.bdwater.assetmanagement.soap.SoapAsyncTask;
import com.bdwater.assetmanagement.soap.SoapClient;
import com.bdwater.assetmanagement.soap.SoapTaskResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class QuestionDraftListFragment extends AsyncSoapNoUIFragment {

    int mPosition = -1;
    int mChildPosition = -1;

    static final int PAGE_SIZE = 10;
    List<TroubleNote> mList = new ArrayList<>();
    QuestionListRecyclerViewAdapter adapter;

    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.swipeMenuRecyclerView)
    public SwipeMenuRecyclerView swipeMenuRecyclerView;

    CustomProgressDialog progressDialog;

    public QuestionDraftListFragment() {
        // Required empty public constructor
    }


    public static QuestionDraftListFragment newInstance() {
        QuestionDraftListFragment fragment = new QuestionDraftListFragment();
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
        View view = inflater.inflate(R.layout.fragment_question_draft_list, container, false);
        ButterKnife.bind(this, view);
        adapter = new QuestionListRecyclerViewAdapter(mList);
        adapter.setAllowEdit(true);
        adapter.setMenuItemClickListener(listener);
        swipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeMenuRecyclerView.setAdapter(adapter);
        swipeMenuRecyclerView.addItemDecoration(new PaddingDecoration(0, 10, 0, 10));
        swipeMenuRecyclerView.useDefaultLoadMore();
        swipeMenuRecyclerView.setLoadMoreListener(new SwipeMenuRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                execute();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mList.clear();
                        execute();
                    }
                });
            }
        });
        swipeRefreshLayout.setRefreshing(true);
        execute();
        return view;
    }
    QuestionListRecyclerViewAdapter.MenuItemClickListener listener = new QuestionListRecyclerViewAdapter.MenuItemClickListener() {
        @Override
        public void onEditClick(View view, int position) {
            TroubleNote entry = adapter.getItem(position);
            switch (entry.Status) {
                case TroubleNote.StatusList.DRAFT:
                    // the note is a draft, clicks to edit it
                    mPosition = position;
                    edit(entry);

                    break;
                default:
                    add();
                    break;
            }
        }

        @Override
        public void onPublishClick(View view, int position) {
            mPosition = position;
            publish(position);
        }

        @Override
        public void onDeleteClick(View view, int position) {

        }

        @Override
        public void onAddChildClick(View view, int position) {
            mPosition = position;
            addChild(adapter.getItem(position));
        }

        @Override
        public void onChildEditClick(View view, int position, int childIndex) {
            mPosition = position;
            mChildPosition = childIndex;
            TroubleNote note = adapter.getItem(position);
            TroubleNoteDetail detail = adapter.getChildItem(position, childIndex);
            editChild(note, detail);
        }

        @Override
        public void onChildClick(View view, int position, int childIndex) {
            mPosition = position;
            mChildPosition = childIndex;
            TroubleNote note = adapter.getItem(position);
            TroubleNoteDetail detail = adapter.getChildItem(position, childIndex);
            editChild(note, detail);
        }
    };
    void add() {
        Intent intent = new Intent(getActivity(), QuestionEditActivity.class);
        startActivityForResult(intent, QuestionEditActivity.REQUEST_CODE_ADD_QUESTION);
    }
    void edit(TroubleNote note) {
        Intent intent = new Intent(getActivity(), QuestionEditActivity.class);
        intent.putExtra(QuestionEditActivity.PARAM_EDIT_MODE, QuestionEditActivity.EDIT_MODE);
        intent.putExtra(QuestionEditActivity.PARAM_ID, note.TroubleNoteID);
        intent.putExtra(QuestionEditActivity.PARAM_NAME, note.Name);
        intent.putExtra(QuestionEditActivity.PARAM_SITE_ID, note.SiteID);
        startActivityForResult(intent, QuestionEditActivity.REQUEST_CODE_EDIT_QUESTION);
    }
    void addChild(TroubleNote note) {
        Intent intent = new Intent(getActivity(), QuestionDetailEditActivity.class);
        intent.putExtra(QuestionDetailEditActivity.PARAM_TROUBLE_NOTE_ID, note.TroubleNoteID);
        intent.putExtra(QuestionDetailEditActivity.PARAM_TROUBLE_NOTE_NAME, note.Name);
        startActivityForResult(intent, QuestionDetailEditActivity.REQUEST_CODE_ADD_CHILD);
    }
    void editChild(TroubleNote note, TroubleNoteDetail detail) {
        Intent intent = new Intent(getActivity(), QuestionDetailEditActivity.class);
        intent.putExtra(QuestionDetailEditActivity.PARAM_EDIT_MODE, QuestionDetailEditActivity.EDIT_MODE);
        intent.putExtra(QuestionDetailEditActivity.PARAM_TROUBLE_NOTE_ID, note.TroubleNoteID);
        intent.putExtra(QuestionDetailEditActivity.PARAM_TROUBLE_NOTE_NAME, note.Name);
        intent.putExtra(QuestionDetailEditActivity.PARAM_TROUBLE_NOTE_DETAIL_JSON,
                new Gson().toJson(detail));
        startActivityForResult(intent, QuestionDetailEditActivity.REQUEST_CODE_EDIT_CHILD);
    }

    void publish(final int position) {
        SoapAsyncTask task = new SoapAsyncTask(SoapClient.PUBLISH_TROUBLE_NOTE_METHOD, new SoapAsyncTask.OnSoapTaskListener() {
            @Override
            public void onTaskExecuted(SoapTaskResult result) {
                progressDialog.dismiss();
                if(result.Result == SoapTaskResult.TaskResultType.Success) {
                    mList.remove(position);
                    adapter.notifyItemRemoved(position);
                    Snackbar.make(getApplication().getMainCoordinatorLayout(),
                            getResources().getString(R.string.trouble_note_publish_success),
                            Snackbar.LENGTH_LONG).show();
                }
                else {
                    Snackbar.make(getApplication().getMainCoordinatorLayout(),
                            result.Message,
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });
        progressDialog = new CustomProgressDialog(getActivity());
        progressDialog.show();
        task.execute(adapter.getItem(position).TroubleNoteID);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        MenuItem plus = menu.add(Menu.NONE, 1, 1, "");
        plus.setIcon(IconicsHelper.getIcon(getActivity(), CommunityMaterial.Icon.cmd_plus, 16, 0, Color.WHITE));
        MenuItemCompat.setShowAsAction(plus, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case 1:
                add();
                break;
        }
        return true;
    }

    @Override
    protected String onGetSoapMethodName() {
        return SoapClient.GET_TROUBLE_NOTES_METHOD;
    }

    @Override
    protected void onSoapAsyncTaskExecute(SoapAsyncTask task) {
        task.execute(getUser().UserID , Integer.toString(TroubleNoteStatus.DRAFT), "0", "100");
    }

    @Override
    protected void onLoading() {

    }

    @Override
    protected void onLoaded() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onLoadSuccess(JSONObject result) {
        try {
            int start = mList.size();
            String dataString = result.getString("data");
            Type type = new TypeToken<List<TroubleNote>>(){}.getType();
            List<TroubleNote> current = new Gson().fromJson(dataString, type);
            mList.addAll(current);
            if(start == 0)
                adapter.notifyDataSetChanged();
            else
                adapter.notifyItemRangeInserted(start, current.size());
            swipeMenuRecyclerView.loadMoreFinish(false, false);
        }
        catch (Exception ex) {
            swipeMenuRecyclerView.loadMoreError(-1, ex.getMessage());
        }
    }


    @Override
    protected void onLoadFailed(int code, String message) {
        swipeMenuRecyclerView.loadMoreError(code, message);
    }

    @Override
    protected void onLoadException(String message) {
        swipeMenuRecyclerView.loadMoreError(-1, message);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case QuestionEditActivity.REQUEST_CODE_ADD_QUESTION:
                if(resultCode == RESULT_OK)
                    swipeRefreshLayout.setRefreshing(true);
                break;
            case QuestionEditActivity.REQUEST_CODE_EDIT_QUESTION:
                if(resultCode == RESULT_OK)
                    swipeRefreshLayout.setRefreshing(true);
                break;
            case QuestionDetailEditActivity.REQUEST_CODE_ADD_CHILD:
                if(resultCode == RESULT_OK)
                    swipeRefreshLayout.setRefreshing(true);
                break;
            case QuestionDetailEditActivity.REQUEST_CODE_EDIT_CHILD:
                if(resultCode == RESULT_OK)
                    swipeRefreshLayout.setRefreshing(true);
                break;
            case QuestionDetailActivity.REQUEST_CODE:
                if(data != null && resultCode == 30) {
                    // replaces child of note by changed
                    adapter.getItem(mPosition).Children[mChildPosition] =
                            new Gson().fromJson(data.getStringExtra(QuestionDetailActivity.RESULT_JSON), TroubleNoteDetail.class);
                    adapter.notifyItemChanged(mPosition);
                    final QuestionListRecyclerViewAdapter.QuestionViewHolder holder =
                            (QuestionListRecyclerViewAdapter.QuestionViewHolder)swipeMenuRecyclerView.findViewHolderForAdapterPosition(mPosition);
                    if(holder != null) {
                        swipeMenuRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                holder.expand();
                            }
                        });

                    }


                }
                break;
        }
    }
}
