package com.kansha.redirectNow.presentation.create

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.kansha.redirectNow.R
import com.kansha.redirectNow.data.model.PhoneDetails
import com.kansha.redirectNow.databinding.FragmentCreateOrEditBinding
import com.kansha.redirectNow.internal.extension.PhoneNumberTextWatcher
import com.kansha.redirectNow.internal.extension.setErrorStyle
import org.koin.android.ext.android.inject

class CreateOrEditFragment(val clickOnSave: (PhoneDetails) -> Unit) : BottomSheetDialogFragment() {


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

        val editText = binding.phoneNumber
        editText.addTextChangedListener(PhoneNumberTextWatcher(editText))
    }

    private fun setupObservers() {
        viewModel.loadStateLiveData.observe(this) { state ->
            when (state) {
                is CreateOrEditState.Loading -> {}
                is CreateOrEditState.Save -> saveOrEdit(state.phoneDetails)
                is CreateOrEditState.Edit -> showContactForEditOnTheScreen(state.phoneDetails)
                is CreateOrEditState.Error -> showErrorMessage(state.errorMessage)
            }
        }
    }

    private fun loadDataToEdit() {
        val intentData = arguments
        val loadedContact: PhoneDetails? = intentData?.getParcelable(EXTRA_PHONE_EDIT)
        viewModel.isEditing(loadedContact)
    }

    private fun showContactForEditOnTheScreen(contactForEdit: PhoneDetails?) {
        binding.contact.setText(contactForEdit?.contact)
        binding.phoneNumber.setText(contactForEdit?.phoneNumber)
    }

    private fun saveOrEdit(contact: PhoneDetails) {
        clickOnSave(contact)
        dismissAllowingStateLoss()
    }

    private fun saveButton() {
        binding.fabSaveRedirect.setOnClickListener {
            val contactTyped = binding.contact.text.toString()
            val phoneNumberTyped = binding.phoneNumber.text.toString()

            if (contactTyped.isBlank() || phoneNumberTyped.isBlank()) {
                Toast.makeText(requireContext(), "Por favor, preencha os campos.", Toast.LENGTH_LONG).show()
            } else {
                viewModel.checksSaveOrEdit(contactTyped, phoneNumberTyped)
                hideKeyboard()
            }
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun showErrorMessage(errorMessage: String?) {
        Snackbar.make(binding.root, getString(R.string.error_message, errorMessage), Snackbar.LENGTH_LONG)
            .setErrorStyle()
            .show()
    }
}