package com.health.index.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.index.R;
import com.health.index.contract.ToolsTestContract;
import com.healthy.library.model.ToolsQuestion;
import com.health.index.presenter.ToolsTestPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.SpanUtils;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = IndexRoutes.INDEX_TOOLS_TEST_SEX)
public class ToolsTestSexActivity extends BaseActivity implements IsNeedShare, ToolsTestContract.View {

    private com.healthy.library.widget.TopBar topBar;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private android.widget.LinearLayout toolsQlist;
    private android.widget.TextView tooosUseCount;
    private android.widget.TextView textTitle;
    @Autowired
    String vcType;

    List<ToolsQuestion> toolsQuestions = new ArrayList<>();

    ToolsTestPresenter toolsTestPresenter;
    String passurl = "";


    private String surl;
    private String sdes;
    private String stitle;
    private Bitmap sBitmap;

    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_tools_test_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        toolsTestPresenter = new ToolsTestPresenter(this, this);
        toolsTestPresenter.getBaseWithHMM(new SimpleHashMapBuilder<String, Object>()
                .puts("type", "1")
                .puts(Functions.FUNCTION, "9044"));


        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                showShare();
//                if("2".equals(type)){
//                    StringDialog.newInstance().setUrl(SpanUtils.getBuilder(mContext,"胎动：").setForegroundColor(Color.parseColor("#222222")).setBold()
//                            .append("是孩子在成型之后，在孕妇肚子里面运动的结果，孩子健康和一些问题都可以通过胎动表现出来。\n").setForegroundColor(Color.parseColor("#222222"))
//                            .append("正常明显胎动1小时不少于3 ~ 5次，12小时明显胎动次数为30 ~ 40次以上。但由于胎儿个体差异大，有的胎儿12小时可动100次左右,只要胎动有规律、有节奏、变化不大，即证明胎儿发育是正常的。胎动正常，表示胎盘功能良好，输送给胎儿的氧气充足，胎儿在子宫内生长发育健全，很愉快地活动着。\n").setForegroundColor(Color.parseColor("#222222"))
//                            .create()).setTitle("小知识").show(getSupportFragmentManager(),"xiaozhishi");
//                }
//                if("3".equals(type)){
//                    StringDialog.newInstance().setUrl(SpanUtils.getBuilder(mContext,"孕肚：").setForegroundColor(Color.parseColor("#222222")).setBold()
//                            .append("孕肚-般都是在怀孕四个月以上才会显出来，而且是从小腹开始明显。因为宝宝的体位和孕妈自身皮肤的弹性不同，怀孕后妈妈肚子形状也不同。\n").setForegroundColor(Color.parseColor("#222222"))
//                            .append("在孕前，女性的子宫只有一颗小水梨大, 并且在骨盆腔中，到了怀孕10周，子宫差不多像葡萄柚大小。-般来说，13周起子宫开始明显变大，往上离开骨盆腔避免压迫，旁人用肉眼也许看不太出来，但妈妈可以自己在耻骨上方摸到。一直到怀孕38周左右，这时胎儿已经成熟了，子宫狭部慢慢变得比较薄，胎儿会下降慢慢进入骨盆腔，初产妇会感觉胎儿往下掉，就是快要生产的征兆;经产妇的情形不同，直到生产前一刻肚子才会有明显的下坠感。\n").setForegroundColor(Color.parseColor("#222222"))
//                            .create()).setTitle("小知识").show(getSupportFragmentManager(),"xiaozhishi");
//                }
//                if("4".equals(type)){
//                    StringDialog.newInstance().setUrl(SpanUtils.getBuilder(mContext,"孕期妈妈变化：").setForegroundColor(Color.parseColor("#222222")).setBold()
//                            .append("准妈妈们在怀孕期间，身体会经历一个较大的变化过程。而在不同的时期，身体变化都不太相同。\n").setForegroundColor(Color.parseColor("#222222"))
//                            .append("长斑：孕妈妈体内的身体激素发生变化代谢而导致的，这些斑都是淡淡的，产后也会逐渐变浅。\n").setForegroundColor(Color.parseColor("#222222"))
//                            .append("肚脐眼突出，出现妊娠纹:随着胎宝宝一天天长大，孕妈妈的肚子也会一天天变大， 腹部的皮肤被撑开之后，会产生妊娠纹，而且左右两侧的腹直肌会逐渐分离，同时加上子宫不断的增大，-般孕妈肚脐眼会逐渐凸出\n").setForegroundColor(Color.parseColor("#222222"))
//                            .append("发浓密：怀孕之后雌激素水平分泌都会增加，所以孕妈妈的身体毛发也会比较旺盛。\n").setForegroundColor(Color.parseColor("#222222"))
//                            .create()).setTitle("小知识").show(getSupportFragmentManager(),"xiaozhishi");
//                }
            }
        });

        if ("2".equals(vcType)) {
            textTitle.setText("算胎动测男女");
            toolsQuestions.add(new ToolsQuestion("有胎动时，宝宝胎动的时间？", "A. 没有规律可循，白天晚上都会动", "B. 睡前动得多", "BU_TU_CHU_OR_TU_CHU"));
            toolsQuestions.add(new ToolsQuestion("有胎动时，宝宝喜欢怎么动呢？", "A. 波浪式的此起彼伏", "B. 这踢一下，那打一拳头", "DA_OR_XIAO"));
            toolsQuestions.add(new ToolsQuestion("有胎动时，宝宝经常在哪活动呢？", "A. 在肚脐周围动来动去", "B. 左动动，右动动", "YUAN_OR_JIAN"));
            passurl = "http://aigender.market.alicloudapi.com/ai_imedical/guess_gender/tai_dong/v1";
        }
        if ("3".equals(vcType)) {
            textTitle.setText("看肚子测男女");

            toolsQuestions.add(new ToolsQuestion("看看肚脐，现在是什么样的呢？", "A. 肚脐不突出", "B. 肚脐很突出", "BU_TU_CHU_OR_TU_CHU"));
            toolsQuestions.add(new ToolsQuestion("量量肚子，大小如何呢？", "A. 肚子超大，很早低头就看不见脚了", "B. ”藏肚“型，肚子不怎么明显", "DA_OR_XIAO"));
            toolsQuestions.add(new ToolsQuestion("摸摸肚子，肚子的形状如何呢？", "A. 圆圆的肚肚", "B. 尖尖的肚肚", "YUAN_OR_JIAN"));

            passurl = "http://aigender.market.alicloudapi.com/ai_imedical/guess_gender/yun_du/v1";
        }
        if ("4".equals(vcType)) {
            textTitle.setText("看变化测男女");

            toolsQuestions.add(new ToolsQuestion("怀孕后，有无水肿现象？", "A. 没有", "B. 有", "BU_TU_CHU_OR_TU_CHU"));
            toolsQuestions.add(new ToolsQuestion("怀孕后，乳房有哪些变化？", "A. 上围增大了很多", "B. 没有明显变化", "DA_OR_XIAO"));
            toolsQuestions.add(new ToolsQuestion("怀孕后，皮肤情况怎么样呢？", "A. 皮肤细腻了，整体上有改善", "B. 毛孔变大了，皮肤好象没以前好了", "YUAN_OR_JIAN"));

            passurl = "http://aigender.market.alicloudapi.com/ai_imedical/guess_gender/yun_qi/v1";
        }
        buildQuestion();
    }

    private void buildQuestion() {
        toolsQlist.removeAllViews();
        for (int i = 0; i < toolsQuestions.size(); i++) {
            final ToolsQuestion toolsQuestion = toolsQuestions.get(i);
            View view = LayoutInflater.from(mContext).inflate(R.layout.index_activity_tools_test_item, toolsQlist, false);
            TextView toolsNumber;
            TextView toolsTitle;
            android.widget.RadioButton checkA;
            android.widget.RadioButton checkB;
            toolsNumber = (TextView) view.findViewById(R.id.toolsNumber);
            toolsTitle = (TextView) view.findViewById(R.id.toolsTitle);
            checkA = (RadioButton) view.findViewById(R.id.checkA);
            checkB = (RadioButton) view.findViewById(R.id.checkB);
            toolsNumber.setText((i + 1) + "");
            toolsTitle.setText(toolsQuestion.question + "");
            checkA.setText(toolsQuestion.answerA);
            checkB.setText(toolsQuestion.answerB);
            checkA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        toolsQuestion.value = "1";
                    }
                }
            });
            checkB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        toolsQuestion.value = "0";
                    }
                }
            });
            toolsQlist.addView(view);
        }
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void initView() {

        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        toolsQlist = (LinearLayout) findViewById(R.id.toolsQlist);
        tooosUseCount = (TextView) findViewById(R.id.tooos_UseCount);
        textTitle = (TextView) findViewById(R.id.textTitle);
    }

    public void chooseButtonOK(View view) {
        if (checkIllegal()) {
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < toolsQuestions.size(); i++) {
                ToolsQuestion toolsQuestion = toolsQuestions.get(i);
                map.put(toolsQuestion.key, toolsQuestion.value);
            }
            toolsTestPresenter.getBaseWithAL(passurl, map, new SimpleHashMapBuilder<String, Object>()
                    .puts("Authorization", "APPCODE " + "8639276d3616459fab0a609326bf6ffb"));
        }

    }

    @Override
    public void onSucessGetBaseHMM(String result) {
        //System.out.println(result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            tooosUseCount.setText(SpanUtils.getBuilder(mContext, "您是第 ").setForegroundColor(Color.parseColor("#763749"))
                    .append(jsonObject.optInt("data") + "").setForegroundColor(Color.parseColor("#FF6266")).setProportion(1.4f)
                    .append(" 位参与预测的孕妈").setForegroundColor(Color.parseColor("#763749")).create());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSucessUpdateBaseHMM(String result) {

    }

    private boolean checkIllegal() {
        for (int i = 0; i < toolsQuestions.size(); i++) {
            ToolsQuestion toolsQuestion = toolsQuestions.get(i);
            if ("-1".equals(toolsQuestion.value)) {
                Toast.makeText(mContext, "请全部勾选，增加预测的准确", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    public void onSucessGetAL(String result) {


        toolsTestPresenter.getBaseWithHMM(new SimpleHashMapBuilder<String, Object>()
                .puts("type", "1")
                .puts(Functions.FUNCTION, "9045"));
        try {
            finish();
            String sex = "男";
            if (result!=null&&! TextUtils.isEmpty(result)){
                JSONObject jsonObject = new JSONObject(result);
                sex = jsonObject.optString("宝宝性别");
            }
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_TEST_SEX_RESULT)
                    .withString("sex", sex)
                    .withString("vcType", vcType + "")
                    .navigation();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getSurl() {
        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_babyNameUrl);
        String url = String.format("%s?type=1&vcType=" + vcType, urlPrefix);
        surl = url;
        return surl;
    }

    @Override
    public String getSdes() {

        if ("2".equals(vcType)) {

            return "算胎动也能测男女哦，你也来试试吧~";
        }
        if ("3".equals(vcType)) {

            return "看肚子也能测男女哦，你也来试试吧~";
        }
        if ("4".equals(vcType)) {

            return "看变化也能测男女哦，你也来试试吧~";
        }

        return "清宫表测男女，据说是古代宫妃用的方法哦~";
    }

    @Override
    public String getStitle() {
        if ("2".equals(vcType)) {

            return "生男生女 - 算胎动预测";
        }
        if ("3".equals(vcType)) {

            return "生男生女 - 看肚子测男女";
        }
        if ("4".equals(vcType)) {

            return "生男生女 - 看变化预测";
        }
        return "生男生女 - 清宫表";
    }

    @Override
    public Bitmap getsBitmap() {
        return null;
    }
}
