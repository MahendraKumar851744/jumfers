package com.jumfers.mocktestseries.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentUtil {
    private FragmentManager fragmentManager;
    private int containerId;

    public FragmentUtil(FragmentManager fragmentManager, int containerId) {
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
    }

    public void addFragment(Fragment fragment, String tag) {
        if (tag == null) {
            tag = "fragment_" + System.currentTimeMillis();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(containerId, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }


    public void replaceFragment(Fragment fragment, String tag) {
        Fragment existingFragment = fragmentManager.findFragmentByTag(tag);
        if (existingFragment != null) {
            fragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerId, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }


    public void popBackStack() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    }



    public void popBackStackImmediate() {
        fragmentManager.popBackStackImmediate();
    }

    public int getBackStackEntryCount() {
        return fragmentManager.getBackStackEntryCount();
    }
}
