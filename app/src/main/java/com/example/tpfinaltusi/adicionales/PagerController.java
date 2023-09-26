package com.example.tpfinaltusi.adicionales;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.tpfinaltusi.fragments.FragmentCrearReporte;
import com.example.tpfinaltusi.fragments.FragmentNoticia;
import com.example.tpfinaltusi.fragments.FragmentPerfil;
import com.example.tpfinaltusi.fragments.FragmentQR;
import com.example.tpfinaltusi.fragments.FragmentReporte;

public class PagerController extends FragmentPagerAdapter {
    int numoftabs;

    public PagerController(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.numoftabs = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentNoticia();
            case 1:
                return new FragmentReporte();
            case 2:
                return new FragmentQR();
            case 3:
                return new FragmentCrearReporte();
            case 4:
                return new FragmentPerfil();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numoftabs;
    }
}
