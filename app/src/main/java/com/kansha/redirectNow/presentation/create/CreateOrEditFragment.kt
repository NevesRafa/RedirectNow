package com.kansha.redirectNow.presentation.create

import android.content.Context
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.kansha.redirectNow.R
import com.kansha.redirectNow.data.model.PhoneDetails
import com.kansha.redirectNow.databinding.FragmentCreateOrEditBinding
import com.kansha.redirectNow.domain.countryList
import com.kansha.redirectNow.internal.extension.setErrorStyle
import org.koin.android.ext.android.inject

class CreateOrEditFragment(val clickOnSave: (PhoneDetails) -> Unit) : BottomSheetDialogFragment() {

    private var toast: Toast? = null
    private var currentMask: TextWatcher? = null

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

        viewModel.phoneMaskLiveData.observe(this) { newMask ->

            if (currentMask != null) {
                binding.phoneNumber.removeTextChangedListener(currentMask)
            }

            currentMask = newMask
            binding.phoneNumber.addTextChangedListener(currentMask)

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
        binding.ddi.setText(contactForEdit?.ddi)
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
            val flagCode = viewModel.getFlagCode(ddiTyped)

            if (contactTyped.isBlank() || phoneNumberTyped.isBlank() || ddiTyped.isBlank()) {
                Toast.makeText(
                    requireContext(),
                    "Por favor, preencha os campos.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                viewModel.checksSaveOrEdit(contactTyped, ddiTyped, phoneNumberTyped, flagCode)
                hideKeyboard()
            }
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun showDropDown() {
        val countryCodes = countryList.map { it.countryCode }
        val adapter = ArrayAdapter(requireContext(), R.layout.ddi_dropdown_item, countryCodes)

        binding.ddi.setAdapter(adapter)

        binding.ddi.doAfterTextChanged {
            viewModel.setDdi(it.toString())
        }

        binding.ddi.setOnItemClickListener { _, _, _, _ ->
            val typedText = binding.ddi.text.toString()
            viewModel.setDdi(typedText)
        }
    }


    private fun showToast(message: String) {
        toast?.cancel()

        if (message.isNotEmpty()) {
            toast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
            toast?.show()
        }
    }

    private fun showErrorMessage(errorMessage: String?) {
        Snackbar.make(
            binding.root,
            getString(R.string.error_message, errorMessage),
            Snackbar.LENGTH_LONG
        )
            .setErrorStyle()
            .show()
    }
}