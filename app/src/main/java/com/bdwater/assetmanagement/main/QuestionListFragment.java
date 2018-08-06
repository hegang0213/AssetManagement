package com.bdwater.assetmanagement.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bdwater.assetmanagement.R;
import com.bdwater.assetmanagement.common.AsyncSoapNoUIFragment;
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
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionListFragment extends AsyncSoapNoUIFragment {

    int mPosition = -1;
    int mChildPosition = -1;

    static final int PAGE_SIZE = 10;
    List<TroubleNote> mList = new ArrayList<>();
    QuestionListRecyclerViewAdapter adapter;

    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.swipeMenuRecyclerView)
    public SwipeMenuRecyclerView swipeMenuRecyclerView;

    public QuestionListFragment() {
        // Required empty public constructor
    }


    public static QuestionListFragment newInstance() {
        QuestionListFragment fragment = new QuestionListFragment();
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
        View view = inflater.inflate(R.layout.fragment_question_list, container, false);
        ButterKnife.bind(this, view);
        adapter = new QuestionListRecyclerViewAdapter(mList);
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
        public void onEditClick(View view, int position) {}

        @Override
        public void onPublishClick(View view, int position) {

        }

        @Override
        public void onDeleteClick(View view, int position) {

        }

        @Override
        public void onAddChildClick(View view, int position) {}

        @Override
        public void onChildEditClick(View view, int position, int childIndex) {}

        @Override
        public void onChildClick(View view, int position, int childIndex) {
            mPosition = position;
            mChildPosition = childIndex;
            TroubleNote note = adapter.getItem(position);
            TroubleNoteDetail detail = adapter.getChildItem(position, childIndex);
            openChild(note, detail);
        }
    };

    void openChild(TroubleNote note, TroubleNoteDetail detail) {
        Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
        intent.putExtra(QuestionDetailActivity.PARAM_TROUBLE_NOTE_DETAIL_JSON,
                new Gson().toJson(detail));
        startActivityForResult(intent, QuestionDetailActivity.REQUEST_CODE);
    }


    @Override
    protected String onGetSoapMethodName() {
        return SoapClient.GET_TROUBLE_NOTES_METHOD;
    }

    @Override
    protected void onSoapAsyncTaskExecute(SoapAsyncTask task) {
        task.execute(getUser().UserID, Integer.toString(TroubleNoteStatus.PROCESSING), "0", "100");
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

        //swipeMenuRecyclerView.loadMoreFinish(false, false);
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
