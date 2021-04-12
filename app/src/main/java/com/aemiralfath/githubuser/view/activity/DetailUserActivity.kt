package com.aemiralfath.githubuser.view.activity

import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aemiralfath.githubuser.R
import com.aemiralfath.githubuser.databinding.ActivityDetailUserBinding
import com.aemiralfath.githubuser.model.db.FavoriteUser
import com.aemiralfath.githubuser.model.db.FavoriteUserApplication
import com.aemiralfath.githubuser.model.entity.DetailUserResponse
import com.aemiralfath.githubuser.view.adapter.SectionPagerAdapter
import com.aemiralfath.githubuser.viewmodel.DetailUserViewModel
import com.aemiralfath.githubuser.viewmodel.FavoriteUserViewModel
import com.aemiralfath.githubuser.viewmodel.FavoriteUserViewModelFactory
import com.bumptech.glide.Glide
import org.jetbrains.anko.design.snackbar

class DetailUserActivity : AppCompatActivity() {

    private lateinit var username: String
    private lateinit var menuItem: Menu
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailUsersResponse: DetailUserResponse
    private lateinit var detailUserViewModel: DetailUserViewModel

    private val favoriteUserViewModel: FavoriteUserViewModel by viewModels {
        FavoriteUserViewModelFactory((application as FavoriteUserApplication).repository)
    }

    private var isFavorite: Boolean = false

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userExtra = intent.getStringExtra(EXTRA_USER)
        username = userExtra.toString()

        supportActionBar?.title = username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        detailUserViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailUserViewModel::class.java)

        detailUserViewModel.setUser(this, username)
        detailUserViewModel.getDataUser().observe(this, getUser)

        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
        sectionPagerAdapter.onDataPass(username)
        binding.viewPager.adapter = sectionPagerAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)
        supportActionBar?.elevation = 0f

        showLoading(true)

    }

    private val getUser: Observer<DetailUserResponse> =
        Observer<DetailUserResponse> {
            val usernameText = "@${username}"

            Glide.with(this).load(it.avatarUrl).into(binding.imgUserAvatar)
            binding.tvUserUsername.text = usernameText
            binding.tvUserName.text =
                if (it.name.isNullOrEmpty()) resources.getString(R.string.not_set) else it.name
            binding.tvUserCompany.text =
                if (it.company.isNullOrEmpty()) resources.getString(R.string.not_set) else it.company
            binding.tvUserLocation.text =
                if (it.location.isNullOrEmpty()) resources.getString(R.string.not_set) else it.location
            binding.tvUserRepositories.text = it.publicRepos.toString()
            binding.tvUserFollowers.text = it.followers.toString()
            binding.tvUserFollowing.text = it.following.toString()

            detailUsersResponse = it

            favoriteUserViewModel.setUserByUsername(username)
            favoriteState()
            showLoading(false)
        }

    private fun favoriteState() {
        favoriteUserViewModel.getDataUser().observe(this, {
            isFavorite = it != null
            setFavorite()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_user_menu, menu)
        if (menu != null) menuItem = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite -> {
                if (isFavorite) removeFromFavorite() else addToFavorite()
                setFavorite()
                return true
            }

            R.id.action_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.share_user))
                intent.putExtra(Intent.EXTRA_TEXT, "https://github.com/${username}")
                startActivity(
                    Intent.createChooser(
                        intent,
                        resources.getString(R.string.share_user)
                    )
                )
                return true
            }

            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



    private fun setFavorite() {
        if (isFavorite) {
            menuItem.getItem(0)?.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_24)
        } else {
            menuItem.getItem(0)?.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_border_24)
        }
    }

    private fun addToFavorite() {
        try {
            favoriteUserViewModel.addToFavoriteUser(
                FavoriteUser(
                    username = detailUsersResponse.login.toString(),
                    link = detailUsersResponse.htmlUrl,
                    avatar = detailUsersResponse.avatarUrl
                )
            )
            isFavorite = true
            setFavorite()
            binding.root.snackbar(getString(R.string.add_to_favorite))
        } catch (e: SQLiteConstraintException) {
            binding.root.snackbar(e.localizedMessage!!).show()
        }
    }

    private fun removeFromFavorite() {
        try {

            favoriteUserViewModel.removeFromFavoriteUser(
                FavoriteUser(
                    username = detailUsersResponse.login.toString(),
                    link = detailUsersResponse.htmlUrl,
                    avatar = detailUsersResponse.avatarUrl
                )
            )
            isFavorite = false
            setFavorite()
            binding.root.snackbar(getString(R.string.remove_from_favorite))
        } catch (e: SQLiteConstraintException) {
            binding.root.snackbar(e.localizedMessage!!)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}