package com.example.collgest.ui.checkinout;

import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class CheckinoutViewModel extends ViewModel{

    private MutableLiveData<String> mText;
    private MutableLiveData<EditText> editText;

    public CheckinoutViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("please scan a nfc tag");

    }
    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<EditText> getSelectedItem() {
        return editText;
    }





}