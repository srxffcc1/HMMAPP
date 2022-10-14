package com.healthy.library.utils;

import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Glide Options
 *
 * @author luoyang
 * @version [AndroidLibrary, 2018-04-26]
 */
public class GlideOptions {
    public static RequestOptions withOptions(int placeHolderId, int errorHolderId) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(placeHolderId)
                .error(errorHolderId);
        return requestOptions;
    }

    public static RequestOptions withCircleOptions(int placeHolderId, int errorHolderId) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(placeHolderId)
                .error(errorHolderId)
                .circleCrop();
        return requestOptions;
    }

    public static RequestOptions withRoundedOptions(int radius, int placeHolderId, int errorHolderId) {
        RequestOptions requestOptions = RequestOptions.bitmapTransform(new MultiTransformation(
                new CenterCrop(),
                new RoundedCornersTransformation(radius, 0, RoundedCornersTransformation.CornerType.ALL))).placeholder(placeHolderId)
                .error(errorHolderId);
        return requestOptions;
    }

    public static RequestOptions withRoundedOptions(int radius, RoundedCornersTransformation.CornerType cornerType, int placeHolderId, int errorHolderId) {
        RequestOptions requestOptions = RequestOptions.bitmapTransform(new MultiTransformation(
                new CenterCrop(),
                new RoundedCornersTransformation(radius, 0, cornerType))).placeholder(placeHolderId)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(errorHolderId);
        return requestOptions;
    }
    public static RequestOptions withRoundedOptions(int radius, RoundedCornersTransformation.CornerType cornerType) {
        RequestOptions requestOptions = RequestOptions.bitmapTransform(new MultiTransformation(
                new CenterCrop(),
                new RoundedCornersTransformation(radius, 0, cornerType)))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        return requestOptions;
    }

    public static RequestOptions withLocalRoundedOptions(int radius) {
        return RequestOptions.bitmapTransform(new MultiTransformation(
                new CenterCrop(),
                new RoundedCornersTransformation(radius, 0, RoundedCornersTransformation.CornerType.ALL)));
    }
}
