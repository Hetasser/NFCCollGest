package com.example.collgest.ui.checkinout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.collgest.R;

public class CheckinoutFragment extends Fragment {
    private CheckinoutViewModel checkinoutViewModel;
    private TextView textView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        System.out.println("**********************************************************************");
        System.out.println("CheckinoutFragment");
        System.out.println("**********************************************************************");
        checkinoutViewModel =
                new ViewModelProvider(this).get(CheckinoutViewModel.class);
        View root = inflater.inflate(R.layout.fragment_checkinout, container, false);
         textView = root.findViewById(R.id.text_checkinout);
        checkinoutViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    public void updateCheckinoutmText(String newmText){
        textView.setText(newmText);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkinoutViewModel = new ViewModelProvider(requireActivity()).get(CheckinoutViewModel.class);

        //
         /*

        items.setOnClickListener(item -> {
            // Set a new item
            viewModel.select(item);
        });*/
    }


}