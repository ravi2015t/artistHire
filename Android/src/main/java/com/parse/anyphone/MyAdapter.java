package com.parse.anyphone;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TMI on 12/21/2016.
 */

public class MyAdapter extends BaseAdapter {
    private final List<Item> mItems = new ArrayList<Item>();
    private final LayoutInflater mInflater;

    public MyAdapter(Context context) {
        mInflater = LayoutInflater.from(context);

//        mItems.add(new Item("Red",       R.drawable.logo));
//        mItems.add(new Item("Magenta",   R.drawable.nobody_m));

    }

    public MyAdapter(Context context, Item item) {
        mInflater = LayoutInflater.from(context);
        mItems.add(item);
    }

    public MyAdapter(Context context, ArrayList<Item> item) {
        mInflater = LayoutInflater.from(context);
        for(int i = 0; i < item.size(); i++){
            mItems.add(item.get(i));
        }
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Item getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return (long) 2.0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;
        TextView name;

        if (v == null) {
            v = mInflater.inflate(R.layout.grid_item, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
        }

        picture = (ImageView) v.getTag(R.id.picture);
        name = (TextView) v.getTag(R.id.text);

        Item item = getItem(i);

        picture.setImageBitmap(item.bitmap);
        name.setText(item.name);

        return v;
    }

//    private static class Item {
//        public final String name;
//        public final int drawableId;
//
//        Item(String name, int drawableId) {
//            this.name = name;
//            this.drawableId = drawableId;
//        }
//    }
}