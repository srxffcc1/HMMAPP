package com.health.servicecenter.adapter;

import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.AppointmentMainItemModel;
import com.healthy.library.utils.JsBridge;
import com.healthy.library.utils.ResizeImgWebViewClient;
import com.healthy.library.widget.X5WebView;

/**
 * @author: long
 * @date: 2021/4/1
 */
public class AppointmentDetailBodyAdapter extends BaseAdapter<AppointmentMainItemModel> {

    private X5WebView mWebView;
    private FrameLayout mContentLayout;

    public AppointmentDetailBodyAdapter() {
        super(R.layout.item_appointment_detail_body_layout);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {

        mContentLayout = holder.getView(R.id.content_layout);
        mWebView = new X5WebView(context, null);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mContentLayout.addView(mWebView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        //mWebView = holder.getView(R.id.wv_item_appointment_detail);
        //WebViewSetting.setWebViewParam(mWebView, context);

        /*String data = getModel().getDetail() + "<img src=\"http://hmm-public.oss-cn-beijing.aliyuncs.com/technician-user-dev/images/ef1f8b73-8c2b-40a4-a457-217475ce5977.png\"></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><span style=\"color: rgb(89,\n" +
                "     89,\n" +
                "     89);\">“我就想安安静静做个妈妈”，这可真是说出每个孕后妈妈的心声了。</span></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><span style=\"color: rgb(89,\n" +
                "     89,\n" +
                "     89);\">生娃之后，当妈的各类压力会全方面爆发，千头万绪根本顾不过来。</span></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><span style=\"color: rgb(89,\n" +
                "     89,\n" +
                "     89);\">给哺乳期的妈妈一点空间，咋就这么难呢？</span></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><span style=\"color: rgb(89,\n" +
                "     89,\n" +
                "     89);\">大家对产后迅速恢复身材的执念，又到底是哪里来的？</span></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><span style=\"color: rgb(89,\n" +
                "     89,\n" +
                "     89);\">每天睁眼就是孩子，注意娃的点滴动态；娃醒着的时候哄他喂他照顾他，娃睡着了还有别的家务活。</span></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><strong>大家回忆起那段日子，觉得最简单的愿望只有一个：让我多睡一会吧。</strong></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><span style=\"color: rgb(89,\n" +
                "     89,\n" +
                "     89);\">连玩手机的空闲都是奢侈，哪里还能顾及运动减肥、管理身材和外形？</span></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><img src=\"http://hmm-public.oss-cn-beijing.aliyuncs.com/technician-user-dev/images/2b12e478-15d1-45e8-a5f8-867f938c9637.jpg\"></p><p class=\"ql-align-justify\"><br></p><h3 class=\"ql-align-justify\"><strong style=\"color: rgb(240,\n" +
                "     102,\n" +
                "     102);\">2.</strong></h3><p class=\"ql-align-justify\"><span style=\"color: rgb(89,\n" +
                "     89,\n" +
                "     89);\">权威机构研究表明，正常来说，母乳妈妈在产后6~9个月后才能回归孕前体重。</span></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><span style=\"color: rgb(89,\n" +
                "     89,\n" +
                "     89);\">产后6个月以内，每个月的自然减重范围也只有0.45到0.9Kg，6个月之后减重速度就更慢了。</span></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><span style=\"color: rgb(89,\n" +
                "     89,\n" +
                "     89);\">但这也只是理想状态。每个妈妈的身体、家庭、工作情况都不一样，情况自然也都不同。</span></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><span style=\"color: rgb(89,\n" +
                "     89,\n" +
                "     89);\">所以，看到张歆艺产后发胖的照片，我们一点都不奇怪。</span></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><span style=\"color: rgb(89,\n" +
                "     89,\n" +
                "     89);\">可糟心的是，“产后迅速恢复”似乎正在变成一种约定“速”成。</span></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><span style=\"color: rgb(89,\n" +
                "     89,\n" +
                "     89);\">“当妈了也要美”、“当妈了也还要瘦”，明明是不折不扣的毒鸡汤，却因为无处不在，成了理所当然的标准。</span></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><span style=\"color: rgb(89,\n" +
                "     89,\n" +
                "     89);\">毕竟我们的周围，充斥着大量的“正面榜样”：前有女明星立下FLAG三个月瘦回马甲线，后有增重50斤后花两个月恢复孕前体重。</span></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><strong>这些所谓的励志无处不在，最终构成了妈妈无法躲避的压力，似乎你做不到这样，就是你有问题。</strong></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><span style=\"color: rgb(89,\n" +
                "     89,\n" +
                "     89);\">就连张歆艺自己在发泄完过后，都要给自己找补一句：没能马上恢复身材，让大家失望了，等我断了奶，我一定尽快瘦回来。</span></p><p class=\"ql-align-justify\"><br></\n" +
                "2021-04-02 09:30:34.943 14231-14592/com.health.client E/net: 快”瘦回来呢？</span></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><strong>产后恢复的时间表，不是应该掌握在妈妈自己手里吗？</strong></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><img src=\"http://hmm-public.oss-cn-beijing.aliyuncs.com/technician-user-dev/images/99788c70-82be-400e-b6cd-eb981981fc44.jpg\"></p><h2 class=\"ql-align-justify\"><br></h2><h3 class=\"ql-align-justify\"><strong style=\"color: rgb(240,\n" +
                "     102,\n" +
                "     102);\">3.</strong></h3><p class=\"ql-align-justify\"><span style=\"color: rgb(89,\n" +
                "     89,\n" +
                "     89);\">女人从一出生，就要接受别人对自己身体的指指点点。</span></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><span style=\"color: rgb(89,\n" +
                "     89,\n" +
                "     89);\">即便是孕期，也逃不过来自四面八方的审视。</span></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><span style=\"color: rgb(89,\n" +
                "     89,\n" +
                "     89);\">我们总是把注意力放在“谁瘦了”、“谁没瘦”、“什么时候能瘦回去”。可除了这些表面数字，是不是还有别的事情可以讨论？</span></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><span style=\"color: rgb(89,\n" +
                "     89,\n" +
                "     89);\">马伊琍当年被人嘲笑身材走样的时候，曾向不明内情的外人解释，“哺乳期的妈妈为了奶水好，每天要吃很多有营养的东西，不能穿塑形内衣。一睁眼就是工作，工作结束后，马上就要吸奶赶紧睡觉，没有运动和休闲时间。”</span></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><strong>但她接着说，我喜欢演戏，我选择母乳喂养女儿，所以我必须接受这样的自己。</strong></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><span style=\"color: rgb(89,\n" +
                "     89,\n" +
                "     89);\">是啊，作为女人，我们有自己热爱的工作；作为妈妈，我们有权力用想要的方式养育孩子。</span></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><span style=\"color: rgb(89,\n" +
                "     89,\n" +
                "     89);\">所以我们不得不有所牺牲，学着接受并不完美的自己。</span></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><span style=\"color: rgb(89,\n" +
                "     89,\n" +
                "     89);\">但这并不代表外人可以要求我们按照某一种标准活着，更没有权力把我们的不够尽善尽美，视作成为被嘲笑、被冒犯的理由。</span></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><strong>就算肚子变大、身材走样，我们的生活也是自己的。</strong></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><span style=\"color: rgb(89,\n" +
                "     89,\n" +
                "     89);\">别再盯着产后妈妈的体重不放了。</span></p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\"><span style=\"color: rgb(89,\n" +
                "     89,\n" +
                "     89);\">还有很多更重要的事，值得去关心。</span></p><p class=\"ql-align-justify\"><br></p>";*/
        String sHead = "<html><head><meta name=\"viewport\" content=\"width=device-width, " +
                "initial-scale=1.0, minimum-scale=0.5, maximum-scale=8.0, user-scalable=yes\" />" +
                "<style>img{max-width:100% !important;height:auto}</style>"
                + "<style>body{max-width:100% !important;}</style>" + "</head><body>";
        String body = getModel().getDetail();
        //如存在P标签进行替换
        if (!TextUtils.isEmpty(body) && body.contains("<p>")) {
            //P标签是双标签需替换两次开口和闭口
            body = body.replace("<p>", "").replace("</p>", "");
        }
        String data = sHead + body + "</body></html>";
        //mWebView.addJavascriptInterface(new JsBridge(), "JsBridge");
        //mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);
        //mWebView.setWebViewClient(new ResizeImgWebViewClient());

    }

    public void onDestroy() {
        if (mWebView != null) {
            mContentLayout.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
    }
}
