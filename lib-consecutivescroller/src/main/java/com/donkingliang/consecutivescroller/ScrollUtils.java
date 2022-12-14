package com.donkingliang.consecutivescroller;

import android.graphics.Rect;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import androidx.core.view.ScrollingView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author donkingliang
 * @Description
 * @Date 2020/3/17
 */
public class ScrollUtils {

    static int computeVerticalScrollOffset(View view) {
        View scrolledView = getScrolledView(view);

        if (scrolledView instanceof ScrollingView) {
            return ((ScrollingView) scrolledView).computeVerticalScrollOffset();
        }

        try {
            Method method = View.class.getDeclaredMethod("computeVerticalScrollOffset");
            method.setAccessible(true);
            Object o = method.invoke(scrolledView);
            if (o != null){
                return (int) o;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scrolledView.getScrollY();
    }

    static int computeVerticalScrollRange(View view) {
        View scrolledView = getScrolledView(view);

        if (scrolledView instanceof ScrollingView) {
            return ((ScrollingView) scrolledView).computeVerticalScrollRange();
        }

        try {
            Method method = View.class.getDeclaredMethod("computeVerticalScrollRange");
            method.setAccessible(true);
            Object o = method.invoke(scrolledView);
            if (o != null){
                return (int) o;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scrolledView.getHeight();
    }

    static int computeVerticalScrollExtent(View view) {
        View scrolledView = getScrolledView(view);

        if (scrolledView instanceof ScrollingView) {
            return ((ScrollingView) scrolledView).computeVerticalScrollExtent();
        }

        try {
            Method method = View.class.getDeclaredMethod("computeVerticalScrollExtent");
            method.setAccessible(true);
            Object o = method.invoke(scrolledView);
            if (o != null){
                return (int) o;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scrolledView.getHeight();
    }

    /**
     * ??????View?????????????????????????????????
     *
     * @param view
     * @return
     */
    static int getScrollTopOffset(View view) {
        if (isConsecutiveScrollerChild(view) && canScrollVertically(view, -1)) {
            return Math.min(-computeVerticalScrollOffset(view), -1);
        } else {
            return 0;
        }
    }

    /**
     * ??????View?????????????????????????????????
     *
     * @param view
     * @return
     */
    static int getScrollBottomOffset(View view) {
        if (isConsecutiveScrollerChild(view) && canScrollVertically(view, 1)) {
            return Math.max(computeVerticalScrollRange(view) - computeVerticalScrollOffset(view)
                    - computeVerticalScrollExtent(view), 1);
        } else {
            return 0;
        }
    }

    /**
     * ???????????????????????????View???(??????????????????????????????????????????????????????)
     *
     * @param view
     * @return
     */
    static boolean canScrollHorizontally(View view) {
        return isConsecutiveScrollerChild(view) && (view.canScrollHorizontally(1) || view.canScrollHorizontally(-1));
    }

    /**
     * ???????????????????????????View???(??????????????????????????????????????????????????????)
     *
     * @param view
     * @return
     */
    static boolean canScrollVertically(View view) {
        return isConsecutiveScrollerChild(view) && (canScrollVertically(view, 1) || canScrollVertically(view, -1));
    }

    private static final Rect mBounds = new Rect();

    /**
     * ????????????????????????
     *
     * @param view
     * @param direction
     * @return
     */
    static boolean canScrollVertically(View view, int direction) {
        View scrolledView = getScrolledView(view);

        if (scrolledView.getVisibility() == View.GONE) {
            // ???????????????????????????
            return false;
        }

        if (scrolledView instanceof AbsListView) {
            AbsListView listView = (AbsListView) scrolledView;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                return listView.canScrollList(direction);
            } else {
                // ???????????????(android 19??????)???AbsListView???????????????
                return false;
            }
        } else {
            // RecyclerView??????canScrollVertically?????????????????????????????????????????????????????????
            if (scrolledView instanceof RecyclerView) {
                RecyclerView recyclerView = (RecyclerView) scrolledView;

                if (recyclerView.canScrollHorizontally(1) || recyclerView.canScrollHorizontally(-1)) {
                    // ??????recyclerView?????????????????????????????????canScrollVertically???????????????????????????????????????????????????????????????
                    // ?????????????????????recyclerView???????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    if (!recyclerView.canScrollVertically(direction)) {
                        return false;
                    }
                }

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                RecyclerView.Adapter adapter = recyclerView.getAdapter();

                if (layoutManager != null && adapter != null && adapter.getItemCount() > 0) {
                    View itemView = layoutManager.findViewByPosition(direction > 0 ? adapter.getItemCount() - 1 : 0);
                    if (itemView == null) {
                        return true;
                    }
                } else {
                    return false;
                }

                int count = recyclerView.getChildCount();
                if (direction > 0) {
                    for (int i = count - 1; i >= 0; i--) {
                        View child = recyclerView.getChildAt(i);
                        recyclerView.getDecoratedBoundsWithMargins(child, mBounds);
                        if (mBounds.bottom > recyclerView.getHeight() - recyclerView.getPaddingBottom()) {
                            return true;
                        }
                    }
                    return false;
                } else {
                    for (int i = 0; i < count; i++) {
                        View child = recyclerView.getChildAt(i);
                        recyclerView.getDecoratedBoundsWithMargins(child, mBounds);
                        if (mBounds.top < recyclerView.getPaddingTop()) {
                            return true;
                        }
                    }
                    return false;
                }
            }

            return scrolledView.canScrollVertically(direction);
        }
    }

    /**
     * ???????????????????????????View
     *
     * @param rootView
     * @param touchX
     * @param touchY
     * @return
     */
    static List<View> getTouchViews(View rootView, int touchX, int touchY) {
        List<View> views = new ArrayList<>();
        addTouchViews(views, rootView, touchX, touchY);
        return views;
    }

    private static void addTouchViews(List<View> views, View view, int touchX, int touchY) {
        if (isConsecutiveScrollerChild(view) && isTouchPointInView(view, touchX, touchY)) {
            views.add(view);

            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                int count = viewGroup.getChildCount();
                for (int i = 0; i < count; i++) {
                    addTouchViews(views, viewGroup.getChildAt(i), touchX, touchY);
                }
            }
        }
    }

    /**
     * ????????????????????????View???
     *
     * @param view
     * @param x
     * @param y
     * @return
     */
    static boolean isTouchPointInView(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        int[] position = new int[2];
        view.getLocationOnScreen(position);
        int left = position[0];
        int top = position[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();

        if (x >= left && x <= right && y >= top && y <= bottom) {
            return true;
        }
        return false;
    }

    static int getRawX(View rootView, MotionEvent ev, int pointerIndex) {
//        if (android.os.Build.VERSION.SDK_INT >= 29) {
//            return (int) ev.getRawX(pointerIndex);
//        } else {
            int[] position = new int[2];
            rootView.getLocationOnScreen(position);
            int left = position[0];
            return (int) (left + ev.getX(pointerIndex));
//        }
    }

    static int getRawY(View rootView, MotionEvent ev, int pointerIndex) {
//        if (android.os.Build.VERSION.SDK_INT >= 29) {
//            return (int) ev.getRawY(pointerIndex);
//        } else {
            int[] position = new int[2];
            rootView.getLocationOnScreen(position);
            int top = position[1];
            return (int) (top + ev.getY(pointerIndex));
//        }
    }

    static List<Integer> getScrollOffsetForViews(List<View> views) {
        List<Integer> offsets = new ArrayList<>();
        for (View view : views) {
            offsets.add(computeVerticalScrollOffset(view));
        }
        return offsets;
    }

    static boolean equalsOffsets(List<Integer> offsets1, List<Integer> offsets2) {
        if (offsets1.size() != offsets2.size()) {
            return false;
        }

        int size = offsets1.size();

        for (int i = 0; i < size; i++) {
            if (!offsets1.get(i).equals(offsets2.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * ??????View??????????????????????????????
     *
     * @param view
     * @return
     */
    static boolean isConsecutiveScrollerChild(View view) {
        if (view != null) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();

            if (lp instanceof ConsecutiveScrollerLayout.LayoutParams) {
                return ((ConsecutiveScrollerLayout.LayoutParams) lp).isConsecutive;
            }
            return true;
        }
        return false;
    }

    /**
     * ?????????????????????view????????????????????????????????????
     *
     * @param view
     * @return
     */
    static View getScrolledView(View view) {
        View consecutiveView = null;

        // ?????????layout_scrollChild????????????view?????????
        View scrolledView = getScrollChild(view);

        while (scrolledView instanceof IConsecutiveScroller) {
            consecutiveView = scrolledView;
            scrolledView = ((IConsecutiveScroller) scrolledView).getCurrentScrollerView();

            if (consecutiveView == scrolledView) {
                break;
            }
        }

        return scrolledView;
    }

    /**
     * ????????????layout_scrollChild??????????????????view???????????????view???????????????view
     *
     * @param view
     * @return
     */
    static View getScrollChild(View view) {
        if (view != null) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();

            if (lp instanceof ConsecutiveScrollerLayout.LayoutParams) {
                int childId = ((ConsecutiveScrollerLayout.LayoutParams) lp).scrollChild;
                if (childId != View.NO_ID){
                    View child = view.findViewById(childId);
                    if (child != null) {
                        return child;
                    }
                }
            }
        }
        return view;
    }

    static boolean startInterceptRequestLayout(RecyclerView view) {
        if ("InterceptRequestLayout".equals(view.getTag())) {
            try {
                Method method = RecyclerView.class.getDeclaredMethod("startInterceptRequestLayout");
                method.setAccessible(true);
                method.invoke(view);
                return true;
            } catch (Exception e) {
            }
        }
        return false;
    }

    static void stopInterceptRequestLayout(RecyclerView view) {
        if ("InterceptRequestLayout".equals(view.getTag())) {
            try {
                Method method = RecyclerView.class.getDeclaredMethod("stopInterceptRequestLayout", boolean.class);
                method.setAccessible(true);
                method.invoke(view, false);
            } catch (Exception e) {
            }
        }
    }

    /**
     * ???????????????????????????isConsecutive???true.??????????????????ConsecutiveScrollerLayout??????
     *
     * @param view
     * @return
     */
    static boolean isConsecutiveScrollParent(View view) {
        View child = view;
        while (child.getParent() instanceof ViewGroup && !(child.getParent() instanceof ConsecutiveScrollerLayout)) {
            child = (View) child.getParent();
        }

        if (child.getParent() instanceof ConsecutiveScrollerLayout) {
            return isConsecutiveScrollerChild(child);
        } else {
            return false;
        }
    }

    /**
     * ?????????????????????????????????view??????????????????
     *
     * @param rootView
     * @param touchX
     * @param touchY
     * @return
     */
    static boolean isHorizontalScroll(View rootView, int touchX, int touchY) {
        List<View> views = getTouchViews(rootView, touchX, touchY);
        for (View view : views) {
            if (view.canScrollHorizontally(1) || view.canScrollHorizontally(-1)) {
                return true;
            }
        }
        return false;
    }

    /**
     * ??????????????????view??????????????????????????????
     *
     * @return
     */
    static boolean isTouchNotTriggerScrollStick(View rootView, int touchX, int touchY) {
        List<ConsecutiveScrollerLayout> csLayouts = getInTouchCSLayout(rootView, touchX, touchY);
        int size = csLayouts.size();
        for (int i = size - 1; i >= 0; i--) {
            ConsecutiveScrollerLayout csl = csLayouts.get(i);
            View topView = getTopViewInTouch(csl, touchX, touchY);
            if (topView != null && csl.isStickyView(topView) && csl.theChildIsStick(topView)) {
                ConsecutiveScrollerLayout.LayoutParams lp = (ConsecutiveScrollerLayout.LayoutParams) topView.getLayoutParams();
                if (!lp.isTriggerScroll) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * ???????????????????????????ConsecutiveScrollerLayout
     *
     * @param rootView
     * @param touchX
     * @param touchY
     * @return
     */
    static List<ConsecutiveScrollerLayout> getInTouchCSLayout(View rootView, int touchX, int touchY) {
        List<ConsecutiveScrollerLayout> csLayouts = new ArrayList<>();
        List<View> views = getTouchViews(rootView, touchX, touchY);
        for (View view : views) {
            if (view instanceof ConsecutiveScrollerLayout) {
                csLayouts.add((ConsecutiveScrollerLayout) view);
            }
        }
        return csLayouts;
    }

    static View getTopViewInTouch(ConsecutiveScrollerLayout csl, int touchX, int touchY) {
        int count = csl.getChildCount();
        View topTouchView = null;

        for (int i = 0; i < count; i++) {
            View child = csl.getChildAt(i);
            if (child.getVisibility() == View.VISIBLE && isTouchPointInView(child, touchX, touchY)) {
                if (topTouchView == null) {
                    topTouchView = child;
                    continue;
                }

                if (ViewCompat.getZ(child) > ViewCompat.getZ(topTouchView) // ??????View???Z??????
                        || (ViewCompat.getZ(child) == ViewCompat.getZ(topTouchView)
                        && csl.getDrawingPosition(child) > csl.getDrawingPosition(topTouchView))) { // ??????????????????
                    topTouchView = child;
                }
            }
        }

        return topTouchView;
    }
}
