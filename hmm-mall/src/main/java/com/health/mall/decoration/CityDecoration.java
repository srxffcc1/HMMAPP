//package com.health.mall.decoration;
//
//import android.content.Context;
//import android.graphics.Rect;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import android.view.View;
//
//import com.healthy.library.utils.TransformUtil;
//
///**
// * @author Li
// * @date 2019/03/04 16:23
// * @des 城市列表分割线
// */
//
//public class CityDecoration extends RecyclerView.ItemDecoration {
//
//    private Context mContext;
//
//    public CityDecoration(Context context) {
//        mContext = context;
//    }
//
//    @Override
//    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
//                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
//        if (parent.getLayoutManager() instanceof GridLayoutManager) {
//            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
//            int spanCount = layoutManager.getSpanCount();
//            int pos = parent.getChildAdapterPosition(view);
//
//            if (pos%spanCount==0) {
//                outRect.left= (int) TransformUtil.dp2px(mContext,15);
//                outRect.right= (int) TransformUtil.dp2px(mContext,6);
//            }else {
//                outRect.left= (int) TransformUtil.dp2px(mContext,6);
//                outRect.right= (int) TransformUtil.dp2px(mContext,15);
//            }
//
//            if (pos<spanCount) {
//                outRect.top= (int) TransformUtil.dp2px(mContext,20);
//            }else {
//                outRect.top= (int) TransformUtil.dp2px(mContext,8);
//            }
//            outRect.bottom= (int) TransformUtil.dp2px(mContext,8);
//        }
//    }
//}
