package com.alegra.andresjose1983.test.service

import com.alegra.andresjose1983.test.ContactActivity
import com.alegra.andresjose1983.test.MainActivity
import com.alegra.andresjose1983.test.module.ViewModulesModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by andre on 1/16/2018.
 */
@Singleton
@Component(modules = arrayOf(ViewModulesModule::class))
interface AlegraActivityComponent {

    fun inject(mainActivity: MainActivity)

    fun inject(contactActivity: ContactActivity)
}