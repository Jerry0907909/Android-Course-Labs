package com.example.himalaya;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Chapter07Activity extends AppCompatActivity
        implements Chapter07EditorFragment.OnBillSavedListener {

    private ViewPager2 viewPager;
    private Chapter07PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chapter07);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chapter07_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.tv_chapter07_back).setOnClickListener(v -> finish());

        TabLayout tabLayout = findViewById(R.id.tab_chapter07);
        viewPager = findViewById(R.id.vp_chapter07);
        pagerAdapter = new Chapter07PagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
                tab.setText(position == 0
                        ? R.string.chapter07_tab_editor
                        : R.string.chapter07_tab_list)).attach();
    }

    @Override
    public void onBillSaved(String month) {
        viewPager.setCurrentItem(1, true);
        pagerAdapter.getListFragment().refreshContent(month);
    }
}
