package org.androidtown.poloride;

import android.graphics.Bitmap;
import com.zomato.photofilters.imageprocessors.Filter;

/**
 * Created by yoonjaepark on 2018. 6. 16..
 */

public class ThumbnailItem {
    public Bitmap image;
    public Filter filter;

    public ThumbnailItem() {
        image = null;
        filter = new Filter();
    }
}
