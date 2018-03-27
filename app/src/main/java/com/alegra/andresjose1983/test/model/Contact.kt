package com.alegra.andresjose1983.test.model

import java.io.Serializable

/**
 * Created by andre on 3/20/2018.
 */
class Contact(val id: Int?,
              var name: String,
              var identification: String,
              var email: String?,
              var phonePrimary: String?,
              var phoneSecondary: String?,
              var fax: String?,
              var mobile: String?,
              var observations: String?,
              var type: List<String>?,
              var address: Address?) : Serializable