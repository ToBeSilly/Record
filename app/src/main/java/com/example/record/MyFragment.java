package com.example.record;

/**
 * 重写MyFragment，添加了tablayout
 */

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class MyFragment extends FragmentStatePagerAdapter {
    private String[] tabTitles = new String[]{"憨憨日记","我的"};
    public MyFragment( FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 2;
    }

    /**
     * 获得各个Fragment的标题
     * @param position Fragment位置
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new BlankFragment();
            case 1:
                return new BlankFragment2();
            default:
                throw new RuntimeException("Invalid tab position");
        }
    }
}


