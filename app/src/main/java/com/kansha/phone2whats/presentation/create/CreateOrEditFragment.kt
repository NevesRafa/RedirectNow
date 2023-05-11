package com.kansha.phone2whats.presentation.create

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kansha.phone2whats.data.model.PhoneDetails
import com.kansha.phone2whats.databinding.FragmentCreateOrEditBinding
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
    }

    private fun setupObservers() {
        viewModel.loadStateLiveData.observe(this) { state ->
            when (state) {
                is CreateOrEditState.Loading -> {}
                is CreateOrEditState.Save -> saveOrEdit(state.phoneDetails)
                is CreateOrEditState.Edit -> showContactForEditOnTheScreen(state.phoneDetails)
                is CreateOrEditState.Error -> {}
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

            viewModel.checksSaveOrEdit(contactTyped, phoneNumberTyped)
            redirectToWhatsApp(phoneNumberTyped)
        }
    }

    private fun redirectToWhatsApp(phoneNumberTyped: String) {
        val url = "https://api.whatsapp.com/send?phone=$phoneNumberTyped"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

}