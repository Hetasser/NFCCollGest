package com.example.collgest.ui.addItem;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class addItemViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public addItemViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is checkout fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

}

