package com.health.sound.contract;


import com.healthy.library.model.SoundTypeSplit;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.Map;

/**
 * @author Li
 * @date 2019/03/29 17:59
 * @des
 */

public interface SoundTypeContract {
    interface View extends BaseView {
        void successGetSoundAlbum(SoundTypeSplit soundHomeSplit);
    }

    interface Presenter extends BasePresenter {

        void getSoundAlbums(Map<String, Object> map);
    }
}
