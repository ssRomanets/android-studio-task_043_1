package com.example.task_043_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val contacts: MutableList<ContactModel>):
    RecyclerView.Adapter<CustomAdapter.ContactsViewHolder>(){

    private var onContactPhoneClickListener: OnContactPhoneClickListener? = null

    interface OnContactPhoneClickListener {
        fun onContactPhoneClick(contactType: ContactModel, position: Int)
    }

    private var onContactSmsClickListener: OnContactSmsClickListener? = null

    interface OnContactSmsClickListener {
        fun onContactSmsClick(contactType: ContactModel, position: Int)
    }

    class ContactsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imagePhoneViewIV: ImageView = itemView.findViewById(R.id.imagePhoneViewIV)
        val imageSmsViewIV: ImageView = itemView.findViewById(R.id.imageSmsViewIV)
        val nameTV: TextView = itemView.findViewById(R.id.nameTV)
        val phoneTV: TextView = itemView.findViewById(R.id.phoneTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ContactsViewHolder(itemView)
    }

    override fun getItemCount() = contacts.size

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val contact = contacts[position]
        holder.imagePhoneViewIV.setImageResource(R.drawable.phone_android_ic)
        holder.imageSmsViewIV.setImageResource(R.drawable.sms_ic)
        holder.nameTV.text = contact.name
        holder.phoneTV.text = contact.phone

        holder.imagePhoneViewIV.setOnClickListener{
            if (onContactPhoneClickListener != null) {
                onContactPhoneClickListener!!.onContactPhoneClick(contact, position)
            }
        }

        holder.imageSmsViewIV.setOnClickListener{
            if (onContactSmsClickListener != null) {
                onContactSmsClickListener!!.onContactSmsClick(contact, position)
            }
        }
    }

    fun setOnContactPhoneClickListener(onContactPhoneClickListener: OnContactPhoneClickListener) {
        this.onContactPhoneClickListener = onContactPhoneClickListener
    }

    fun setOnContactSmsClickListener(onContactSmsClickListener: OnContactSmsClickListener) {
        this.onContactSmsClickListener = onContactSmsClickListener
    }
}

