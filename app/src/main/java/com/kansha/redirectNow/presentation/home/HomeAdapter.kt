package com.kansha.redirectNow.presentation.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kansha.redirectNow.data.model.PhoneDetails
import com.kansha.redirectNow.databinding.ItemPhoneBinding


class HomeAdapter(
    private val longClick: (PhoneDetails, View) -> Unit,
    private val redirectWhatsApp: (PhoneDetails) -> Unit,
    private val redirectCall: (PhoneDetails) -> Unit
) :
    RecyclerView.Adapter<HomeScreenViewHolder>() {

    private val contacts = mutableListOf<PhoneDetails>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeScreenViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPhoneBinding.inflate(inflater, parent, false)
        return HomeScreenViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeScreenViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bind(
            contact,
            longClick,
            redirectWhatsApp,
            redirectCall
        )
    }

    override fun getItemCount() = contacts.size

    fun update(phoneDetails: List<PhoneDetails>) {
        this.contacts.clear()
        this.contacts.addAll(phoneDetails)
        notifyDataSetChanged()
    }
}

class HomeScreenViewHolder(private val binding: ItemPhoneBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        contact: PhoneDetails,
        longClick: (PhoneDetails, View) -> Unit,
        redirectWhatsApp: (PhoneDetails) -> Unit,
        redirectCall: (PhoneDetails) -> Unit
    ) {

        binding.phoneNumber.text = contact.phoneNumber
        binding.contactName.text = contact.contact

        binding.root.setOnLongClickListener {
            longClick(contact, binding.root)
            return@setOnLongClickListener true
        }

        binding.redirectWhatsapp.setOnClickListener {
            redirectWhatsApp(contact)
        }

        binding.redirectCall.setOnClickListener {
            redirectCall(contact)
        }

        Glide.with(binding.root)
            .load("https://flagcdn.com/w320/${contact.flagCode}.png")
            .into(binding.ddiFlag)
    }
}