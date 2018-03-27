package com.alegra.andresjose1983.test.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.text.TextUtils
import com.alegra.andresjose1983.test.AlegraApplication
import com.alegra.andresjose1983.test.model.Contact
import com.alegra.andresjose1983.test.service.AlegraAPI
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by andre on 3/26/2018.
 */
class ContactViewModel : ViewModel() {

    var notifications: MutableLiveData<Int> = MutableLiveData()

    @Inject
    lateinit var api: AlegraAPI

    init {
        AlegraApplication.component.inject(this@ContactViewModel)
    }

    fun save(contact: Contact) {

        if (!contact.email.isNullOrEmpty()) {
            if (!contact.email?.isValidEmail()!!) {
                notifications.value = 4
                return
            }
        }
        notifications.value = 1

        doAsync {
            api.save(contact).enqueue(object : Callback<Any> {
                override fun onFailure(call: Call<Any>?, t: Throwable?) {
                    notifications.value = 2
                }

                override fun onResponse(call: Call<Any>?, response: Response<Any>?) {
                    notifications.value = 2
                    notifications.value = 3
                }
            })
        }
    }

    fun String.isValidEmail() = !TextUtils.isEmpty(this) &&
            android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

}