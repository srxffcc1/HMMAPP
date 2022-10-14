//package com.health.client.presenter;
//
//import android.app.Application;
//import android.util.Base64;
//
//import com.health.client.R;
//import com.healthy.library.LibApplication;
//import com.healthy.library.constant.Functions;
//import com.healthy.library.constant.SpKey;
//import com.healthy.library.net.NoInsertStringObserver;
//import com.healthy.library.net.RetrofitHelper;
//import com.healthy.library.utils.SpUtils;
//import com.ximalaya.ting.android.opensdk.model.PlayableModel;
//import com.ximalaya.ting.android.opensdk.model.live.radio.Radio;
//import com.ximalaya.ting.android.opensdk.model.live.schedule.Schedule;
//import com.ximalaya.ting.android.opensdk.model.track.CommonTrackList;
//import com.ximalaya.ting.android.opensdk.model.track.Track;
//import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
//import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
//import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;
//import com.yhao.floatwindow.FloatWindow;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
//import io.reactivex.rxjava3.schedulers.Schedulers;
//
//public class IXmPlayerStatusListenerImp implements IXmPlayerStatusListener {
//    Application application;
//
//    public IXmPlayerStatusListenerImp(Application application) {
//        this.application = application;
//    }
//
//    @Override
//    public void onPlayStart() {
//        System.out.println("播放开始");
//        CommonTrackList infoList = XmPlayerManager.getInstance(application).getCommonTrackList();
//        PlayableModel info = XmPlayerManager.getInstance(application).getCurrSound();
//        if (info != null) {
//            if (info instanceof Track) {
//
//                Track track = (Track) info;
//                if (infoList.getTracks().size() == 1) {
//                    try {
//                        SoundHolder.getInstance().floatSoundNext.setImageResource(R.drawable.flaot_sound_next_g);
//                        SoundHolder.getInstance().floatSoundPre.setImageResource(R.drawable.flaot_sound_pre_g);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                } else {
//                    try {
//                        SoundHolder.getInstance().floatSoundNext.setImageResource(R.drawable.flaot_sound_next);
//                        SoundHolder.getInstance().floatSoundPre.setImageResource(R.drawable.flaot_sound_pre);
//                        if (track.equals(infoList.getTracks().get(infoList.getTracks().size() - 1))) {//最后一集
//                            SoundHolder.getInstance().floatSoundNext.setImageResource(R.drawable.flaot_sound_next_g);
//                        }
//                        if (track.equals(infoList.getTracks().get(0))) {//第一集
//                            SoundHolder.getInstance().floatSoundPre.setImageResource(R.drawable.flaot_sound_pre_g);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//
//                if (SoundHolder.getInstance().soundAlbum != null) {
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("memberId", new String(Base64.decode(SpUtils.getValue(application, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
//                    map.put("tracksId", track.getDataId() + "");
//                    map.put("albumsId", SoundHolder.getInstance().soundAlbum.id + "");
//                    if (track.getTrackTitle() != null) {
//
//                        map.put("title", track.getTrackTitle() + "");
//                    } else {
//
//                        map.put("title", SoundHolder.getInstance().soundAlbum.album_title + "");
//                    }
//                    map.put(Functions.FUNCTION, "8071");
//                    RetrofitHelper.createService(LibApplication.getAppContext())
//                            .getData(map)
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(new NoInsertStringObserver(null, application, false) {
//                                @Override
//                                protected void onGetResultSuccess(String obj) {
//                                    super.onGetResultSuccess(obj);
//
//                                }
//
//                                @Override
//                                protected void onFinish() {
//                                    super.onFinish();
//                                }
//                            });
//                }
//
//            }
//        }
//
//
//        try {
//            SoundHolder.getInstance().floatSoundPlay.setImageResource(R.drawable.flaot_sound_pause);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onPlayPause() {
//        try {
//            SoundHolder.getInstance().floatSoundPlay.setImageResource(R.drawable.flaot_sound_play);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onPlayStop() {
//        try {
//            SoundHolder.getInstance().floatSoundPlay.setImageResource(R.drawable.flaot_sound_play);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onSoundPlayComplete() {
//        try {
//            FloatWindow.get().hide();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            SoundHolder.getInstance().floatSoundPlay.setImageResource(R.drawable.flaot_sound_play);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onSoundPrepared() {
//
//    }
//
//    @Override
//    public void onSoundSwitch(PlayableModel playableModel, PlayableModel playableModel1) {
//
//    }
//
//    @Override
//    public void onBufferingStart() {
//        try {
//            SoundHolder.getInstance().soundProgress.setIndeterminate(false);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onBufferingStop() {
//
//    }
//
//    @Override
//    public void onBufferProgress(int i) {
//        try {
//            SoundHolder.getInstance().soundProgress.setIndeterminate(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onPlayProgress(int currPos, int duration) {
//        try {
//            SoundHolder.getInstance().soundProgress.setIndeterminate(false);
//            SoundHolder.getInstance().soundProgress.setProgress((int) (100 * currPos / (float) duration));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        String title = "";
//        PlayableModel info = XmPlayerManager.getInstance(application).getCurrSound();
//        if (info != null) {
//            if (info instanceof Track) {
//                title = ((Track) info).getTrackTitle();
//            } else if (info instanceof Schedule) {
//                title = ((Schedule) info).getRelatedProgram().getProgramName();
//            } else if (info instanceof Radio) {
//                title = ((Radio) info).getRadioName();
//            }
//        }
//        try {
//            SoundHolder.getInstance().floatName.setText(title);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public boolean onError(XmPlayerException e) {
//        return false;
//    }
//};
