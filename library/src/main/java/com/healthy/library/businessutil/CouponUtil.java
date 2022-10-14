package com.healthy.library.businessutil;

import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.interfaces.IHmmCoupon;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CouponUtil {

    /**
     * 获得勾选之后的变化结果集
     * @param couponInfo
     * @param selectInfosingle
     * @return
     */
    public static List<IHmmCoupon> getSelectListResult(List<IHmmCoupon> couponInfo, IHmmCoupon selectInfosingle) {
        if (couponInfo == null) {
            couponInfo = new ArrayList<>();
        }
        //System.out.println("当前选中的券价格体系:"+selectInfosingle.getCriterionType());
        if(couponInfo.size()>0){//说明有券 先判断下价格体系是不是一致
//            couponInfo.clear();
            couponInfo.add(selectInfosingle);
            return couponInfo;
        }
        int pindex = -1; //平台券
        int sindexhaslimit = -1;//商家券有门槛
        int sindexnolimit = -1;//商家券无门槛
        for (int i = 0; i < couponInfo.size(); i++) {
            if (couponInfo.get(i).getCouponType() == 1) {
                pindex = i;
            } else {
                if(couponInfo.get(i).getRequirement().contains("无门槛")){//说明是无门槛
                    sindexnolimit = i;
                }else {
                    sindexhaslimit = i;
                }
            }
        }
        if(selectInfosingle.getCouponType()==1){//选的是平台
            if((sindexhaslimit != -1&&!couponInfo.get(sindexhaslimit).isSupportSuperposition())
                    ||(sindexnolimit!=-1&&!couponInfo.get(sindexnolimit).isSupportSuperposition())){//存在一张不支持叠加
                //需要清空
                couponInfo.clear();//清理掉冲突的加入卡池
                couponInfo.add(selectInfosingle);
            }else {//
                if(pindex!=-1){//替换
                    couponInfo.set(pindex, selectInfosingle);
                }else {
                    couponInfo.add(selectInfosingle);//加入卡池
                }

            }
        }else {//选的是商家  需要再想想

            if(sindexhaslimit!=-1&&sindexnolimit==-1){//有门槛替换 替换
                if(!selectInfosingle.getRequirement().contains("无门槛")){
                    couponInfo.set(sindexhaslimit, selectInfosingle);
                }else {
                    couponInfo.add(selectInfosingle);//加入卡池
                }
            } else if(sindexnolimit!=-1&&sindexhaslimit==-1){//无门槛 替换
                if(selectInfosingle.getRequirement().contains("无门槛")){
                    couponInfo.set(sindexnolimit, selectInfosingle);
                }else {
                    couponInfo.add(selectInfosingle);//加入卡池
                }
            }else if(sindexhaslimit!=-1&&sindexnolimit!=-1){
                if(selectInfosingle.getRequirement().contains("无门槛")){
                    couponInfo.set(sindexnolimit, selectInfosingle);
                }else {
                    couponInfo.set(sindexhaslimit, selectInfosingle);
                }
            }else {//不存在替换的情况那就看看是清空卡池还是加入卡池的问题了
                if(!selectInfosingle.isSupportSuperposition()&&pindex!=-1){//需要清理掉那张平台券
                    couponInfo.set(pindex,selectInfosingle);//直接替换掉这张平台券为选中的商家券
                }else {
                    couponInfo.add(selectInfosingle);//加入卡池
                }
            }
        }
        return couponInfo;
    }

    public static CouponInfoZ getMatchGood(List<CouponInfoZ> couponInfoLeftZList) {
        CouponInfoZ couponInfoZ=null;
        for (int i = 0; i <couponInfoLeftZList.size() ; i++) {
            if(couponInfoZ!=null&&couponInfoLeftZList.get(i).getCouponDenominationDouble()>couponInfoZ.getCouponDenominationDouble()){
                couponInfoZ=couponInfoLeftZList.get(i);
            }else {
                if(couponInfoZ==null){
                    couponInfoZ=couponInfoLeftZList.get(i);
                }
            }
        }
        return couponInfoZ;
    }

    /**
     * 获得匹配最优的推荐
     * @param needseach
     * @param desMoney 商品净值
     * @return
     */
    public IHmmCoupon getMatchBestCoupon(List<IHmmCoupon> needseach, double desMoney) {
        IHmmCoupon result = null;
        for (int i = 0; i < needseach.size(); i++) {
            IHmmCoupon tpt = needseach.get(i);
            if (Double.parseDouble(tpt.getOverPayment()) <= desMoney) {//券符合使用要求
                if(result==null){
                    result=tpt;
                }else {
                    if (Double.parseDouble(tpt.getCouponDenomination()) > Double.parseDouble(result.getCouponDenomination())) {//当前的面额大于旧的则替换
                        result = tpt;
                    }else if (Double.parseDouble(tpt.getCouponDenomination()) == Double.parseDouble(result.getCouponDenomination())) {//面额一样比较时间
                        Date tpttime = null;
                        Date resulttime = null;
                        try {
                            tpttime = new SimpleDateFormat("yyyy-MM-dd").parse(tpt.getTimeValidityEnd());
                            resulttime = new SimpleDateFormat("yyyy-MM-dd").parse(result.getTimeValidityEnd());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (tpttime.before(resulttime)) {
                            result = tpt;
                        } else if (tpttime.equals(resulttime)) {
                            if (tpt.getCouponType() == 2) {
                                result = tpt;
                            }
                        }
                    }
                }

            }
        }
        return result;
    }
    /**
     * 判断当前券是不是已选
     * @param couponInfo
     * @param selectInfosingle
     * @return
     */
    public static boolean checkItemCouponInSelectList(List<IHmmCoupon> couponInfo, IHmmCoupon selectInfosingle){
        for (int i = 0; i <couponInfo.size() ; i++) {
            if(selectInfosingle.getUseId().equals(couponInfo.get(i).getUseId())){
                return true;
            }
        }
        return false;
    }
    /**
     * 判断当前券和已选券的池子存在冲突关系吗 用来在列表中表示不可选或置灰状态
     * @param couponInfo
     * @param selectInfosingle
     * @return
     */
    public static boolean checkItemCouponCanWhite(List<IHmmCoupon> couponInfo, IHmmCoupon selectInfosingle){
//        if(checkItemCouponInSelectList(couponInfo,selectInfosingle)){//已经是已选了就设置不冲突
//            return false;
//        }else {
//            if(couponInfo.size()>0){//说明有券 先判断下价格体系是不是一致
//                return true;
//
////                if(selectInfosingle.getCriterionType()!=couponInfo.get(0).getCriterionType()){
////                    return true;
////                }
//            }
//            //System.out.println("和冲突吗");
//            int pindex = -1; //平台券
//            int sindexhaslimit = -1;//商家券有门槛
//            int sindexnolimit = -1;//商家券无门槛
//
//            for (int i = 0; i < couponInfo.size(); i++) {
//                if (couponInfo.get(i).getCouponType() == 1) {
//                    pindex = i;
//                } else {
//                    if(couponInfo.get(i).getRequirement().contains("无门槛")){//说明是无门槛
//                        //System.out.println("和中检测到无门槛");
//                        sindexnolimit = i;
//                    }else {
//                        //System.out.println("和中检测到有门槛");
//                        sindexhaslimit = i;
//                    }
//                }
//            }
//            if(selectInfosingle.getCouponType()==1){//选的是平台
//                if((sindexhaslimit != -1&&!couponInfo.get(sindexhaslimit).isSupportSuperposition())
//                        ||(sindexnolimit!=-1&&!couponInfo.get(sindexnolimit).isSupportSuperposition())){//存在一张不支持叠加
//                    return true;
//                }else {//
//
//                }
//            }else {//选的是商家
//
//                if(sindexhaslimit!=-1&&sindexnolimit==-1){//有门槛替换 替换
//                    if(!selectInfosingle.getRequirement().contains("无门槛")){
//                        //System.out.println("和有门槛冲突");
//                        return true;
//                    }else{
//                        //System.out.println("和中无有门槛");
//                    }
//                } else if(sindexnolimit!=-1&&sindexhaslimit==-1){//无门槛 替换
//                    if(selectInfosingle.getRequirement().contains("无门槛")){
//                        //System.out.println("和无门槛冲突");
//                        return true;
//                    }else {
//                        //System.out.println("和中无无门槛");
//                    }
//                }else if(sindexhaslimit!=-1&&sindexnolimit!=-1){
//                    return true;
//
//                }else {//不存在替换的情况那就看看是清空卡池还是加入卡池的问题了
//                    //System.out.println("卡池中没商家券");
//                    if(!selectInfosingle.isSupportSuperposition()&&pindex!=-1){//需要清理掉那张平台券
//                        return true;
//                    }else {
//                    }
//                }
//            }
//
//        }
        return false;
    }

}
