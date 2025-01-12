package com.blogspot.e_kanivets.moneytracker.activity.account.edit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blogspot.e_kanivets.moneytracker.MtApp
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.adapter.RecordAdapter
import com.blogspot.e_kanivets.moneytracker.controller.FormatController
import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController
import com.blogspot.e_kanivets.moneytracker.controller.data.RecordController
import com.blogspot.e_kanivets.moneytracker.controller.data.TransferController
import com.blogspot.e_kanivets.moneytracker.databinding.FragmentAccountOperationsBinding
import com.blogspot.e_kanivets.moneytracker.entity.RecordItem
import com.blogspot.e_kanivets.moneytracker.entity.data.Account
import com.blogspot.e_kanivets.moneytracker.entity.data.Category
import com.blogspot.e_kanivets.moneytracker.entity.data.Record
import com.blogspot.e_kanivets.moneytracker.entity.data.Transfer
import com.blogspot.e_kanivets.moneytracker.util.RecordItemsBuilder
import javax.inject.Inject

class AccountOperationsFragment : Fragment() {

    @Inject
    internal lateinit var accountController: AccountController

    @Inject
    internal lateinit var recordController: RecordController

    @Inject
    internal lateinit var transferController: TransferController

    @Inject
    internal lateinit var formatController: FormatController

    private lateinit var account: Account

    private lateinit var binding: FragmentAccountOperationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAccountOperationsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initData() {
        MtApp.get().appComponent.inject(this@AccountOperationsFragment)
        arguments?.let { arguments -> account = arguments.getParcelable(KEY_ACCOUNT)!! }
    }

    private fun initViews() {
        binding.recyclerView.adapter = RecordAdapter(requireContext(), getRecordItems(), false)
    }

    private fun getRecordItems(): List<RecordItem> {
        val accountRecords = recordController.getRecordsForAccount(account)
        val accountTransfers = transferController.getTransfersForAccount(account)

        accountRecords += obtainRecordsFromTransfers(accountTransfers)
        accountRecords.sortByDescending { it.time }

        return RecordItemsBuilder().getRecordItems(accountRecords)
    }

    private fun obtainRecordsFromTransfers(transfers: List<Transfer>): List<Record> {
        val records = mutableListOf<Record>()

        transfers.forEach {
            val type = if (it.fromAccountId == account.id) Record.TYPE_EXPENSE else Record.TYPE_INCOME
            val title = constructRecordTitle(type, it)
            val category = Category(getString(R.string.transfer).toLowerCase())
            val notes = getString(R.string.account_transfer)
            val price = if (type == Record.TYPE_EXPENSE) it.fromAmount else it.toAmount
            val decimals = if (type == Record.TYPE_EXPENSE) it.fromDecimals else it.toDecimals

            records += Record(it.id, it.time, type, title, category, notes, price, account, account.currency, decimals)
        }

        return records.toList()
    }

    private fun constructRecordTitle(type: Int, transfer: Transfer): String {
        val titlePrefix = getString(if (type == Record.TYPE_EXPENSE) R.string.to else R.string.from)
        val oppositeAccountId = if (type == Record.TYPE_EXPENSE) transfer.toAccountId else transfer.fromAccountId
        val oppositeAccountTitle = "$titlePrefix ${accountController.read(oppositeAccountId)?.title}"
        return "${getString(R.string.transfer)} $oppositeAccountTitle".toLowerCase()
    }

    companion object {

        private const val KEY_ACCOUNT = "key_account"

        fun newInstance(account: Account): AccountOperationsFragment {
            val fragment = AccountOperationsFragment()
            val arguments = Bundle()
            arguments.putParcelable(KEY_ACCOUNT, account)
            fragment.arguments = arguments
            return fragment
        }

    }
}
