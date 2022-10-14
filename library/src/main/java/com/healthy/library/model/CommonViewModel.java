package com.healthy.library.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @author Li
 * @date 2019/04/04 17:03
 * @des
 */

public class CommonViewModel extends ViewModel {
    private MutableLiveData<String> mTMutableLiveData;

    public MutableLiveData<String> getModel() {
        if (mTMutableLiveData == null) {
            mTMutableLiveData = new MutableLiveData<>();
        }
        return mTMutableLiveData;
    }

}
