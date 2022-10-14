package com.healthy.library.message;

/**
 * @author Li
 * @date 2019/05/09 16:48
 * @des 更改用户信息
 */

public class FoodDialogInfoOpenMsg {
    public String id;
    public int dialogType=1;

    public FoodDialogInfoOpenMsg(String id, int dialogType) {
        this.id = id;
        this.dialogType = dialogType;
    }
}
