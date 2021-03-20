package com.aemiralfath.githubuser.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aemiralfath.githubuser.R
import com.aemiralfath.githubuser.databinding.ActivityMainBinding
import com.aemiralfath.githubuser.model.entity.User
import com.aemiralfath.githubuser.model.UserData
import com.aemiralfath.githubuser.view.adapter.UserAdapter
import com.aemiralfath.githubuser.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private val title = "Github User"
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = title

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        prepare()

        val mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel.setUser()
        mainViewModel.getDataUser().observe(this, {
            adapter.listUsers = it
        })

        binding.svUser.isActivated = true
        binding.svUser.queryHint = "Search User"
        binding.svUser.onActionViewExpanded()
        binding.svUser.isIconified = false
        binding.svUser.clearFocus()

        binding.svUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mainViewModel.setUser(newText)
                mainViewModel.getDataUser().observe(this@MainActivity, {
                    adapter.listUsers = it
                    adapter.notifyDataSetChanged()
                })
                return true
            }
        })

        binding.svUser.setOnCloseListener {
            mainViewModel.setUser()
            mainViewModel.getDataUser().observe(this@MainActivity, {
                adapter.listUsers = it
                adapter.notifyDataSetChanged()
            })
            true
        }

        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intent = Intent(this@MainActivity, DetailUserActivity::class.java)
                intent.putExtra(DetailUserActivity.EXTRA_USER, data)
                startActivity(intent)
            }
        })
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

    private fun prepare() {
        val dataUsername = resources.getStringArray(R.array.username)
        val dataName = resources.getStringArray(R.array.name)
        val dataLocation = resources.getStringArray(R.array.location)
        val dataCompany = resources.getStringArray(R.array.company)
        val dataRepository = resources.getStringArray(R.array.repository)
        val dataFollowers = resources.getStringArray(R.array.followers)
        val dataFollowing = resources.getStringArray(R.array.following)
        val dataAvatar = resources.obtainTypedArray(R.array.avatar)

        UserData.setData(
            dataUsername,
            dataName,
            dataLocation,
            dataCompany,
            dataRepository,
            dataFollowers,
            dataFollowing,
            dataAvatar
        )

        dataAvatar.recycle()
    }
}