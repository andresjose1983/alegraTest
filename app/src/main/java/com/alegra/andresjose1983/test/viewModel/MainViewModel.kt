package test.andresjose1983.alegra.com.andresjose1983.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.alegra.andresjose1983.test.AlegraApplication
import com.alegra.andresjose1983.test.model.Contact
import com.alegra.andresjose1983.test.service.AlegraAPI
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by andre on 3/20/2018.
 */
class MainViewModel : ViewModel() {

    var contacts: MutableLiveData<List<Contact>> = MutableLiveData()
    var contact: MutableLiveData<Contact> = MutableLiveData()
    var notifications: MutableLiveData<Int> = MutableLiveData()
    private var contactsSearch: ArrayList<Contact> = ArrayList()

    @Inject
    lateinit var api: AlegraAPI

    init {
        AlegraApplication.component.inject(this@MainViewModel)
    }

    fun getContacts() {
        notifications.value = 1
        doAsync {
            api.getContacts().enqueue(object : Callback<List<Contact>> {
                override fun onFailure(call: Call<List<Contact>>?, t: Throwable?) {

                }

                override fun onResponse(call: Call<List<Contact>>?, response: Response<List<Contact>>?) {
                    response?.let {
                        it.body()?.let {
                            contacts.value = it
                            contactsSearch = ArrayList(it)
                        }
                    }
                    notifications.value = 2
                }
            })
        }
    }

    fun deleteContact(contact: Contact) {
        doAsync {
            api.delete(contact.id!!).enqueue(object : Callback<Any> {
                override fun onFailure(call: Call<Any>?, t: Throwable?) {
                }

                override fun onResponse(call: Call<Any>?, response: Response<Any>?) {
                    notifications.value = 3
                }
            })
        }
    }

    fun search(search: String) {
        if (search.isNotEmpty())
            contacts.value = contactsSearch.filter {
                it.name.toUpperCase().contains(search) ||
                        it.identification.toUpperCase().contains(search)
            }
        else
            contacts.value = contactsSearch
    }

    fun goToContactDetail(contactP: Contact) {
        contact.value = contactP
    }
}