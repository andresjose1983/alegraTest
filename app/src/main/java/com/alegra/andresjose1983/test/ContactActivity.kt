package com.alegra.andresjose1983.test

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.alegra.andresjose1983.test.model.Address
import com.alegra.andresjose1983.test.model.Contact
import com.alegra.andresjose1983.test.module.ViewModulesModule
import com.alegra.andresjose1983.test.service.DaggerAlegraActivityComponent
import com.alegra.andresjose1983.test.viewModel.ContactViewModel
import kotlinx.android.synthetic.main.activity_contact.*
import org.jetbrains.anko.AlertDialogBuilder
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import javax.inject.Inject


class ContactActivity : AppCompatActivity() {

    @Inject
    lateinit var contactViewModel: ContactViewModel

    private var mObsProgressBar: AlertDialogBuilder? = null

    lateinit var contact: Contact

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        DaggerAlegraActivityComponent
                .builder()
                .viewModulesModule(ViewModulesModule(this@ContactActivity))
                .build()
                .inject(this@ContactActivity)

        contactViewModel.notifications.observe(this, Observer {
            when (it!!) {
                1 -> {
                    mObsProgressBar = alert(message = getString(R.string.submitting),
                            title = getString(R.string.please_wait))
                    mObsProgressBar?.let {
                        it.cancellable(false)
                        it.show()
                    }
                }
                2 -> mObsProgressBar?.dismiss()
                3 -> {
                    toast(getString(R.string.contact_added))
                    finish()
                }
                4 -> toast(getString(R.string.invalid_email))
            }
        })

        save_fab.setOnClickListener {
            val address = Address(address_edit_text.text.toString(), city_edit_text.text.toString())
            val type = ArrayList<String>(1)
            type.add((type_spinner.selectedItem as String).toLowerCase())
            contact = Contact(null, name_edit_text.text.toString(),
                    identification_edit_text.text.toString(),
                    email_edit_text.text.toString(),
                    phone_edit_text.text.toString(),
                    other_phone_edit_text.text.toString(),
                    fax_edit_text.text.toString(),
                    device_phone_number_edit_text.text.toString(),
                    observation_edit_text.text.toString(),
                    type,
                    address)
            contact.let {
                contactViewModel.save(it)
            }
        }

        if (intent.hasExtra("contact")) {
            contact = this.intent.extras.get("contact") as Contact
            save_fab.visibility = View.GONE

            with(contact) {
                name_edit_text.setText(name)
                identification_edit_text.setText(identification)
                email_edit_text.setText(email)
                phone_edit_text.setText(phonePrimary)
                other_phone_edit_text.setText(phoneSecondary)
                fax_edit_text.setText(fax)
                device_phone_number_edit_text.setText(mobile)
                observation_edit_text.setText(observations)
                address?.let {
                    address_edit_text.setText(it.address)
                    city_edit_text.setText(it.city)
                }

                if (type?.isNotEmpty()!!) {
                    when (type!![0]) {
                        "client" -> type_spinner.setSelection(0)
                        else -> type_spinner.setSelection(1)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        contactViewModel.notifications.removeObservers(this)
        super.onDestroy()
    }
}
