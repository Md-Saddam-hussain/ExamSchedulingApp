package com.amier.modernloginregister.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.amier.modernloginregister.Common.Common;
import com.amier.modernloginregister.R;
import com.amier.modernloginregister.model.BookingInformation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BookingStep4Fragment  extends Fragment {

    SimpleDateFormat simpleDateFormat;
    LocalBroadcastManager localBroadcastManager;
    Unbinder unbinder;

    //txt_booking_course_text
    @BindView(R.id.txt_booking_course_text)
    TextView txt_booking_course_text;

    //txt_booking_time_txt
    @BindView(R.id.txt_booking_time_txt)
    TextView txt_booking_time_txt;

    //txt_center_address_text
    @BindView(R.id.txt_center_address_text)
    TextView txt_center_address_text;

    //txt_center_name_text
    @BindView(R.id.txt_center_name_text)
    TextView txt_center_name_text;

    //txt_center_website_text
    @BindView(R.id.txt_center_website_text)
    TextView txt_center_website_text;

    //txt_center_phone_number_text
    @BindView(R.id.txt_center_phone_number_text)
    TextView txt_center_phone_number_text;

    //txt_center_opening_hrs_text
    @BindView(R.id.txt_center_opening_hrs_text)
    TextView txt_center_opening_hrs_text;

    @OnClick(R.id.btn_confirm)
    void confirmBooking(){
        BookingInformation bookingInformation = new BookingInformation();

        bookingInformation.setCourseId(Common.currentCourse.getCourseId());
        bookingInformation.setCourseName(Common.currentCourse.getName());

        bookingInformation.setCenterId(Common.currentCenter.getCenterId());
        bookingInformation.setCourseName(Common.currentCenter.getName());

        bookingInformation.setTime(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                .append(" at ")
                .append(simpleDateFormat.format(Common.currentDate.getTime())).toString());

        bookingInformation.setSlot(Long.valueOf(Common.currentTimeSlot));

        DocumentReference bookingDate =  FirebaseFirestore.getInstance()
                .collection("AllCities")
                .document(Common.city)
                .collection("Branch")
                .document(Common.currentCenter.getCenterId())
                .collection("Courses")
                .document(Common.currentCourse.getCourseId())
                .collection(Common.simpleDateFormat.format(Common.currentDate.getTime()))
                .document(String.valueOf(Common.currentTimeSlot));

        //Write data
        bookingDate.set(bookingInformation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //getActivity().finish();
                        Toast.makeText(getContext(),"Success Booking!",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }


    BroadcastReceiver confirmBookingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    };

    private void setData() {
        txt_booking_course_text.setText(Common.currentCourse.getName());
        txt_booking_time_txt.setText(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
        .append(" at ")
        .append(simpleDateFormat.format(Common.currentDate.getTime())));

        txt_center_address_text.setText(Common.currentCenter.getAddress());
        txt_center_website_text.setText(Common.currentCenter.getWebsite());
        txt_center_phone_number_text.setText(Common.currentCenter.getPhone());
        txt_center_name_text.setText(Common.currentCenter.getName());
        txt_center_opening_hrs_text.setText(Common.currentCenter.getOpenHours());

    }

    static BookingStep4Fragment instance;

    public static BookingStep4Fragment getInstance(){
        if(instance == null)
            instance = new BookingStep4Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Apply format for date display
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(confirmBookingReceiver, new IntentFilter(Common.KEY_CONFIRM_BOOKING));


    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confirmBookingReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View itemView = inflater.inflate(R.layout.fragment_booking_step_four,container,false);
        unbinder = ButterKnife.bind(this,itemView);
        return itemView;


    }
}
