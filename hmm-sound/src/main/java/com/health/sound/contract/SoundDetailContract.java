package com.health.sound.contract;


import com.healthy.library.base.BasePresenter;
import com.healthy.library.base.BaseView;
import com.healthy.library.model.SoundAlbum;

import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/29 17:59
 * @des
 */

public interface SoundDetailContract {
    interface View extends BaseView {
        void successGetSoundAlbums(List<SoundAlbum> soundAlbumList);
        void successGetSoundAlbumsCollectStatus(String status);
        void successSubAlbums(String status);
    }

    interface Presenter extends BasePresenter {
        void subAlbums(Map<String, Object> map);
        void getSoundAlbums(Map<String, Object> map);
        void getAlbumsCollectStatus(Map<String, Object> map);
    }
}
