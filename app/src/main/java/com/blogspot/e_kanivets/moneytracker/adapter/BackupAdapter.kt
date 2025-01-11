package com.blogspot.e_kanivets.moneytracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.blogspot.e_kanivets.moneytracker.databinding.ViewBackupItemBinding

class BackupAdapter(
    private val context: Context,
    private val backups: List<String>,
) : BaseAdapter() {

    var onBackupListener: OnBackupListener? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: ViewBackupItemBinding
        var view = convertView

        if (view == null) {
            val layoutInflater = LayoutInflater.from(context)

            binding = ViewBackupItemBinding.inflate(layoutInflater, parent, false)
            view = binding.root

            view.tag = binding
        } else {
            binding =  view.tag as ViewBackupItemBinding
        }

        val backupItem = getItem(position)

        binding.tvTitle.text = backupItem
        binding.ivDelete.setOnClickListener { onBackupListener?.onBackupDelete(backupItem) }

        return view
    }

    override fun getItem(position: Int): String = backups[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = backups.size

    interface OnBackupListener {
        fun onBackupDelete(backupName: String)
    }
}
