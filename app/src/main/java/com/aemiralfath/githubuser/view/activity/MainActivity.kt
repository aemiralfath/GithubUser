package com.aemiralfath.githubuser.view.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aemiralfath.githubuser.R
import com.aemiralfath.githubuser.databinding.ActivityMainBinding
import com.aemiralfath.githubuser.model.entity.UsersItem
import com.aemiralfath.githubuser.model.entity.UsersResponse
import com.aemiralfath.githubuser.view.adapter.UserAdapter
import com.aemiralfath.githubuser.viewmodel.MainViewModel
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: UserAdapter

    private var onSearch = false

    companion object {
        private const val STATE_SEARCH = "state_search"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.app_name)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.adapter = adapter

        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        mainViewModel.getDataUser().observe(this, getUser)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        binding.svUser.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        binding.svUser.isActivated = true
        binding.svUser.queryHint = resources.getString(R.string.search_username)
        binding.svUser.onActionViewExpanded()
        binding.svUser.isIconified = false
        binding.svUser.clearFocus()

        binding.svUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                onSearch = true
                return if (query.isNullOrBlank()) {
                    false
                } else {
                    showLoading(true)
                    mainViewModel.searchUser(applicationContext, query)
                    mainViewModel.getDataUser().observe(this@MainActivity, getUser)
                    true
                }
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return if (newText.isNullOrBlank()) {
                    onSearch = false
                    showLoading(true)
                    mainViewModel.setUser(applicationContext)
                    mainViewModel.getDataUser().observe(this@MainActivity, getUser)
                    true
                } else {
                    false
                }
            }
        })

        binding.svUser.setOnCloseListener {
            onSearch = false
            showLoading(true)
            mainViewModel.setUser(applicationContext)
            mainViewModel.getDataUser().observe(this@MainActivity, getUser)
            true
        }

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UsersItem?) {
                val intent = Intent(this@MainActivity, DetailUserActivity::class.java)
                intent.putExtra(DetailUserActivity.EXTRA_USER, data?.login)
                startActivity(intent)
            }
        })

        if (savedInstanceState != null) {
            onSearch = savedInstanceState.getBoolean(STATE_SEARCH)
            if (!onSearch) {
                mainViewModel.setUser(applicationContext)
            }
        } else {
            mainViewModel.setUser(applicationContext)
        }

        showLoading(true)
    }

    private val getUser: Observer<UsersResponse> =
        Observer<UsersResponse> {
            adapter.listUsers = it ?: UsersResponse()
            adapter.notifyDataSetChanged()
            showLoading(false)
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setMode(selectedMode: Int) {
        when (selectedMode) {
            R.id.action_profile -> {
                val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(intent)
            }
            R.id.action_setting_language -> {
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.rvUser.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.rvUser.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(STATE_SEARCH, onSearch)
        super.onSaveInstanceState(outState)
    }
}