package com.kansha.phone2whats.presentation.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.kansha.phone2whats.R
import com.kansha.phone2whats.data.model.PhoneDetails
import com.kansha.phone2whats.databinding.ActivityHomeBinding
import com.kansha.phone2whats.presentation.create.CreateOrEditFragment
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
        adapter.update(result)
    }

    private fun setupPhoneList() {

        binding.recyclerPhones.layoutManager = LinearLayoutManager(this)

        adapter = HomeAdapter(
            longClick = { phone, itemClicked ->
                showPopupMenu(itemClicked, phone)
            })

        binding.recyclerPhones.adapter = adapter

    }

    private fun fabAddPhone() {
        binding.fabAddContact.setOnClickListener {

            val addContact = CreateOrEditFragment(clickOnSave = { phone ->
                viewModel.savePhone(phone)
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
}