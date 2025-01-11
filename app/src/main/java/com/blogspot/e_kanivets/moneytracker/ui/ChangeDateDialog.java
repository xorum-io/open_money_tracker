package com.blogspot.e_kanivets.moneytracker.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.databinding.DialogChangeDateBinding;

import java.util.Calendar;
import java.util.Date;

public class ChangeDateDialog extends AlertDialog {

    private Date date;
    private OnDateChangedListener listener;

    private DialogChangeDateBinding binding;

    public ChangeDateDialog(Context context, Date date, OnDateChangedListener listener) {
        super(context);
        this.date = date;
        this.listener = listener;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DialogChangeDateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        binding.datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
        binding.bOk.setOnClickListener(view -> ok());
        binding.bCancel.setOnClickListener(view -> dismiss());
    }

    public void ok() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, binding.datePicker.getYear());
        cal.set(Calendar.MONTH, binding.datePicker.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, binding.datePicker.getDayOfMonth());

        listener.OnDataChanged(cal.getTime());
        dismiss();
    }

    public interface OnDateChangedListener {
        void OnDataChanged(Date date);
    }
}
