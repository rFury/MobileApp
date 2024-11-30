package com.example.project.Model;

import android.util.Log;
import android.widget.Toast;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Booking implements Serializable {


    private String bookingID;

    public String pickupTime;
    public String returnTime;

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    private Calendar pickupDate;
    private Calendar returnDate;

    private String bookingStatus="waiting for approval";

    private String customerID;

    private int vehicleID;

    private double totalCost;

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public Booking(String bookingID, Calendar pickupDate, Calendar returnDate, String bookingStatus, String customerID, int vehicleID) {
        this.bookingID = bookingID;
        this.pickupDate = pickupDate;
        this.returnDate = returnDate;
        this.bookingStatus = bookingStatus;
        this.customerID = customerID;
        this.vehicleID = vehicleID;

    }
    public Booking() {
    }
    public Booking( Calendar pickupDate, Calendar returnDate, String bookingStatus, String customerID, int vehicleID) {
        this.pickupDate = pickupDate;
        this.returnDate = returnDate;
        this.bookingStatus = bookingStatus;
        this.customerID = customerID;
        this.vehicleID = vehicleID;

    }


    public String toString(){
        SimpleDateFormat format = new SimpleDateFormat("MMMM, d yyyy hh:mm a");
        return  "\n" +
                "BookingID:         " + bookingID + "\n" +
                "Pickup Date:       " + format.format(pickupDate.getTime()) + "\n" +
                "Return Date:       " + format.format(returnDate.getTime()) + "\n" +
                "Status:            " + bookingStatus + "\n" +
                "CustomerID:        " + customerID + "\n";
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public Calendar getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(Calendar pickupDate) {
        this.pickupDate = pickupDate;
    }

    public Calendar getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Calendar returnDate) {
        this.returnDate = returnDate;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }


    public String getPickupTime(){
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a MMMM, d yyyy");
        return format.format(pickupDate.getTime());
    }

    public String getReturnTime(){
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a MMMM, d yyyy");
        return format.format(returnDate.getTime());
    }
}
