package com.amier.modernloginregister.Interface;

import com.amier.modernloginregister.model.TimeSlot;
import com.amier.modernloginregister.model.TimeSlot;

import java.util.List;

public interface ITimeSlotLoadListener {
    void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList);
    void onTimeSlotLoadFailed(String message);
    void onTimeSlotLoadEmpty();


}
