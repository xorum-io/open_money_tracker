package com.blogspot.e_kanivets.moneytracker.activity.charts.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blogspot.e_kanivets.moneytracker.databinding.FragmentGraphBinding
import com.blogspot.e_kanivets.moneytracker.report.chart.BarChartConverter
import com.blogspot.e_kanivets.moneytracker.report.chart.IMonthReport
import com.github.mikephil.charting.data.BarData

class GraphFragment : Fragment() {
    companion object {
        private const val ARG_MONTH_REPORT = "arg_month_report"
        private const val ARG_NO_DATA_TEXT = "arg_no_data_text"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param monthReport report for some period grouped by months.
         * @return A new instance of fragment GraphFragment.
         */
        fun newInstance(monthReport: IMonthReport): GraphFragment {
            val fragment = GraphFragment()
            val args = Bundle()
            args.putParcelable(ARG_MONTH_REPORT, monthReport)
            fragment.arguments = args
            return fragment
        }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param noDataText text that will be displayed in case of error.
         * @return A new instance of fragment GraphFragment.
         */
        fun newInstance(noDataText: String): GraphFragment {
            val fragment = GraphFragment()
            val args = Bundle()
            args.putString(ARG_NO_DATA_TEXT, noDataText)
            fragment.arguments = args
            return fragment
        }
    }

    private var monthReport: IMonthReport? = null
    private var noDataText: String? = null

    private var binding: FragmentGraphBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            monthReport = args.getParcelable(ARG_MONTH_REPORT)
            noDataText = args.getString(ARG_NO_DATA_TEXT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                             savedInstanceState: Bundle?): View? {
        binding = FragmentGraphBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        if (monthReport == null) {
            binding?.barChart?.setNoDataText(noDataText)
        } else {
            monthReport?.let { report ->
                val barChartConverter = BarChartConverter(requireContext(), report)

                val barData = BarData(barChartConverter.xAxisValueList,
                        barChartConverter.barDataSetList)
                barData.setDrawValues(false)

                binding?.barChart?.apply {
                    setData(barData)
                    setDescription(null)
                    setVisibleXRangeMinimum(8f)
                    setScaleYEnabled(false)
                    setVisibleXRangeMaximum(34f)
                    setHighlightPerDragEnabled(false)
                    setHighlightPerTapEnabled(false)
                }
            }
        }
    }
}
