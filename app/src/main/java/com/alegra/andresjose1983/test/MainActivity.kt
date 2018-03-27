package com.alegra.andresjose1983.test

import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.View
import com.alegra.andresjose1983.test.adapter.ContactAdapter
import com.alegra.andresjose1983.test.module.ViewModulesModule
import com.alegra.andresjose1983.test.service.DaggerAlegraActivityComponent
import com.alegra.andresjose1983.test.util.SimpleItemTouchHelperCallback
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import test.andresjose1983.alegra.com.andresjose1983.viewModel.MainViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    @Inject
    lateinit var mainViewModel: MainViewModel
    lateinit var contactAdapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DaggerAlegraActivityComponent
                .builder()
                .viewModulesModule(ViewModulesModule(this@MainActivity))
                .build()
                .inject(this@MainActivity)

        contactAdapter = ContactAdapter(mainViewModel)

        mainViewModel.contacts.observe(this, Observer {
            it?.let {
                with(contactAdapter) {
                    contacts.clear()
                    contacts.addAll(it)
                    notifyDataSetChanged()
                }
            }
        })
        mainViewModel.notifications.observe(this, Observer {
            it?.let {
                when (it) {
                    1 -> contacts_swipe_refresh_layout.isRefreshing = true
                    2 -> contacts_swipe_refresh_layout.isRefreshing = false
                    3 -> toast(getString(R.string.contact_deleted))
                }
            }
        })

        mainViewModel.contact.observe(this, Observer {
            it?.let {
                startActivity<ContactActivity>("contact" to it)
            }
        })

        contacts_swipe_refresh_layout.setOnRefreshListener {
            mainViewModel.getContacts()
        }

        contacts_recycler_view.adapter = contactAdapter
        contacts_recycler_view.setHasFixedSize(true)

        contacts_recycler_view.setItemViewCacheSize(5000)
        contacts_recycler_view.isDrawingCacheEnabled = true
        contacts_recycler_view.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        var layoutManager = LinearLayoutManager(this)
        contacts_recycler_view.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(contacts_recycler_view.context,
                layoutManager.orientation)
        contacts_recycler_view.addItemDecoration(dividerItemDecoration)

        val itemTouchHelper = ItemTouchHelper(SimpleItemTouchHelperCallback(this, contactAdapter))
        itemTouchHelper.attachToRecyclerView(contacts_recycler_view)

        add_contact_fab.setOnClickListener {

            startActivity<ContactActivity>()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchView = menu?.findItem(R.id.action_filter_search)?.actionView
                as android.support.v7.widget.SearchView
        searchView.setOnQueryTextListener(this)
        searchView.setBackgroundResource(R.color.colorPrimary)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        return super.onCreateOptionsMenu(menu)
    }

    override fun onStart() {
        mainViewModel.getContacts()
        super.onStart()
    }

    override fun onDestroy() {
        mainViewModel.contacts.removeObservers(this)
        mainViewModel.notifications.removeObservers(this)
        mainViewModel.contact.removeObservers(this)
        super.onDestroy()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {

        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        val value = newText?.toUpperCase()
        value?.let {
            contactAdapter.contacts.clear()
            contactAdapter.notifyDataSetChanged()
            mainViewModel.search(it.toUpperCase())
        }
        return false
    }

}
