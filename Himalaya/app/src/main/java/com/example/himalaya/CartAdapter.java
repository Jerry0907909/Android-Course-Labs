package com.example.himalaya;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CartAdapter extends BaseAdapter {

    public interface OnCartItemActionListener {
        void onCartItemClick(CartItem item);

        void onCartItemLongClick(CartItem item);
    }

    private final Context context;
    private final List<CartItem> cartItems;
    private final OnCartItemActionListener actionListener;

    public CartAdapter(Context context, List<CartItem> cartItems, OnCartItemActionListener actionListener) {
        this.context = context;
        this.cartItems = cartItems;
        this.actionListener = actionListener;
    }

    @Override
    public int getCount() {
        return cartItems.size();
    }

    @Override
    public Object getItem(int position) {
        return cartItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_chapter06_cart, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.iv_cart_image);
            holder.nameView = convertView.findViewById(R.id.tv_cart_name);
            holder.subtitleView = convertView.findViewById(R.id.tv_cart_subtitle);
            holder.quantityView = convertView.findViewById(R.id.tv_cart_quantity);
            holder.priceView = convertView.findViewById(R.id.tv_cart_price);
            holder.totalView = convertView.findViewById(R.id.tv_cart_item_total);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CartItem item = cartItems.get(position);
        holder.imageView.setImageResource(item.getProduct().getImageResId());
        holder.nameView.setText(item.getProduct().getName());
        holder.subtitleView.setText(item.getProduct().getSubtitle());
        holder.quantityView.setText(context.getString(R.string.chapter06_quantity_value, item.getQuantity()));
        holder.priceView.setText(context.getString(R.string.chapter06_unit_price_value, item.getProduct().getPrice()));
        holder.totalView.setText(context.getString(R.string.chapter06_price_value, item.getTotalPrice()));
        convertView.setOnClickListener(v -> actionListener.onCartItemClick(item));
        convertView.setOnLongClickListener(v -> {
            actionListener.onCartItemLongClick(item);
            return true;
        });
        return convertView;
    }

    private static final class ViewHolder {
        ImageView imageView;
        TextView nameView;
        TextView subtitleView;
        TextView quantityView;
        TextView priceView;
        TextView totalView;
    }
}
