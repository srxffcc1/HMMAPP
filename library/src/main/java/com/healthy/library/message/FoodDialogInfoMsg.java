package com.healthy.library.message;

import java.util.Date;

/**
 * @author Li
 * @date 2019/05/09 16:48
 * @des 更改用户信息
 */

public class FoodDialogInfoMsg {
    public String effectId;
    public String foodId;


    public String effect;
    public String food;

    public FoodDialogInfoMsg(String effectId, String foodId, String effect, String food) {
        this.effectId = effectId;
        this.foodId = foodId;
        this.effect = effect;
        this.food = food;
    }
}
