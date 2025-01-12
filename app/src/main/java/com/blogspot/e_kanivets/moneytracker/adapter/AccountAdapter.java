package com.blogspot.e_kanivets.moneytracker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.blogspot.e_kanivets.moneytracker.MtApp;
import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.controller.FormatController;
import com.blogspot.e_kanivets.moneytracker.databinding.ViewAccountBinding;
import com.blogspot.e_kanivets.moneytracker.entity.data.Account;

import java.util.List;

import javax.inject.Inject;

public class AccountAdapter extends BaseAdapter {
    @Inject
    FormatController formatController;

    private Context context;
    private List<Account> accounts;

    private int whiteRed;
    private int whiteGreen;
    private int red;
    private int green;
    private int grey;

    @SuppressWarnings("deprecation")
    public AccountAdapter(Context context, List<Account> accounts) {
        MtApp.get().getAppComponent().inject(AccountAdapter.this);

        this.context = context;
        this.accounts = accounts;

        whiteRed = context.getResources().getColor(R.color.white_red);
        whiteGreen = context.getResources().getColor(R.color.white_green);
        red = context.getResources().getColor(R.color.red);
        green = context.getResources().getColor(R.color.green);
        grey = context.getResources().getColor(R.color.grey_inactive);
    }

    @Override
    public int getCount() {
        return accounts.size();
    }

    @Override
    public Object getItem(int position) {
        return accounts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewAccountBinding binding;

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);

            binding = ViewAccountBinding.inflate(layoutInflater, parent, false);
            convertView = binding.getRoot();

            convertView.setTag(binding);
        } else {
            binding = (ViewAccountBinding) convertView.getTag();
        }

        Account account = accounts.get(position);

        if (account.isArchived()) {
            convertView.setBackgroundColor(grey);
        } else {
            convertView.setBackgroundColor(account.getFullSum() >= 0.0 ? whiteGreen : whiteRed);
        }

        binding.tvCurSum.setTextColor(account.getFullSum() >= 0.0 ? green : red);
        binding.tvCurrency.setTextColor(account.getFullSum() >= 0.0 ? green : red);

        binding.tvTitle.setText(account.getTitle());
        binding.tvCurSum.setText(formatController.formatSignedAmount(account.getFullSum()));
        binding.tvCurrency.setText(account.getCurrency());

        return convertView;
    }
}
