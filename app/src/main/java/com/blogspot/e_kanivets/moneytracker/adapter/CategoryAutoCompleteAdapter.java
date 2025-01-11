package com.blogspot.e_kanivets.moneytracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.blogspot.e_kanivets.moneytracker.databinding.ViewCategoryItemBinding;
import com.blogspot.e_kanivets.moneytracker.util.CategoryAutoCompleter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CategoryAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    private final CategoryAutoCompleter autoCompleter;

    public CategoryAutoCompleteAdapter(Context context, int resource, CategoryAutoCompleter autoCompleter) {
        super(context, resource);
        this.autoCompleter = autoCompleter;
    }

    @Override
    public View getView(int position, View convertView, @NotNull ViewGroup parent) {
        ViewCategoryItemBinding binding;

        if (convertView == null) {
            binding = ViewCategoryItemBinding.inflate(LayoutInflater.from(getContext()), parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (ViewCategoryItemBinding) convertView.getTag();
        }

        final String category = getItem(position);

        binding.tvCategory.setText(category);
        binding.ivCancel.setOnClickListener(v -> {
            autoCompleter.removeFromAutoComplete(category);
            remove(category);
            notifyDataSetChanged();
        });

        return convertView;
    }

    @NotNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                List<String> tempList;

                if (constraint != null)
                    tempList = autoCompleter.completeByPart(constraint.toString());
                else tempList = new ArrayList<>();

                filterResults.values = tempList;
                filterResults.count = tempList.size();

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();

                if (results != null && results.count > 0) {
                    addAll((List) results.values);
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
