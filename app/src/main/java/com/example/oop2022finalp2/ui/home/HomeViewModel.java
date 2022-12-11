package com.example.oop2022finalp2.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    /**
     * @author Yuntao Jiang
     * @description:
     * @date :2022/12/03 13:09
     *
     **/
    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    /**
     * @author Yuntao Jiang
     * @description:
     * @date :2022/12/03 13:09
     *
     **/
    public LiveData<String> getText() {
        return mText;
    }
}