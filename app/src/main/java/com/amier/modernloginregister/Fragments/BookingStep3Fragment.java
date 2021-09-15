package com.amier.modernloginregister.Fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amier.modernloginregister.Adapter.MyTimeSlotAdapter;
import com.amier.modernloginregister.Common.Common;
import com.amier.modernloginregister.Common.SpacesItemDecoration;
import com.amier.modernloginregister.Interface.ITimeSlotLoadListener;
import com.amier.modernloginregister.R;
import com.amier.modernloginregister.model.TimeSlot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import dmax.dialog.SpotsDialog;

public class BookingStep3Fragment extends Fragment implements ITimeSlotLoadListener {

    //variable
    DocumentReference courseDoc;
    ITimeSlotLoadListener iTimeSlotLoadListener;
    AlertDialog dialog;

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;


    @BindView(R.id.recycler_time_slot)
    RecyclerView recycler_time_slot;
    @BindView(R.id.calendarView)
    HorizontalCalendarView calendarView;
    SimpleDateFormat simpleDateFormat;

    BroadcastReceiver displayTimeSlot = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Calendar date = Calendar.getInstance();
            loadAvailableTimeSlotOfCourseExam(Common.currentCourse.getCourseId(),
                    simpleDateFormat.format(date.getTime()));


        }
    };

    private void loadAvailableTimeSlotOfCourseExam(String courseId, String bookDate) {
        dialog.show();
        //   /AllCities/Pune/Branch/LECEPzzx7GUXzfkamEI5/Courses/91myv76ko8MMtXhZTx86
        courseDoc = FirebaseFirestore.getInstance()
                .collection("AllCities")
                .document(Common.city)
                .collection("Branch")
                .document(Common.currentCenter.getCenterId())
                .collection("Courses")
                .document(Common.currentCourse.getCourseId());

        courseDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) //if course is available
                    {
                        //get information of booking
                        //if not created, return empty
                        CollectionReference date = FirebaseFirestore.getInstance()
                                .collection("AllCities")
                                .document(Common.city)
                                .collection("Branch")
                                .document(Common.currentCenter.getCenterId())
                                .collection("Courses")
                                .document(Common.currentCourse.getCourseId())
                                .collection(bookDate);//01_09_2021
                        date.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    QuerySnapshot querySnapshot = task.getResult();
                                    //if dont have any appointment
                                    if(querySnapshot.isEmpty()) {
                                        iTimeSlotLoadListener.onTimeSlotLoadEmpty();
                                    }
                                    else
                                    {
                                        //if have appointment
                                        List<TimeSlot> timeSlots = new ArrayList<>();
                                        for(QueryDocumentSnapshot document: task.getResult())
                                            timeSlots.add(document.toObject(TimeSlot.class));
                                        iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots);
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                iTimeSlotLoadListener.onTimeSlotLoadFailed(e.getMessage());

                            }
                        });

                    }
                }
            }
        });
    }


    static BookingStep3Fragment instance;

    public static BookingStep3Fragment getInstance(){
        if(instance == null)
            instance = new BookingStep3Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iTimeSlotLoadListener = this;
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(displayTimeSlot,new IntentFilter(Common.KEY_DISPLAY_TIME_SLOT));

        simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");

        dialog = new SpotsDialog.Builder().setContext(getActivity()).setCancelable(false).build();


    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(displayTimeSlot);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        View itemView = inflater.inflate(R.layout.fragment_booking_step_three,container,false);
        unbinder = ButterKnife.bind(this,itemView);
        init(itemView);
        return itemView;
    }

    private void init(View itemView) {
        recycler_time_slot.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        recycler_time_slot.setLayoutManager(gridLayoutManager);
        recycler_time_slot.addItemDecoration( new SpacesItemDecoration(8));

        //Calendra
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE,0);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE,2); // two days left

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(itemView,R.id.calendarView)
                .range(startDate,endDate)
                .datesNumberOnScreen(1)
                .mode(HorizontalCalendar.Mode.DAYS)
                .defaultSelectedDate(startDate)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                    if(Common.currentDate.getTimeInMillis() != date.getTimeInMillis())
                    {
                        Common.currentDate = date;
                        //This code will not load again if you select new day same with day selected
                        loadAvailableTimeSlotOfCourseExam(Common.currentCourse.getCourseId()
                                ,simpleDateFormat.format(date.getTime()));
                    }
            }
        });

    }

    @Override
    public void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList) {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getContext(),timeSlotList);
        recycler_time_slot.setAdapter(adapter);

        dialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadEmpty() {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getContext());
        recycler_time_slot.setAdapter(adapter);

        dialog.dismiss();
    }
}
