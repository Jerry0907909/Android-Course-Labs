package com.example.himalaya;

import java.util.ArrayList;
import java.util.List;

public final class Chapter06Data {

    private Chapter06Data() {
    }

    public static List<ShopProduct> getProducts() {
        List<ShopProduct> products = new ArrayList<>();
        products.add(new ShopProduct(
                "xiaomi17",
                "小米17",
                "第五代骁龙8至尊版 | 7000mAh | 黑色",
                3999,
                R.drawable.xiaomi
        ));
        products.add(new ShopProduct(
                "iphone17",
                "苹果17",
                "准新机 | 现货速发 | 紫色",
                6788,
                R.drawable.iphone
        ));
        products.add(new ShopProduct(
                "pura80",
                "华为 Pura 80",
                "12GB+256GB | 丝绒黑 | 国家补贴",
                3599,
                R.drawable.huawei
        ));
        return products;
    }
}
