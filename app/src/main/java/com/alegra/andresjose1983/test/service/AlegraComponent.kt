package com.alegra.andresjose1983.test.service

import android.app.Application
import com.alegra.andresjose1983.test.module.AppModule
import com.alegra.andresjose1983.test.module.NetModule
import com.alegra.andresjose1983.test.module.ViewModulesModule
import com.alegra.andresjose1983.test.viewModel.ContactViewModel
import dagger.Component
import test.andresjose1983.alegra.com.andresjose1983.viewModel.MainViewModel
import javax.inject.Singleton

/**
 * Created by andre on 1/16/2018.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, NetModule::class, ViewModulesModule::class))
interface AlegraComponent {

    fun inject(app: Application)

    fun inject(mainViewModel: MainViewModel)

    fun inject(contactViewModel: ContactViewModel)
}