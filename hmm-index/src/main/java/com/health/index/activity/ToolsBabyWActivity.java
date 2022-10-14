package com.health.index.activity;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.index.R;
import com.health.index.contract.ToolsTestContract;
import com.healthy.library.model.ToolsBabyBase;
import com.healthy.library.model.ToolsBabyModel;
import com.health.index.presenter.ToolsTestPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.dialog.SingleWheelDialog;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.SpanUtils;
import com.healthy.library.widget.CommonInsertSection;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.StringDialog;
import com.healthy.library.widget.TopBar;
import com.lihang.ShadowLayout;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = IndexRoutes.INDEX_TOOLS_BABY_WEIGHT)
public class ToolsBabyWActivity extends BaseActivity implements IsNeedShare, IsFitsSystemWindows, ToolsTestContract.View {
    private com.healthy.library.widget.TopBar topBar;
    private android.widget.ImageView babyIcon;
    private com.lihang.ShadowLayout mShadowLayout;
    private android.widget.LinearLayout babyLL;
    private com.healthy.library.widget.CommonInsertSection shuangdingjingLL;
    private com.healthy.library.widget.CommonInsertSection guguchangLL;
    private com.healthy.library.widget.CommonInsertSection fuweiyaochangLL;
    private com.healthy.library.widget.CommonInsertSection taierzhoushuLL;
    private android.widget.TextView babyUTitle;
    private android.widget.TextView babyUTxt;

    ImageView shuangdingjingImg;
    ImageView guguchangImg;
    ImageView fuweiyaochangImg;
    ImageView taierzhoushuImg;

    EditText shuangdingjing;
    EditText guguchang;
    EditText fuweiyaochang;
    TextView taierzhoushu;

    private SingleWheelDialog mBornTypeDialog;
    ToolsTestPresenter toolsTestPresenter;
    String passurl = "";
    private TextView toolsBabyPTitle;
    private TextView toolsBabyPWeight;
    private TextView toolsBabyPUnit;
    private TextView toolsBabyPResult;
    private TextView toolsBabyPWeek;
    private TextView toolsBabyPWeekTip;
    private com.healthy.library.widget.ImageTextView toolsBabyPKey1;
    private com.healthy.library.widget.ImageTextView toolsBabyPKey2;
    private com.healthy.library.widget.ImageTextView toolsBabyPKey3;
    private TextView toolsBabyPV1;
    private TextView toolsBabyPV2;
    private TextView toolsBabyPV3;

    boolean isresult = false;
    private ToolsBabyModel toolsBabyModel;

    public List<ToolsBabyBase> toolsBabyBaseList = new ArrayList<>();


    private AlertDialog mShareDialog;
    String surl;
    String sdes;
    String stitle;

    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_tools_babyweight;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        clearAll();

        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                if (isresult) {
                    buildDes();
                    showShare();
                } else {
                    StringDialog.newInstance().setUrl(SpanUtils.getBuilder(mContext, "双顶径（BPD）：").setForegroundColor(Color.parseColor("#222222")).setBold()
                            .append("又称胎头双顶径，是指胎儿头部左右两侧之间最宽部位的长度。医生常用它来观察孩子发育的情况，判断头盆是否不称，能否顺利分娩。\n").setForegroundColor(Color.parseColor("#222222"))
                            .append("股骨长(FL)：").setForegroundColor(Color.parseColor("#222222")).setBold()
                            .append("胎儿大腿骨长度，预测胎儿体质量的胎儿生物指标。\n").setForegroundColor(Color.parseColor("#222222"))
                            .append("腹围周长(AC)：").setForegroundColor(Color.parseColor("#222222")).setBold()
                            .append("胎儿腹部一圈的长度，医生会根据腹围来预测胎儿的大小，是否适合顺产等等。\n").setForegroundColor(Color.parseColor("#222222"))
                            .create()).setTitle("小知识").show(getSupportFragmentManager(), "xiaozhishi");
                }
            }
        });
        toolsBabyBaseList.clear();
        toolsBabyBaseList.add(new ToolsBabyBase("2.52±0.25", "6.90±1.65", "1.17±0.31"));
        toolsBabyBaseList.add(new ToolsBabyBase("2.83±0.57", "7.77±1.82", "1.38±0.48"));
        toolsBabyBaseList.add(new ToolsBabyBase("3.23±0.51", "9.13±1.56", "1.74±0.58"));
        toolsBabyBaseList.add(new ToolsBabyBase("3.62±0.58", "10.32±1.92", "2.10±0.51"));
        toolsBabyBaseList.add(new ToolsBabyBase("3.97±0.44", "11.49±1.62", "2.52±0.44"));
        toolsBabyBaseList.add(new ToolsBabyBase("4.25±0.53", "12.41±1.89", "2.71±0.46"));
        toolsBabyBaseList.add(new ToolsBabyBase("4.52±0.53", "13.59±2.30", "3.03±0.50"));
        toolsBabyBaseList.add(new ToolsBabyBase("4.88±0.53", "14.80±1.89", "3.35±0.47"));
        toolsBabyBaseList.add(new ToolsBabyBase("5.22±0.42", "15.62±0.92", "3.64±0.40"));
        toolsBabyBaseList.add(new ToolsBabyBase("5.45±0.57", "16.70±2.23", "3.82±0.47"));
        toolsBabyBaseList.add(new ToolsBabyBase("5.80±0.44", "17.90±1.85", "4.21±0.41"));
        toolsBabyBaseList.add(new ToolsBabyBase("6.05±0.50", "18.74±2.23", "4.36±0.51"));
        toolsBabyBaseList.add(new ToolsBabyBase("6.39±0.70", "19.64±2.20", "4.65±0.42"));
        toolsBabyBaseList.add(new ToolsBabyBase("6.68±0.61", "21.62±2.30", "4.87±0.41"));
        toolsBabyBaseList.add(new ToolsBabyBase("6.98±0.57", "21.81±2.12", "5.10±0.41"));
        toolsBabyBaseList.add(new ToolsBabyBase("7.24±0.65", "22.86±2.41", "5.35±0.55"));
        toolsBabyBaseList.add(new ToolsBabyBase("7.50±0.65", "23.71±1.50", "5.61±0.44"));
        toolsBabyBaseList.add(new ToolsBabyBase("7.83±0.62", "24.88±2.03", "5.77±0.47"));
        toolsBabyBaseList.add(new ToolsBabyBase("8.06±0.60", "25.78±2.32", "6.03±0.38"));
        toolsBabyBaseList.add(new ToolsBabyBase("8.17±0.65", "26.20±2.23", "6.43±0.49"));
        toolsBabyBaseList.add(new ToolsBabyBase("8.50±0.47", "27.78±2.30", "6.52±0.46"));
        toolsBabyBaseList.add(new ToolsBabyBase("8.61±0.63", "27.99±2.55", "6.62±0.43"));
        toolsBabyBaseList.add(new ToolsBabyBase("8.70±0.55", "28.74±2.88", "6.71±0.45"));
        toolsBabyBaseList.add(new ToolsBabyBase("8.81±0.57", "29.44±2.83", "6.95±0.47"));
        toolsBabyBaseList.add(new ToolsBabyBase("9.00±0.63", "30.14±2.17", "7.10±0.52"));
        toolsBabyBaseList.add(new ToolsBabyBase("9.08±0.59", "30.63±2.83", "7.20±0.43"));
        toolsBabyBaseList.add(new ToolsBabyBase("9.21±0.59", "31.34±3.12", "7.34±0.53"));
        toolsBabyBaseList.add(new ToolsBabyBase("9.28±0.50", "31.49±2.79", "7.40±0.53"));
        passurl = "http://aiburn.market.alicloudapi.com/ai_imedical/child_increase/v1";
        toolsTestPresenter = new ToolsTestPresenter(this, this);
        shuangdingjing.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    shuangdingjingImg.setVisibility(View.VISIBLE);
                } else {
                    shuangdingjingImg.setVisibility(View.GONE);
                }
            }
        });
        shuangdingjingImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuangdingjing.setText("");
            }
        });
        shuangdingjing.addTextChangedListener(new DoubleStringTextWatcher(shuangdingjing, 2));
        guguchang.addTextChangedListener(new DoubleStringTextWatcher(guguchang, 2));
        fuweiyaochang.addTextChangedListener(new DoubleStringTextWatcher(fuweiyaochang, 2));
        guguchang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    guguchangImg.setVisibility(View.VISIBLE);
                } else {
                    guguchangImg.setVisibility(View.GONE);
                }
            }
        });
        guguchangImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guguchang.setText("");
            }
        });


        fuweiyaochang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    fuweiyaochangImg.setVisibility(View.VISIBLE);
                } else {
                    fuweiyaochangImg.setVisibility(View.GONE);
                }
            }
        });
        fuweiyaochangImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fuweiyaochang.setText("");
            }
        });

    }

    @Override
    public String getSurl() {
        return surl;
    }

    @Override
    public String getSdes() {
        return sdes;
    }

    @Override
    public String getStitle() {
        return stitle;
    }

    @Override
    public Bitmap getsBitmap() {
        return null;
    }

    class DoubleStringTextWatcher implements TextWatcher {
        EditText des;
        int doublesize;

        public DoubleStringTextWatcher(EditText des, int doublesize) {
            this.des = des;
            this.doublesize = doublesize;
        }

//        public DoubleStringTextWatcher(EditText des) {
//            this.des = des;
//        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            //删除.后面超过两位的数字
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > doublesize) {
                    s = s.toString().subSequence(0,
                            s.toString().indexOf(".") + doublesize + 1);
                    des.setText(s);
                    des.setSelection(s.length());
                }
            }

            //如果.在起始位置,则起始位置自动补0
            if (".".equals(s.toString().trim().substring(0))) {
                s = "0" + s;
                des.setText(s);
                des.setSelection(2);
            }

            //如果起始位置为0并且第二位跟的不是".",则无法后续输入
            if (s.toString().startsWith("0")
                    && s.toString().trim().length() > 1) {
                if (!".".equals(s.toString().substring(1, 2))) {
                    des.setText(s.subSequence(0, 1));
                    des.setSelection(1);
                    return;
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }
    }

    /**
     * 选择分娩方式
     *
     * @param view view
     */
    public void chooseBornType(View view) {
        if (mBornTypeDialog == null) {
            ArrayList<String> list = new ArrayList<>();
            for (int i = 13; i < 41; i++) {
                list.add(i + "");
            }
            mBornTypeDialog = SingleWheelDialog.newInstance(list, "请选择胎儿周数");
            mBornTypeDialog.setOnConfirmClick(new SingleWheelDialog.OnConfirmClickListener() {
                @Override
                public void onClick(int pos, String data) {
                    taierzhoushu.setText(data);
                }
            });
        }
        mBornTypeDialog.show(getSupportFragmentManager(), "bornType");
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        babyIcon = (ImageView) findViewById(R.id.babyIcon);
        mShadowLayout = (ShadowLayout) findViewById(R.id.mShadowLayout);
        babyLL = (LinearLayout) findViewById(R.id.babyLL);
        shuangdingjingLL = (CommonInsertSection) findViewById(R.id.shuangdingjingLL);
        guguchangLL = (CommonInsertSection) findViewById(R.id.guguchangLL);
        fuweiyaochangLL = (CommonInsertSection) findViewById(R.id.fuweiyaochangLL);
        taierzhoushuLL = (CommonInsertSection) findViewById(R.id.taierzhoushuLL);
        babyUTitle = (TextView) findViewById(R.id.babyUTitle);
        babyUTxt = (TextView) findViewById(R.id.babyUTxt);


        shuangdingjing = shuangdingjingLL.findViewById(R.id.endTxt);
        guguchang = guguchangLL.findViewById(R.id.endTxt);
        fuweiyaochang = fuweiyaochangLL.findViewById(R.id.endTxt);
        taierzhoushu = taierzhoushuLL.findViewById(R.id.endTxt);

        shuangdingjingImg = shuangdingjingLL.findViewById(R.id.endImg);
        guguchangImg = guguchangLL.findViewById(R.id.endImg);
        fuweiyaochangImg = fuweiyaochangLL.findViewById(R.id.endImg);
        taierzhoushuImg = taierzhoushuLL.findViewById(R.id.endImg);
        shuangdingjingLL.getmTxtTitle().setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
        guguchangLL.getmTxtTitle().setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
        fuweiyaochangLL.getmTxtTitle().setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
        taierzhoushuLL.getmTxtTitle().setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);


        shuangdingjing.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        guguchang.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        fuweiyaochang.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);


        toolsBabyPTitle = (TextView) findViewById(R.id.toolsBabyPTitle);
        toolsBabyPWeight = (TextView) findViewById(R.id.toolsBabyPWeight);
        toolsBabyPUnit = (TextView) findViewById(R.id.toolsBabyPUnit);
        toolsBabyPResult = (TextView) findViewById(R.id.toolsBabyPResult);
        toolsBabyPWeek = (TextView) findViewById(R.id.toolsBabyPWeek);
        toolsBabyPWeekTip = (TextView) findViewById(R.id.toolsBabyPWeekTip);
        toolsBabyPKey1 = (ImageTextView) findViewById(R.id.toolsBabyPKey1);
        toolsBabyPKey2 = (ImageTextView) findViewById(R.id.toolsBabyPKey2);
        toolsBabyPKey3 = (ImageTextView) findViewById(R.id.toolsBabyPKey3);
        toolsBabyPV1 = (TextView) findViewById(R.id.toolsBabyPV1);
        toolsBabyPV2 = (TextView) findViewById(R.id.toolsBabyPV2);
        toolsBabyPV3 = (TextView) findViewById(R.id.toolsBabyPV3);


    }

    public void clearAll() {
        isresult = false;
        mShadowLayout.setVisibility(View.GONE);
        babyLL.setVisibility(View.VISIBLE);
        shuangdingjing.setText("");
        guguchang.setText("");
        fuweiyaochang.setText("");
        taierzhoushu.setText("");
        topBar.getSubmitBack().setImageResource(R.drawable.tools_misc);
    }

    @Override
    public void onBackBtnPressed() {
        if (isresult) {
            clearAll();
        } else {
            super.onBackBtnPressed();
        }
    }

    private boolean checkIllegal() {
        if (TextUtils.isEmpty(shuangdingjing.getText().toString())) {
            showToastIgnoreLife("请输入双顶径");
            return false;
        }
        if (TextUtils.isEmpty(guguchang.getText().toString())) {
            showToastIgnoreLife("请输入股骨长");
            return false;
        }
        if (TextUtils.isEmpty(fuweiyaochang.getText().toString())) {
            showToastIgnoreLife("请输入腹围");
            return false;
        }
        if (TextUtils.isEmpty(taierzhoushu.getText().toString())) {
            showToastIgnoreLife("请选择周数");
            return false;
        }
        if (TextUtils.isEmpty(fuweiyaochang.getText().toString())) {
            showToastIgnoreLife("请输入腹围");
            return false;
        }
        if (!checkObjAboveDes(shuangdingjing.getText().toString(), 10)) {
            showToastIgnoreLife("请输入正确的双顶径0~10");
            return false;
        }
        if (!checkObjAboveDes(guguchang.getText().toString(), 10)) {
            showToastIgnoreLife("请输入正确的股骨长0~10");
            return false;
        }
        if (!checkObjAboveDes(fuweiyaochang.getText().toString(), 40)) {
            showToastIgnoreLife("请输入正确的腹围0~40");
            return false;
        }


        return true;
    }

    public boolean checkObjAboveDes(String obj, int des) {
        double objint = -1;
        try {
            objint = Double.parseDouble(obj);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (objint != -1 && objint != 0) {
            if (objint <= des) {
                return true;
            }
        }
        return false;
    }

    public void chooseButtonOK(View view) {

        if (checkIllegal()) {
            showLoading();
            Map<String, Object> map = new HashMap<>();
            map.put("FU_WEI", fuweiyaochang.getText().toString());
            map.put("GU_GU_CHANG", guguchang.getText().toString());
            map.put("SHUANG_DING_JIN", shuangdingjing.getText().toString());
            map.put("WEEK", taierzhoushu.getText().toString());
            toolsTestPresenter.getBaseWithAL(passurl, map, new SimpleHashMapBuilder<String, Object>()
                    .puts("Authorization", "APPCODE " + "8639276d3616459fab0a609326bf6ffb"));
        }


    }

    @Override
    public void onSucessGetBaseHMM(String result) {

    }

    @Override
    public void onSucessUpdateBaseHMM(String result) {

    }

    @Override
    public void onSucessGetAL(String result) {
        showContent();
        toolsBabyModel = resolveBabyData(result.replace("（", "").replace("）", ""));
        buildResult();
    }

    private void buildResult() {
        if(toolsBabyModel!=null){
            isresult = true;
            mShadowLayout.setVisibility(View.VISIBLE);
            babyLL.setVisibility(View.GONE);
            toolsBabyPWeight.setText(toolsBabyModel.宝宝体重测量指标.宝宝体重公斤 != null ? toolsBabyModel.宝宝体重测量指标.宝宝体重公斤 : "");
            toolsBabyPResult.setText(toolsBabyModel.宝宝发育状态评估);
            toolsBabyPWeek.setText(taierzhoushu.getText().toString());
            toolsBabyPV1.setText(toolsBabyBaseList.get(Integer.parseInt(taierzhoushu.getText().toString()) - 13).shuangdingjing);
            toolsBabyPV2.setText(toolsBabyBaseList.get(Integer.parseInt(taierzhoushu.getText().toString()) - 13).guguchang);
            toolsBabyPV3.setText(toolsBabyBaseList.get(Integer.parseInt(taierzhoushu.getText().toString()) - 13).fuweiyaochang);
            topBar.getSubmitBack().setImageResource(R.drawable.index_ic_share);
        }

    }

    private ToolsBabyModel resolveBabyData(String obj) {
        ToolsBabyModel result = null;
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
            Type type = new TypeToken<ToolsBabyModel>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    private void buildDes() {
        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_babyWeightUrl);
        String url = String.format("%s?fu=%s&gu=%s&ding=%s&week=%s", urlPrefix, fuweiyaochang.getText().toString(), guguchang.getText().toString(), shuangdingjing.getText().toString(), taierzhoushu.getText().toString());
        surl = url;
        stitle = "";

        stitle = "宝宝还在肚子里多重了？想知道就来测测吧！";

        sdes = "小数据大作用，B超单里玄机多, 憨妈妈教你方便快捷估算宝宝体重";

    }
    /**
     * 分享
     *
     * @param shareMedia 分享平台
     * @param url        链接地址
     * @param des        描述
     * @param title      标题
     */
}
