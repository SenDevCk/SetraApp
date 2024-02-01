package com.bih.nic.bsphcl.adapter;






//import android.app.FragmentManager;
//
//
//
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentPagerAdapter;
//
//import com.bih.nic.bsphcl.fragments.LoginFragment;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by lue on 15-12-2016.
// */
//public class ViewPagerAdapter extends FragmentPagerAdapter {
//    private final List<Fragment> mFragmentList = new ArrayList<>();
//    private final List<String> mFragmentTitleList = new ArrayList<>();
//
//    public ViewPagerAdapter(FragmentManager manager) {
//        super(manager);
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        return mFragmentList.get(position);
//    }
//
//    @Override
//    public int getCount() {
//        return mFragmentList.size();
//    }
//
//    public void addFrag(LoginFragment fragment, String title) {
//        mFragmentList.add(fragment);
//        mFragmentTitleList.add(title);
//    }
//
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return mFragmentTitleList.get(position);
//    }
//}