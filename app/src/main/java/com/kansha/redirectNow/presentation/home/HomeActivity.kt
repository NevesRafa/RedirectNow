package com.kansha.redirectNow.presentation.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kansha.redirectNow.R

import com.kansha.redirectNow.data.model.PhoneDetails
import com.kansha.redirectNow.databinding.ActivityHomeBinding
import com.kansha.redirectNow.databinding.AlertDialogBinding

import com.kansha.redirectNow.internal.extension.gone
import com.kansha.redirectNow.presentation.create.CreateOrEditFragment
import org.koin.android.ext.android.inject

class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by inject()
    private lateinit var adapter: HomeAdapter
    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPhoneList()
        setupObservers()
        fabAddPhone()
        viewModel.loadList()

    }

    private fun setupObservers() {
        viewModel.loadStateLiveData.observe(this) { state ->
            when (state) {
                is HomeState.Loading -> {}
                is HomeState.Success -> showPhoneList(state.result)
                is HomeState.EditOrRemove -> showPopupMenu(state.itemClick, state.phoneDetails)
                is HomeState.OpenModalEdit -> showModalToEdit(state.phoneDetails)
                is HomeState.Error -> {}
            }
        }
    }

    private fun showPhoneList(result: List<PhoneDetails>) {
        if (result.isEmpty()) {
            binding.textEmptyList.visibility
        } else {
            binding.textEmptyList.gone()
        }
        adapter.update(result)
    }

    private fun setupPhoneList() {

        binding.recyclerPhones.layoutManager = LinearLayoutManager(this)

        adapter = HomeAdapter(
            shortClick = { phone ->
                showDialog(phone.phoneNumber, getString(R.string.message_from_short_click))
            },
            longClick = { phone, itemClicked ->
                showPopupMenu(itemClicked, phone)
            })

        binding.recyclerPhones.adapter = adapter

    }

    private fun fabAddPhone() {
        binding.fabAddPhone.setOnClickListener {

            val addContact = CreateOrEditFragment(clickOnSave = { phone ->
                viewModel.savePhone(phone)
                showDialog(phone.phoneNumber, getString(R.string.message_from_save))
            })
            addContact.show(supportFragmentManager, null)
        }
    }

    private fun showPopupMenu(itemClicked: View, phone: PhoneDetails) {
        val popup = PopupMenu(this, itemClicked)
        popup.menuInflater.inflate(R.menu.menu_edit_remove, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            viewModel.clickMenu(menuItem, phone)
            return@setOnMenuItemClickListener true
        }
        popup.show()
    }

    private fun showModalToEdit(phoneForEdit: PhoneDetails) {

        val intentData = Bundle()
        intentData.putParcelable(CreateOrEditFragment.EXTRA_PHONE_EDIT, phoneForEdit)

        val fragment = CreateOrEditFragment(clickOnSave = { workdayEdited ->
            viewModel.phoneEdit(workdayEdited)
        })
        fragment.arguments = intentData
        fragment.show(supportFragmentManager, null)
    }

    private fun showDialog(phone: String, message: String) {
        val customView = AlertDialogBinding.inflate(LayoutInflater.from(this))

        val dialog = MaterialAlertDialogBuilder(this)
            .setView(customView.root)
            .setCancelable(false)
            .show()

        customView.alertDialogTitle.text = getString(R.string.redirecionar_para_o_whatsapp)
        customView.alertDialogMessage.text = message

        customView.alertDialogBtnAccept.setOnClickListener {
            redirectToWhatsApp(phone)
            dialog.dismiss()
        }
        customView.alertDialogBtnDecline.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun redirectToWhatsApp(phoneNumberTyped: String) {
        val url = "https://api.whatsapp.com/send?phone=$phoneNumberTyped"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}