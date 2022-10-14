package com.healthy.library.model;

import com.healthy.library.LibApplication;
import com.healthy.library.constant.SpKey;
import com.healthy.library.utils.SpUtils;

import java.io.Serializable;
import java.util.ArrayList;

public class GoodsBasketAll implements Serializable {


    public int total;

    public ArrayList<GoodsBasketCell> validList;

    public ArrayList<GoodsBasketCell> invalidList;

    public ArrayList<GoodsBasketCell> differentList;

    //判断失效商品
    public void checkRealInvald() {
        if(validList!=null&&validList.size()>0){
            for (int i = 0; i <validList.size() ; i++) {
                GoodsBasketCell goodsBasketCell=validList.get(i);
                if("8".equals(goodsBasketCell.getGoodsMarketingIdOrg())&& !SpUtils.getValue(LibApplication.getAppContext(), SpKey.PLUSSTATUS,false)){
                    if(invalidList==null){
                        invalidList=new ArrayList<>();
                    }
                    invalidList.add(goodsBasketCell);
                    validList.remove(i);
                    i--;
                    continue;
                }
//                if(goodsBasketCell.getStockInBasket()<=0){
//                    if(invalidList==null){
//                        invalidList=new ArrayList<>();
//                    }
//                    invalidList.add(goodsBasketCell);
//                    validList.remove(i);
//                    i--;
//                    continue;
//                }
            }
        }
    }
}
