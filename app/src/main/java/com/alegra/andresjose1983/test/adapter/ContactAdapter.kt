package com.alegra.andresjose1983.test.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alegra.andresjose1983.test.R
import com.alegra.andresjose1983.test.model.Contact
import com.alegra.andresjose1983.test.util.ItemTouchHelperViewHolder
import kotlinx.android.synthetic.main.item_contact.view.*
import test.andresjose1983.alegra.com.andresjose1983.viewModel.MainViewModel

/**
 * Created by andre on 3/21/2018.
 */
class ContactAdapter(val mainViewModel: MainViewModel) : RecyclerView.Adapter<ContactAdapter.ViewHolder>(), ItemTouchHelperViewHolder {

    var contacts: ArrayList<Contact> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.item_contact, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = contacts.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.let {
            contacts.let {
                holder.bind(it[position])
            }
        }
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(contact: Contact) {
            with(contact) {
                view.name_text_view.text = name
                view.identification_text_view.text = identification
            }

            view.setOnClickListener {
                mainViewModel.goToContactDetail(contacts[adapterPosition])
            }
        }
    }

    override fun onItemSelected() {

    }

    override fun onItemClear() {

    }

    override fun onItemDismiss(position: Int) {
        mainViewModel.deleteContact(contacts[position])
        contacts.removeAt(position)
        notifyItemRemoved(position)
    }
}