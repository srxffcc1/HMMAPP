package com.healthy.library.model;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

import com.healthy.library.R;

/**
 * @author: long
 * @date: 2021/4/15
 * @des
 */
public class TitleModel {

    private String title;
    private String rightTitle;
    private boolean isShowRight = true;
    private boolean isDrawableRightShow;
    @DrawableRes
    private int drawableRight = R.drawable.mall_ic_more_normal;
    @DrawableRes
    private int backgroundDrawable = -1;
    @ColorInt
    private int solidColor = -1;
    private boolean isHtmlText;

    private int titleIcon;
    private boolean isRadius;
    private int radius = 12;//默认全部圆角12
    private int topLeftRadius;
    private int topRightRadius;
    private int bottomLeftRadius;
    private int bottomRightRadius;

    public TitleModel() {
    }

    public TitleModel(String title, String rightTitle) {
        this.title = title;
        this.rightTitle = rightTitle;
    }

    public TitleModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public TitleModel setRightTitle(String rightTitle) {
        this.rightTitle = rightTitle;
        return this;
    }

    public TitleModel setShowRight(boolean isShowRight) {
        this.isShowRight = isShowRight;
        return this;
    }

    public TitleModel setDrawableRightShow(boolean isDrawableRightShow) {
        this.isDrawableRightShow = isDrawableRightShow;
        return this;
    }

    public TitleModel setDrawableRight(int drawableRight) {
        this.drawableRight = drawableRight;
        return this;
    }

    public TitleModel setBackgroundDrawable(int backgroundDrawable) {
        this.backgroundDrawable = backgroundDrawable;
        return this;
    }

    public TitleModel setSolidColor(int solidColor) {
        this.solidColor = solidColor;
        return this;
    }

    public TitleModel setHtmlText(boolean htmlText) {
        isHtmlText = htmlText;
        return this;
    }

    public TitleModel setTitleIcon(int titleIcon) {
        this.titleIcon = titleIcon;
        return this;
    }

    public TitleModel setRadius(boolean radius) {
        isRadius = radius;
        return this;
    }
    public TitleModel setRadius(int radius) {
        this.radius = radius;
        return this;
    }
    public TitleModel setTopLeftRadius(int topLeftRadius) {
        this.topLeftRadius = topLeftRadius;
        return this;
    }
    public TitleModel setTopRightRadius(int topRightRadius) {
        this.topRightRadius = topRightRadius;
        return this;
    }
    public TitleModel setBottomLeftRadius(int bottomLeftRadius) {
        this.bottomLeftRadius = bottomLeftRadius;
        return this;
    }
    public TitleModel setBottomRightRadius(int bottomRightRadius) {
        this.bottomRightRadius = bottomRightRadius;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getRightTitle() {
        return rightTitle;
    }

    public boolean isShowRight() {
        return isShowRight;
    }

    public boolean isDrawableRightShow() {
        return isDrawableRightShow;
    }

    public int getDrawableRight() {
        return drawableRight;
    }

    public int getBackgroundDrawable() {
        return backgroundDrawable;
    }

    public int getSolidColor() {
        return solidColor;
    }

    public boolean isHtmlText() {
        return isHtmlText;
    }

    public int getTitleIcon() {
        return titleIcon;
    }

    public boolean isRadius() {
        return isRadius;
    }

    public int getRadius() {
        return radius;
    }

    public int getTopLeftRadius() {
        return topLeftRadius;
    }

    public int getTopRightRadius() {
        return topRightRadius;
    }

    public int getBottomLeftRadius() {
        return bottomLeftRadius;
    }

    public int getBottomRightRadius() {
        return bottomRightRadius;
    }


}
