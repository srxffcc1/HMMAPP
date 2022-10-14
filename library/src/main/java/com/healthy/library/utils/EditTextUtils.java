//package com.healthy.library.utils;
//
//import android.os.Build;
//import android.view.ActionMode;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.EditText;
//
///**
// * Author: Li
// * Date: 2018/10/10 0010
// * Description:
// */
//public class EditTextUtils {
//    /**
//     * 屏蔽粘贴复制等操作
//     * @param editText 待屏蔽控件
//     */
//    public static void shieldOperation(EditText editText) {
//        editText.setLongClickable(false);
//        ActionMode.Callback callback = new ActionMode.Callback() {
//            @Override
//            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                return false;
//            }
//
//            @Override
//            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                return false;
//            }
//
//            @Override
//            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                return false;
//            }
//
//            @Override
//            public void onDestroyActionMode(ActionMode mode) {
//
//            }
//        };
//        editText.setCustomSelectionActionModeCallback(callback);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            editText.setCustomInsertionActionModeCallback(callback);
//        }
//    }
//}
