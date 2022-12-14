package com.health.index.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.index.R;
import com.health.index.contract.ToolsTestContract;
import com.health.index.presenter.ToolsTestPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.dialog.DateDialogUnder;
import com.healthy.library.dialog.LunarDateDialog;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.interfaces.OnDateConfirmListener;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.SpanUtils;
import com.healthy.library.widget.CommonInsertSection;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Route(path = IndexRoutes.INDEX_TOOLS_TEST_SEX_EXL)
public class ToolsTestSexExlActivity extends BaseActivity implements IsNeedShare,ToolsTestContract.View {
    private DateDialogUnder mBornDateDialog;
    private LunarDateDialog lunarDateDialog;
    private com.healthy.library.widget.TopBar topBar;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private com.healthy.library.widget.CommonInsertSection Monbrithday;
    private com.healthy.library.widget.CommonInsertSection Babybrithday;
    private android.widget.TextView tooosUseCount;
    private android.widget.TextView textTitle;
    private TextView endTxtM;
    private TextView endTxtB;
    private String MONTH;
    private int YEAR;
    ToolsTestPresenter toolsTestPresenter;

    private String surl;
    private String sdes;
    private String stitle;
    private Bitmap sBitmap;


    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_tools_test_exl;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        toolsTestPresenter=new ToolsTestPresenter(this,this);
        toolsTestPresenter.getBaseWithHMM(new SimpleHashMapBuilder<String, Object>()
                .puts("type","1")
                .puts(Functions.FUNCTION,"9044"));

        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
//                StringDialog.newInstance().setUrl(SpanUtils.getBuilder(mContext,"????????????").setForegroundColor(Color.parseColor("#222222")).setBold()
//                        .append("???????????????????????????,??????????????????????????????????????????????????????????????????????????????????????????????????????????????????\n").setForegroundColor(Color.parseColor("#222222"))
//                        .append("??????????????????????????????????????????????????????18???45?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????\n").setForegroundColor(Color.parseColor("#222222"))
//                        .append("??????????????????????????????????????????(??????????????????1???)????????????????????????????????????????????????????????????????????????\n").setForegroundColor(Color.parseColor("#222222"))
//                        .create()).setTitle("?????????").show(getSupportFragmentManager(),"xiaozhishi");
                showShare();
            }
        });
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void initView() {

        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        Monbrithday = (CommonInsertSection) findViewById(R.id.Monbrithday);
        Babybrithday = (CommonInsertSection) findViewById(R.id.Babybrithday);
        tooosUseCount = (TextView) findViewById(R.id.tooos_UseCount);
        textTitle = (TextView) findViewById(R.id.textTitle);
        endTxtM = Monbrithday.findViewById(R.id.endTxt);
        endTxtB = Babybrithday.findViewById(R.id.endTxt);



        Monbrithday.getmTxtTitle().setTextSize(TypedValue.COMPLEX_UNIT_SP,16f);
        Babybrithday.getmTxtTitle().setTextSize(TypedValue.COMPLEX_UNIT_SP,16f);


        ConstraintLayout.LayoutParams pam= (ConstraintLayout.LayoutParams) Monbrithday.getmTxtTitle().getLayoutParams();
        pam.width=ConstraintLayout.LayoutParams.WRAP_CONTENT;
        Monbrithday.getmTxtTitle().setLayoutParams(pam);

        ConstraintLayout.LayoutParams pam2= (ConstraintLayout.LayoutParams) Babybrithday.getmTxtTitle().getLayoutParams();
        pam2.width=ConstraintLayout.LayoutParams.WRAP_CONTENT;
        Babybrithday.getmTxtTitle().setLayoutParams(pam2);

    }
    public static  Date parse(String strDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(strDate);
    }

    /**
     * ??????????????????
     *
     * @param view view
     */
    public void chooseButtonLeft(View view) {
        if (lunarDateDialog == null) {

            lunarDateDialog = LunarDateDialog.newInstance("???????????????????????????"
            );
        }
        lunarDateDialog.setItemClickCallBack(new LunarDateDialog.ItemClickCallBack() {
            @Override
            public void click(String id, String name) {
                MONTH=id;
                endTxtB.setText(name);
            }

            @Override
            public void dismiss() {

            }
        });
        lunarDateDialog.show(getSupportFragmentManager(), "lunarDate");


    }
    public void chooseButtonOK(View view) {
        if(checkIllegal()){
            toolsTestPresenter.getBaseWithAL("http://aigender.market.alicloudapi.com/ai_imedical/guess_gender/qing_gong_biao/v1"
                    ,new SimpleHashMapBuilder<String, Object>()
                            .puts("MONTH",MONTH+"")
                            .puts("YEAR",YEAR+"")
                    ,new SimpleHashMapBuilder<String, Object>()
                            .puts("Authorization","APPCODE "+"8639276d3616459fab0a609326bf6ffb"));
        }
    }
    private boolean checkIllegal() {
        if(TextUtils.isEmpty(endTxtM.getText())){
            Toast.makeText(mContext,"???????????????",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(endTxtB.getText())){
            Toast.makeText(mContext,"?????????????????????",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(YEAR<18){
            Toast.makeText(mContext,"????????????,?????????????????????????????????",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public void chooseButtonRight(View view) {
        if (mBornDateDialog == null) {
            long currentMill = System.currentTimeMillis();
            long duration = 0;
            try {
                duration = new SimpleDateFormat("yyyy-MM-dd").parse("1976-1-1").getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mBornDateDialog = DateDialogUnder.newInstance(
                    currentMill, duration, currentMill, "?????????????????????"
            );
            mBornDateDialog.setOnConfirmClick(new OnDateConfirmListener() {
                @Override
                public void onConfirm(int pos, Date date) {
                    YEAR= DateUtils.getAge(date,new Date());
                    endTxtM.setText(new SimpleDateFormat("yyy-MM-dd").format(date));
                }
            });
        }
        mBornDateDialog.show(getSupportFragmentManager(), "bornDate");
    }

    @Override
    public void onSucessGetBaseHMM(String result) {
        //System.out.println(result);
        try {
            JSONObject jsonObject=new JSONObject(result);
            tooosUseCount.setText(SpanUtils.getBuilder(mContext,"????????? ").setForegroundColor(Color.parseColor("#763749"))
                    .append(jsonObject.optInt("data")+"").setForegroundColor(Color.parseColor("#FF6266")).setProportion(1.4f)
                    .append(" ????????????????????????").setForegroundColor(Color.parseColor("#763749")).setBold()
                    .create());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSucessUpdateBaseHMM(String result) {

    }

    @Override
    public void onSucessGetAL(String result) {
        toolsTestPresenter.getBaseWithHMM(new SimpleHashMapBuilder<String, Object>()
                .puts("type","1")
                .puts(Functions.FUNCTION,"9045"));
        try {
            finish();
            JSONObject jsonObject=new JSONObject(result);
            String sex=jsonObject.optString("????????????");
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_TEST_SEX_RESULT)
                    .withString("sex",sex)
                    .withString("vcType",1+"")
                    .navigation();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getSurl() {
        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_babyNameUrl);
        String url = String.format("%s?type=1&vcType="+1, urlPrefix);
        surl = url;
        return surl;
    }

    @Override
    public String getSdes() {
        return "?????????????????????????????????????????????????????????~";
    }

    @Override
    public String getStitle() {
        return "???????????? - ?????????";
    }

    @Override
    public Bitmap getsBitmap() {
        return null;
    }
}
