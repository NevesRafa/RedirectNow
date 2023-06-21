package com.kansha.redirectNow.presentation.create

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.kansha.redirectNow.R
import com.kansha.redirectNow.data.model.PhoneDetails
import com.kansha.redirectNow.databinding.FragmentCreateOrEditBinding
import com.kansha.redirectNow.domain.countryList
import com.kansha.redirectNow.internal.extension.PhoneNumberTextWatcher
import com.kansha.redirectNow.internal.extension.setErrorStyle
import org.koin.android.ext.android.inject

class CreateOrEditFragment(val clickOnSave: (PhoneDetails) -> Unit) : BottomSheetDialogFragment() {

    private var toast: Toast? = null

    companion object {
        const val EXTRA_PHONE_EDIT = "extra_phone_edit"
    }

    private val viewModel: CreateOrEditViewModel by inject()
    private lateinit var binding: FragmentCreateOrEditBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateOrEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        loadDataToEdit()
        saveButton()
        showDropDown()

        val editText = binding.phoneNumber
        editText.addTextChangedListener(PhoneNumberTextWatcher(editText))
    }

    private fun setupObservers() {
        viewModel.loadStateLiveData.observe(this) { state ->
            when (state) {
                is CreateOrEditState.Loading -> {}
                is CreateOrEditState.Save -> saveOrEdit(state.phoneDetails)
                is CreateOrEditState.InvalidDdi -> toastInvalidDdi()
                is CreateOrEditState.Edit -> showContactForEditOnTheScreen(state.phoneDetails)
                is CreateOrEditState.Error -> showErrorMessage(state.errorMessage)
            }
        }
    }

    private fun toastInvalidDdi() {
        Toast.makeText(requireContext(), "DDI inválido", Toast.LENGTH_SHORT).show()
    }

    private fun loadDataToEdit() {
        val intentData = arguments
        val loadedContact: PhoneDetails? = intentData?.getParcelable(EXTRA_PHONE_EDIT)
        viewModel.isEditing(loadedContact)
    }

    private fun showContactForEditOnTheScreen(contactForEdit: PhoneDetails?) {
        binding.contact.setText(contactForEdit?.contact)
        binding.phoneNumber.setText(contactForEdit?.phoneNumber)
        binding.fabSaveRedirect.text = getString(R.string.save_change)
    }

    private fun saveOrEdit(contact: PhoneDetails) {
        clickOnSave(contact)
        dismissAllowingStateLoss()
    }

    private fun saveButton() {
        binding.fabSaveRedirect.setOnClickListener {
            val contactTyped = binding.contact.text.toString()
            val phoneNumberTyped = binding.phoneNumber.text.toString()
            val ddiTyped = binding.ddi.text.toString()
            val flagCode = getFlagCode(ddiTyped)

            if (contactTyped.isBlank() || phoneNumberTyped.isBlank() || ddiTyped.isBlank()) {
                Toast.makeText(requireContext(), "Por favor, preencha os campos.", Toast.LENGTH_LONG).show()
            } else {
                viewModel.checksSaveOrEdit(contactTyped, ddiTyped, phoneNumberTyped, flagCode)
                hideKeyboard()
            }
        }
    }

    private fun getFlagCode(ddiTyped: String): String {
        val code = countryList.find { it.countryCode == ddiTyped }
        return code?.flagCode ?: ""
    }

    private fun hideKeyboard() {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun showDropDown() {
        val countryCodes = countryList.map { it.countryCode }
        val adapter = ArrayAdapter(requireContext(), R.layout.ddi_dropdown_item, countryCodes)

        binding.ddi.setAdapter(adapter)

        binding.ddi.setOnItemClickListener { _, _, _, _ ->
            val typedText = binding.ddi.text.toString()
            val countryName = getCountryName(typedText)
            showToast(countryName)
        }
    }

    private fun getCountryName(countryCode: String): String {
        val country = countryList.find { it.countryCode == countryCode }
        return country?.name ?: ""
    }

    private fun showToast(message: String) {
        toast?.cancel()

        if (message.isNotEmpty()) {
            toast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
            toast?.show()
        }
    }

    private fun showErrorMessage(errorMessage: String?) {
        Snackbar.make(binding.root, getString(R.string.error_message, errorMessage), Snackbar.LENGTH_LONG)
            .setErrorStyle()
            .show()
    }
}