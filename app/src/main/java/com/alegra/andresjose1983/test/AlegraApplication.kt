package com.alegra.andresjose1983.test

import android.app.Application
import com.alegra.andresjose1983.test.module.AppModule
import com.alegra.andresjose1983.test.module.NetModule
import com.alegra.andresjose1983.test.service.AlegraComponent
import com.alegra.andresjose1983.test.service.DaggerAlegraComponent

/**
 * Created by andre on 3/20/2018.
 */
class AlegraApplication : Application() {

    companion object {
        lateinit var component: AlegraComponent
    }

    override fun onCreate() {
        super.onCreate()

        component = DaggerAlegraComponent
                .builder()
                .appModule(AppModule(this))
                .netModule(NetModule(BuildConfig.URL))
                .build()

        component.inject(this)
    }

}