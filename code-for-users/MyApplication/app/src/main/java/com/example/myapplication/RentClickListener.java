package com.example.myapplication;

import android.view.View;

import java.text.ParseException;

public interface RentClickListener {
    void rentClick(View view, int position) throws ParseException;
}
