package com.example.collgest.ui.listing;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.collgest.db.CollGestDBHelper;
import com.example.collgest.db.CollGestItem;

import com.example.collgest.R;

import java.util.Arrays;
import java.util.List;

public class ListingFragment extends Fragment {
    private Context activityContext;

    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 1;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;


    protected RecyclerView mRecyclerView;
    protected GameListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected String[][] mDataset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
        activityContext = this.getActivity();
        initDataset();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_listing, container, false);
        rootView.setTag(TAG);

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.listing_recyclerview);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        //mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager = new GridLayoutManager(getActivity(),SPAN_COUNT);

        mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        mAdapter = new GameListAdapter(mDataset);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // END_INCLUDE(initializeRecyclerView)
        return rootView;
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        if (layoutManagerType == LayoutManagerType.GRID_LAYOUT_MANAGER) {
            mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
            mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
        } else {
            mLayoutManager = new LinearLayoutManager(getActivity());
            mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }


    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {
        CollGestDBHelper collGestDBHelper = new CollGestDBHelper(activityContext);
        List<CollGestItem> listAllItems = collGestDBHelper.getAllGestItem();

        mDataset = new String[listAllItems.size()][7];
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++    +++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(listAllItems.size());
        System.out.println(listAllItems.toString());
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++    +++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        for (int i = 0; i < listAllItems.size(); i++) {
            mDataset[i][0] = listAllItems.get(i).getItemName();
            mDataset[i][1] = Integer.toString(listAllItems.get(i).getItemMinJoueurs());
            mDataset[i][2] = Integer.toString(listAllItems.get(i).getItemMaxJoueurs());
            mDataset[i][3] = Integer.toString(listAllItems.get(i).getItemDuration());
            mDataset[i][4] = listAllItems.get(i).getItemTypes();
            mDataset[i][5] = listAllItems.get(i).getItemCheckedOut();
            mDataset[i][6] = listAllItems.get(i).getItemLastPlayed();
        }
        System.out.println(mDataset);
    }
}