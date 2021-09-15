package com.amier.modernloginregister.model;

public class BookingInformation {

    private String studentName,studentPhone,time,courseName,courseId,centerName,centerId,centerAddress;
    private Long slot;

    public BookingInformation() {
    }

    public BookingInformation(String studentName, String studentPhone, String time, String courseName, String courseId, String centerName, String centerId, String centerAddress, Long slot) {
        this.studentName = studentName;
        this.studentPhone = studentPhone;
        this.time = time;
        this.courseName = courseName;
        this.courseId = courseId;
        this.centerName = centerName;
        this.centerId = centerId;
        this.centerAddress = centerAddress;
        this.slot = slot;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentPhone() {
        return studentPhone;
    }

    public void setStudentPhone(String studentPhone) {
        this.studentPhone = studentPhone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getCenterId() {
        return centerId;
    }

    public void setCenterId(String centerId) {
        this.centerId = centerId;
    }

    public String getCenterAddress() {
        return centerAddress;
    }

    public void setCenterAddress(String centerAddress) {
        this.centerAddress = centerAddress;
    }

    public Long getSlot() {
        return slot;
    }

    public void setSlot(Long slot) {
        this.slot = slot;
    }
}
