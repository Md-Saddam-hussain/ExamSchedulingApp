package com.amier.modernloginregister.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amier.modernloginregister.Adapter.MyCourseAdapter;
import com.amier.modernloginregister.Common.Common;
import com.amier.modernloginregister.Common.SpacesItemDecoration;
import com.amier.modernloginregister.R;
import com.amier.modernloginregister.model.Courser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookingStep2Fragment extends Fragment {

    static BookingStep2Fragment instance;

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;

    @BindView(R.id.recycler_course)
    RecyclerView recycler_course;

    private BroadcastReceiver courseDoneReceiver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Courser> courseArrayList = intent.getParcelableArrayListExtra(Common.KEY_COURSE_LOAD_DONE);
            //Create Adapter Late
            MyCourseAdapter adapter = new MyCourseAdapter(getContext(),courseArrayList);
            recycler_course.setAdapter(adapter);
        }
    };

    public static BookingStep2Fragment getInstance(){
        if(instance == null)
            instance = new BookingStep2Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(courseDoneReceiver,new IntentFilter(Common.KEY_COURSE_LOAD_DONE));
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(courseDoneReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        View itemView = inflater.inflate(R.layout.fragment_booking_step_two,container,false);
        unbinder = ButterKnife.bind(this,itemView);
        
        initView();
        
        return itemView;
    }

    private void initView() {
        recycler_course.setHasFixedSize(true);
        recycler_course.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recycler_course.addItemDecoration(new SpacesItemDecoration(4));
    }
}
