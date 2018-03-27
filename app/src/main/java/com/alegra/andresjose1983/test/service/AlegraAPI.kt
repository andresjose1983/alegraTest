package com.alegra.andresjose1983.test.service

import com.alegra.andresjose1983.test.model.Contact
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by andre on 3/20/2018.
 */
interface AlegraAPI {


    @GET("contacts/")
    fun getContacts(): Call<List<Contact>>

    @DELETE("contacts/{contactId}")
    fun delete(@Path("contactId") contactId: Int): Call<Any>

    @POST("contacts")
    fun save(@Body contact: Contact): Call<Any>
}