package com.example.himalaya;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class GoodsAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private final List<ShopProduct> products;
    private final AddCartListener addCartListener;

    public GoodsAdapter(Context context, List<ShopProduct> products, AddCartListener addCartListener) {
        this.inflater = LayoutInflater.from(context);
        this.products = products;
        this.addCartListener = addCartListener;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_chapter06_product, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ShopProduct product = products.get(position);
        holder.productImageView.setImageResource(product.getImageResId());
        holder.productNameView.setText(product.getName());
        holder.productSubtitleView.setText(product.getSubtitle());
        holder.productPriceView.setText(
                parent.getContext().getString(R.string.chapter06_price_value, product.getPrice())
        );
        holder.addCartButton.setOnClickListener(v -> addCartListener.addToCart(product));
        return view;
    }

    private static final class ViewHolder {
        private final ImageView productImageView;
        private final TextView productNameView;
        private final TextView productSubtitleView;
        private final TextView productPriceView;
        private final Button addCartButton;

        private ViewHolder(View itemView) {
            productImageView = itemView.findViewById(R.id.iv_product_image);
            productNameView = itemView.findViewById(R.id.tv_product_name);
            productSubtitleView = itemView.findViewById(R.id.tv_product_subtitle);
            productPriceView = itemView.findViewById(R.id.tv_product_price);
            addCartButton = itemView.findViewById(R.id.btn_add_to_cart);
        }
    }

    public interface AddCartListener {
        void addToCart(ShopProduct product);
    }
}
