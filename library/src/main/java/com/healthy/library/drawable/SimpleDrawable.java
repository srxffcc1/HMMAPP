//package com.healthy.library.drawable;
//
//import android.graphics.Rect;
//import android.graphics.drawable.GradientDrawable;
//
///**
// * Author: Li
// * Date: 2018/12/13 0013
// * Description:
// */
//public class SimpleDrawable extends GradientDrawable {
//
//    private boolean isRoundCorner;
//
//    private SimpleDrawable(Builder builder) {
//        isRoundCorner = builder.isRoundCorner;
//        if (!isRoundCorner) {
//            setCornerRadius(builder.radius);
//        }
//        setColors(builder.colors);
//        setOrientation(builder.orientation);
//    }
//
//    @Override
//    protected void onBoundsChange(Rect r) {
//        super.onBoundsChange(r);
//        if (isRoundCorner) {
//            float radius = Math.min(r.width(), r.height()) * 1.0f / 2;
//            setCornerRadius(radius);
//        }
//    }
//
//
//    public static class Builder {
//        private int[] colors;
//        private boolean isRoundCorner = false;
//        private float radius;
//        private Orientation orientation = Orientation.LEFT_RIGHT;
//
//        public Builder setColors(int... colors) {
//            this.colors = colors;
//            return this;
//        }
//
//        public Builder isRoundCorner(boolean isRoundCorner) {
//            this.isRoundCorner = isRoundCorner;
//            return this;
//        }
//
//        public Builder setRadius(float radius) {
//            this.radius = radius;
//            return this;
//        }
//
//        public Builder setOrizention(Orientation orizention) {
//            this.orientation = orizention;
//            return this;
//        }
//
//        public SimpleDrawable build() {
//            return new SimpleDrawable(this);
//        }
//
//    }
//}
