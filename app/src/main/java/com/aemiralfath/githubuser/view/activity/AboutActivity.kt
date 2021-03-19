package com.aemiralfath.githubuser.view.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.aemiralfath.githubuser.R
import com.aemiralfath.githubuser.databinding.ActivityAboutBinding
import com.aemiralfath.githubuser.model.db.FavoriteUser
import com.aemiralfath.githubuser.model.db.database
import com.aemiralfath.githubuser.model.entity.User
import com.aemiralfath.githubuser.view.adapter.UserAdapter
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding
    private lateinit var adapter: UserAdapter

    private var favoriteUser: MutableList<FavoriteUser> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "About"

        adapter = UserAdapter()
        showFavorite()

        binding.circleImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.dicoding.com/users/aemiralfath")
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        showFavorite()
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
                intent.putExtra(Intent.EXTRA_SUBJECT, "Sharing Profile")
                intent.putExtra(Intent.EXTRA_TEXT, "https://github.com/aemiralfath/")
                startActivity(Intent.createChooser(intent, "Share Profile"))
            }
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showFavorite() {
        favoriteUser.clear()

        this.database.use {
            val resultUser = select(FavoriteUser.TABLE_FAVORITE)
            val favUser = resultUser.parseList(classParser<FavoriteUser>())
            favoriteUser.addAll(favUser)
        }

        if (favoriteUser.isEmpty()) {
            binding.rvUsersAbout.visibility = View.GONE
            binding.emptyView.visibility = View.VISIBLE
        } else {
            binding.rvUsersAbout.visibility = View.VISIBLE
            binding.emptyView.visibility = View.GONE
            showRecyclerList()
        }
    }

    private fun showRecyclerList() {
        val listData: ArrayList<User> = arrayListOf()
        favoriteUser.forEach {
            listData.add(
                User(
                    username = it.username,
                    name = it.name,
                    company = it.company,
                    avatar = it.avatar,
                )
            )
        }

        adapter.listUsers = listData
        adapter.notifyDataSetChanged()
        binding.rvUsersAbout.setHasFixedSize(true)
        binding.rvUsersAbout.layoutManager = LinearLayoutManager(this)
        binding.rvUsersAbout.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intent = Intent(this@AboutActivity, DetailUserActivity::class.java)
                intent.putExtra(DetailUserActivity.EXTRA_USER, data)
                startActivity(intent)
            }
        })
    }
}