package com.example.tpfinaltusi.adicionales;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.tpfinaltusi.fragments.FragmentAprobacionReportes;
import com.example.tpfinaltusi.fragments.FragmentCrearQR;

public class PagerControllerAdmin extends FragmentPagerAdapter {
    int numoftabs;

    public PagerControllerAdmin(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.numoftabs = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentAprobacionReportes();
            case 1:
                return new FragmentCrearQR();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numoftabs;
    }
}
