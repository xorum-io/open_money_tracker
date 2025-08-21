package com.blogspot.e_kanivets.moneytracker.activity.charts.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blogspot.e_kanivets.moneytracker.adapter.MonthSummaryAdapter
import com.blogspot.e_kanivets.moneytracker.databinding.FragmentSummaryBinding
import com.blogspot.e_kanivets.moneytracker.report.chart.IMonthReport

class SummaryFragment : Fragment() {

    companion object {

        private const val ARG_MONTH_REPORT = "arg_month_report"

        fun newInstance(monthReport: IMonthReport?) = SummaryFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_MONTH_REPORT, monthReport)
            }
        }
    }

    private var monthReport: IMonthReport? = null
    private var binding: FragmentSummaryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            monthReport = args.getParcelable(ARG_MONTH_REPORT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSummaryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        monthReport?.let { report ->
            binding?.listView?.adapter = MonthSummaryAdapter(requireActivity(), report)
        }
    }
}
