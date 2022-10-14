package com.health.sound.contract;


import com.health.sound.model.SoundIndex;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/29 17:59
 * @des
 */

public interface SoundIndexContract {
    interface View extends BaseView {
        void successGetSoundIndex(List<SoundIndex> soundIndices);
    }

    interface Presenter extends BasePresenter {

        void getSoundIndex(Map<String, Object> map);
    }
}
