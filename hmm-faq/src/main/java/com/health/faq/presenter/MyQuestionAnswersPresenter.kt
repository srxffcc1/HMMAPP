package com.health.faq.presenter

import android.content.Context
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.health.faq.contract.MyQuestionAnswersContract
import com.health.faq.model.MyQuestion
import com.healthy.library.LibApplication
import com.healthy.library.constant.Functions
import com.healthy.library.net.ObservableHelper
import com.healthy.library.net.StringObserver
import org.json.JSONObject
import java.util.HashMap

class MyQuestionAnswersPresenter(mContext:Context,baseView: MyQuestionAnswersContract.View) : MyQuestionAnswersContract.Presenter {
    var baseView: MyQuestionAnswersContract.View? = baseView
    var mContext :Context? = mContext

    /*
    * 获取数据 1:我提问 2：我回答
    * */
    override fun getRuestionAnswers(chooseType: String) {
        val map = HashMap<String, Any>()
        map["function"] = Functions.FUNCTION_GET_QUESTIONS_ANSWERS
        map["chooseType"] = chooseType
        ObservableHelper.createObservable(mContext, map)
                .subscribe(object : StringObserver(baseView, mContext, false,
                        true, true, false, false, false) {
                    override fun onGetResultSuccess(obj: String?) {
                        super.onGetResultSuccess(obj)
                        //解析数据
                        val jsonObject=JSONObject(obj)["data"].toString()
                        var result = Gson().fromJson<MutableList<MyQuestion>>(jsonObject)
                        baseView?.getRuestionAnswersSuccess(result)
                    }

                    override fun onGetResultFail(result: String?) {
                        super.onGetResultFail(result)
                        var jsonObject = JSONObject(result)
                        baseView?.getRuestionAnswersFail(jsonObject["msg"].toString())
                    }

                })
    }
}