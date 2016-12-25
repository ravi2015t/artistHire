package com.parse.anyphone;

import android.graphics.Bitmap;

/**
 * Created by TMI on 12/21/2016.
 */

public class Item {
    public final String name;
//    public final int drawableId;
    public final Bitmap bitmap;
    Item(String name, Bitmap bitmap) {
        this.name = name;
        this.bitmap = bitmap;
    }
}
