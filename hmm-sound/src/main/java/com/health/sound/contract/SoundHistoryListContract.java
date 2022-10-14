package com.health.sound.contract;


import com.health.sound.model.SoundHistory;
import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.PageInfoEarly;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/29 17:59
 * @des
 */

public interface SoundHistoryListContract {
    interface View extends BaseView {
        void successGetHistoryAlbum(List<SoundHistory> soundAlbumList, PageInfoEarly pageInfoEarly);
    }

    interface Presenter extends BasePresenter {

        void getHistoryAlbum(Map<String, Object> map);
    }
}
