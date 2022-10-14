package com.health.sound.contract;


import com.healthy.library.model.SoundHomeSplit;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.Map;

/**
 * @author Li
 * @date 2019/03/29 17:59
 * @des
 */

public interface SoundHomeContract {
    interface View extends BaseView {
        void successGetSoundAlbumUp(SoundHomeSplit soundHomeSplit);
        void successGetSoundAlbumDown(SoundHomeSplit soundHomeSplit);
    }

    interface Presenter extends BasePresenter {

        void getSoundAlbumsUp(Map<String, Object> map);
        void getSoundAlbumsDown(Map<String, Object> map);
    }
}
