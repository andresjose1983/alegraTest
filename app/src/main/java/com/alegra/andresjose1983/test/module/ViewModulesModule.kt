package com.alegra.andresjose1983.test.module

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import com.alegra.andresjose1983.test.viewModel.ContactViewModel
import dagger.Module
import dagger.Provides
import test.andresjose1983.alegra.com.andresjose1983.viewModel.MainViewModel

/**
 * Created by andre on 3/20/2018.
 */
@Module
class ViewModulesModule(val activity: AppCompatActivity) {

    @Provides
    fun provideMainActivityViewModel() = ViewModelProviders.of(activity).get(MainViewModel::class.java)

    @Provides
    fun provideContanctActivityViewModel() = ViewModelProviders.of(activity).get(ContactViewModel::class.java)
}