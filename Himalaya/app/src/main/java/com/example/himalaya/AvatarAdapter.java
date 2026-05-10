package com.example.himalaya;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class AvatarAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private final int[] avatarResIds;

    public AvatarAdapter(Context context, int[] avatarResIds) {
        this.inflater = LayoutInflater.from(context);
        this.avatarResIds = avatarResIds;
    }

    @Override
    public int getCount() {
        return avatarResIds.length;
    }

    @Override
    public Object getItem(int position) {
        return avatarResIds[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_avatar, parent, false);
        }

        ImageView avatarView = view.findViewById(R.id.iv_avatar_item);
        avatarView.setImageResource(avatarResIds[position]);
        return view;
    }
}
