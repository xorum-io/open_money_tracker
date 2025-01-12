package com.blogspot.e_kanivets.moneytracker.activity.account.edit.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blogspot.e_kanivets.moneytracker.MtApp
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.controller.FormatController
import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController
import com.blogspot.e_kanivets.moneytracker.databinding.FragmentEditAccountBinding
import com.blogspot.e_kanivets.moneytracker.entity.data.Account
import com.blogspot.e_kanivets.moneytracker.util.CrashlyticsProxy
import com.blogspot.e_kanivets.moneytracker.util.validator.EditAccountValidator
import com.blogspot.e_kanivets.moneytracker.util.validator.IValidator
import javax.inject.Inject

class EditAccountFragment : Fragment() {

    @Inject
    internal lateinit var accountController: AccountController

    @Inject
    internal lateinit var formatController: FormatController

    private lateinit var accountValidator: IValidator<Account>
    private lateinit var account: Account

    private lateinit var binding: FragmentEditAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEditAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initData() {
        MtApp.get().appComponent.inject(this@EditAccountFragment)
        arguments?.let { arguments -> account = arguments.getParcelable(KEY_ACCOUNT)!! }
    }

    private fun initViews(view: View) {
        binding.etTitle.setText(account.title)
        binding.etGoal.setText(formatController.formatPrecisionNone(account.goal))
        binding.viewColor.setBackgroundColor(account.color)

        val fabDone = view.rootView.findViewById<FloatingActionButton>(R.id.fabDone)
        fabDone.setOnClickListener { done() }

        accountValidator = EditAccountValidator(requireContext(), binding)
    }

    private fun done() {
        CrashlyticsProxy.get().logButton("Edit Account")
        if (accountValidator.validate()) {
            val title = binding.etTitle.text.toString().trim { it <= ' ' }
            val goal = binding.etGoal.text.toString().toDouble()

            val newAccount = Account(
                account.id, title, account.curSum.toDouble(),
                account.currency, goal, account.isArchived, account.color
            )
            val updated = accountController.update(newAccount) != null
            if (updated) {
                CrashlyticsProxy.get().logEvent("Edit Account")
                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            }
        }
    }

    companion object {

        private const val KEY_ACCOUNT = "key_account"

        fun newInstance(account: Account): EditAccountFragment {
            val fragment = EditAccountFragment()
            val arguments = Bundle()
            arguments.putParcelable(KEY_ACCOUNT, account)
            fragment.arguments = arguments
            return fragment
        }
    }
}
