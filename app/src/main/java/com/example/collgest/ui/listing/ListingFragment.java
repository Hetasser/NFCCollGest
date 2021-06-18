package com.example.collgest.ui.listing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.collgest.R;

public class ListingFragment extends Fragment {

    private ListingViewModel listingViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        listingViewModel =
                new ViewModelProvider(this).get(ListingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_listing, container, false);
        return root;
    }
}