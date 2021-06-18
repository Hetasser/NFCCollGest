package com.example.collgest.ui.addItem;

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

public class addItemFragment extends Fragment {

    private com.example.collgest.ui.addItem.addItemViewModel addItemViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addItemViewModel =
                new ViewModelProvider(this).get(com.example.collgest.ui.addItem.addItemViewModel.class);
        View root = inflater.inflate(R.layout.fragment_additem, container, false);

        return root;
    }
}