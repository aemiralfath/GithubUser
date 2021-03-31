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

    private val title = "Github User"
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: UserAdapter

    private var onSearch = false

    companion object{
        private const val STATE_SEARCH = "state_search"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = title

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
        binding.svUser.queryHint = "Search Username"
        binding.svUser.onActionViewExpanded()
        binding.svUser.isIconified = false
        binding.svUser.clearFocus()

        binding.svUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                onSearch = true
                return if (query.isNullOrBlank()) {
                    false
                } else {
                    mainViewModel.searchUser(query)
                    true
                }
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return if (newText.isNullOrBlank()) {
                    onSearch = false
                    mainViewModel.setUser()
                    true
                } else {
                    false
                }
            }
        })

        binding.svUser.setOnCloseListener {
            onSearch = true
            mainViewModel.setUser()
            true
        }

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UsersItem?) {
                val intent = Intent(this@MainActivity, DetailUserActivity::class.java)
                intent.putExtra(DetailUserActivity.EXTRA_USER, data)
                startActivity(intent)
            }
        })

        showLoading(true)

        if (savedInstanceState != null){
            onSearch = savedInstanceState.getBoolean(STATE_SEARCH)
            if (!onSearch) {
                mainViewModel.setUser()
            }
        }else{
            mainViewModel.setUser()
        }
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
                val intent = Intent(this@MainActivity, AboutActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(STATE_SEARCH, onSearch)
        super.onSaveInstanceState(outState)
    }
}