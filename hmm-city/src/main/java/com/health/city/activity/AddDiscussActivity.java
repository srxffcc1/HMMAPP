package com.health.city.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.city.R;
import com.health.city.contract.AddDiscussContract;
import com.healthy.library.model.DiscussStore;
import com.health.city.model.UserInfoCityModel;
import com.health.city.presenter.AddDiscussPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.routes.CityRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.TopBar;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 写评论 写回复
 */
@Route(path = CityRoutes.CITY_ADDDIS)
public class AddDiscussActivity extends BaseActivity implements AddDiscussContract.View, OnSubmitListener {

    @Autowired
    String postingId;
    @Autowired
    String memberState;

    @Autowired
    String activityType;//评论 回复


    @Autowired
    String postingDiscussId;
    @Autowired
    String toMemberId;
    @Autowired
    String toMemberType;
    @Autowired
    String fatherId;
    private int num = 100;

    UserInfoCityModel userInfoCityModel;
    private com.healthy.library.widget.TopBar topBar;
    private android.widget.EditText chatContent;
    AddDiscussPresenter addDiscussPresenter;
    @Override
    public void onSuccessAdd() {
        setResult(Activity.RESULT_OK);
        super.finish();
    }

    @Override
    public void onSuccessGetMine(UserInfoCityModel userInfoCityModel) {
        this.userInfoCityModel=userInfoCityModel;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.city_activity_senddiscuss;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        addDiscussPresenter=new AddDiscussPresenter(mContext,this);
        addDiscussPresenter.getMine();

        if (!TextUtils.isEmpty(SpUtils.getValue(mContext, SpKey.DISCUSS_TMP))) {
            String jsonstring = SpUtils.getValue(mContext, SpKey.DISCUSS_TMP);
            DiscussStore postStore = resolveTmpData(jsonstring);
            chatContent.setText(postStore.discussContent);
        }


        chatContent.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                temp = s;
                //System.out.println("s=" + s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                int number = num - s.length();
//                tv_num.setText("" + number);
                selectionStart = chatContent.getSelectionStart();
                selectionEnd = chatContent.getSelectionEnd();
                ////System.out.println("start="+selectionStart+",end="+selectionEnd);
                if (temp.length() > num) {
                    showToast("限制输入" + num + "个字");
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionStart;
                    chatContent.setText(s);
                    chatContent.setSelection(tempSelection);//设置光标在最后
                }
            }
        });
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
        topBar.setSubmitListener(this);
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        chatContent = (EditText) findViewById(R.id.chatContent);
    }

    @Override
    public void onSubmitBtnPressed() {

        if(!TextUtils.isEmpty(chatContent.getText().toString())){
            Map<String,Object> map=new HashMap<>();

            if("回复".equals(activityType)){
                map.put("postingDiscussId",postingDiscussId);
                map.put("toMemberId",toMemberId);
                map.put("toMemberType",toMemberType);
                map.put("fatherId",fatherId);
                map.put("status","0");
                map.put("content",chatContent.getText().toString());
                addDiscussPresenter.addReview2(map);
            }else {
                map.put("postingId",postingId);
                map.put("memberState",memberState);
                map.put("status","0");
                map.put("content",chatContent.getText().toString());
                addDiscussPresenter.addDiscuss2(map);
            }
        }else {
            Toast.makeText(mContext,"请输入内容",Toast.LENGTH_SHORT).show();
        }
    }

    private DiscussStore resolveTmpData(String obj) {
        DiscussStore result = null;
        try {
            JSONObject data = new JSONObject(obj);
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<DiscussStore>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void finish() {
        if (!TextUtils.isEmpty(chatContent.getText().toString())) {
            StyledDialog.init(this);
            StyledDialog.buildIosAlert("", "是否保存草稿?", new MyDialogListener() {
                @Override
                public void onFirst() {
                    SpUtils.store(mContext, SpKey.DISCUSS_TMP, "");
                    AddDiscussActivity.super.finish();
                }

                @Override
                public void onThird() {
                    super.onThird();

                }

                @Override
                public void onSecond() {
                    DiscussStore postStore = new DiscussStore();
                    postStore.setDiscussContent(chatContent.getText().toString());
                    String result = new Gson().toJson(postStore);
                    SpUtils.store(mContext, SpKey.DISCUSS_TMP, result);
                    AddDiscussActivity.super.finish();


                }
            }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("不保存", "保存").show();
        } else {
            AddDiscussActivity.super.finish();
        }


    }
}
