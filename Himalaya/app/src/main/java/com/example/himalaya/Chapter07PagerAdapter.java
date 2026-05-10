package com.example.himalaya;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class Chapter07PagerAdapter extends FragmentStateAdapter {

    private final Chapter07EditorFragment editorFragment = new Chapter07EditorFragment();
    private final Chapter07ListFragment listFragment = Chapter07ListFragment.newInstance();

    public Chapter07PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return position == 0 ? editorFragment : listFragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public Chapter07ListFragment getListFragment() {
        return listFragment;
    }
}
