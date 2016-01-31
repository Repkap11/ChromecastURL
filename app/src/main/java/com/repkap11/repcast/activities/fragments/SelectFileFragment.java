package com.repkap11.repcast.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.repkap11.repcast.R;
import com.repkap11.repcast.activities.SelectFileActivity;
import com.repkap11.repcast.model.FileListAdapter;
import com.repkap11.repcast.model.JsonDirectory;


public class SelectFileFragment extends Fragment {

    private static final String TAG = SelectFileFragment.class.getSimpleName();
    private static final String INSTANCE_STATE_DIR = "INSTANCE_STATE_DIR";
    private FileListAdapter mAdapter;
    private AbsListView mListView;
    private JsonDirectory.JsonFileDir mDirectory;

    public SelectFileFragment() {
        Log.e(TAG, "Fragment Created");

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mDirectory = savedInstanceState.getParcelable(INSTANCE_STATE_DIR);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_selectfile, container, false);
        rootView.setKeepScreenOn(true);
        mListView = (AbsListView) rootView.findViewById(R.id.fragment_selectfile_list);
        setRetainInstance(true);

        if (mAdapter == null) {
            mAdapter = new FileListAdapter(mDirectory.path64);
        }
        mAdapter.updateContext((SelectFileActivity) getActivity());
        mListView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(INSTANCE_STATE_DIR, mDirectory);
        super.onSaveInstanceState(outState);
    }

    public void showListUsingDirectory(JsonDirectory.JsonFileDir dir) {
        mDirectory = dir;
        mAdapter = new FileListAdapter(dir.path64);
    }

    public void searchFile(String string) {
        mAdapter.getFilter().filter(string);
    }

    public String getDirectoryName() {
        return mDirectory.name;
    }
}
