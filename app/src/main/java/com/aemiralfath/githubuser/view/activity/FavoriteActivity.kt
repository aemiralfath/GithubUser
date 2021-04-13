package com.aemiralfath.githubuser.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.aemiralfath.githubuser.R
import com.aemiralfath.githubuser.databinding.ActivityAboutBinding
import com.aemiralfath.githubuser.model.db.FavoriteUserApplication
import com.aemiralfath.githubuser.model.db.entity.FavoriteUser
import com.aemiralfath.githubuser.model.network.response.UsersItemResponse
import com.aemiralfath.githubuser.model.network.response.UsersResponse
import com.aemiralfath.githubuser.view.adapter.UserAdapter
import com.aemiralfath.githubuser.viewmodel.FavoriteUserViewModel
import com.aemiralfath.githubuser.viewmodel.FavoriteUserViewModelFactory

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding
    private lateinit var adapter: UserAdapter

    private val favoriteUserViewModel: FavoriteUserViewModel by viewModels {
        FavoriteUserViewModelFactory((application as FavoriteUserApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.favorite)

        adapter = UserAdapter()

        favoriteUserViewModel.setUser()
        favoriteUserViewModel.getDataUsers().observe(this, getUser)

        binding.circleImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.dicoding.com/users/aemiralfath")
            startActivity(intent)
        }
    }

    private val getUser: Observer<List<FavoriteUser>> = Observer {
        Log.d("ListUser", it.size.toString())
        if (it.isEmpty()) {
            binding.rvUsersAbout.visibility = View.GONE
            binding.emptyView.visibility = View.VISIBLE
        } else {
            binding.rvUsersAbout.visibility = View.VISIBLE
            binding.emptyView.visibility = View.GONE
            showRecyclerList(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.about_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_link -> {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://www.linkedin.com/in/aemiralfath")
                startActivity(intent)
            }
            R.id.action_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.share_profile))
                intent.putExtra(Intent.EXTRA_TEXT, "https://github.com/aemiralfath/")
                startActivity(
                    Intent.createChooser(
                        intent,
                        resources.getString(R.string.share_profile)
                    )
                )
            }
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showRecyclerList(favoriteUser: List<FavoriteUser>) {
        val listData: ArrayList<UsersItemResponse> = arrayListOf()
        favoriteUser.forEach {
            listData.add(
                UsersItemResponse(
                    login = it.username,
                    htmlUrl = it.link,
                    avatarUrl = it.avatar
                )
            )
        }

        adapter.listUsers = UsersResponse(listData.size, false, listData)
        adapter.notifyDataSetChanged()
        binding.rvUsersAbout.setHasFixedSize(true)
        binding.rvUsersAbout.layoutManager = LinearLayoutManager(this)
        binding.rvUsersAbout.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UsersItemResponse?) {
                val intent = Intent(this@FavoriteActivity, DetailUserActivity::class.java)
                intent.putExtra(DetailUserActivity.EXTRA_USER, data?.login)
                startActivity(intent)
            }
        })
    }
}