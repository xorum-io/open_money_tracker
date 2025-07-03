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

class CategoryAutoCompleteAdapter(
    context: Context,
    resource: Int,
    private val autoCompleter: CategoryAutoCompleter
) : ArrayAdapter<String>(context, resource), Filterable {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ViewCategoryItemBinding
        var view = convertView
        if (view == null) {
            binding = ViewCategoryItemBinding.inflate(LayoutInflater.from(context), parent, false)
            view = binding.root
            view.tag = binding
        } else {
            binding = view.tag as ViewCategoryItemBinding
        }
        val category = getItem(position)
        binding.tvCategory.text = category
        binding.ivCancel.setOnClickListener {
            autoCompleter.removeFromAutoComplete(category)
            remove(category)
            notifyDataSetChanged()
        }
        return view
    }

    @NotNull
    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults = FilterResults()
            
            val tempList = if (constraint != null) {
                autoCompleter.completeByPart(constraint.toString())
            } else {
                ArrayList<String>()
            }

            filterResults.values = tempList
            filterResults.count = tempList.size

            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            clear()
            if (results != null && results.count > 0) {
                addAll(results.values as List<String>)
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }
    }
}
