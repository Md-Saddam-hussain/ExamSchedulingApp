package com.amier.modernloginregister.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.amier.modernloginregister.Fragments.BookingStep1Fragment;
import com.amier.modernloginregister.Fragments.BookingStep2Fragment;
import com.amier.modernloginregister.Fragments.BookingStep3Fragment;
import com.amier.modernloginregister.Fragments.BookingStep4Fragment;

public class MyViewPagerAdapter extends FragmentPagerAdapter {
    public MyViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0 :
                return BookingStep1Fragment.getInstance();
            case 1 :
                return BookingStep2Fragment.getInstance();
            case 2 :
                return BookingStep3Fragment.getInstance();
            case 3 :
                return BookingStep4Fragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount()
    {
        //return 0;
        return 4;
    }
}
