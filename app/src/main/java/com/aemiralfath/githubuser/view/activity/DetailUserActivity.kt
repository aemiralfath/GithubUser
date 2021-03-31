package com.aemiralfath.githubuser.view.activity

import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aemiralfath.githubuser.R
import com.aemiralfath.githubuser.databinding.ActivityDetailUserBinding
import com.aemiralfath.githubuser.model.db.FavoriteUser
import com.aemiralfath.githubuser.model.db.database
import com.aemiralfath.githubuser.model.entity.DetailUserResponse
import com.aemiralfath.githubuser.model.entity.UsersItem
import com.aemiralfath.githubuser.viewmodel.DetailUserViewModel
import com.bumptech.glide.Glide
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar

class DetailUserActivity : AppCompatActivity() {

    private lateinit var username: String
    private lateinit var menuItem: Menu
    private lateinit var detailUserViewModel: DetailUserViewModel
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailUsersResponse: DetailUserResponse

    private var isFavorite: Boolean = false

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userExtra = intent.getParcelableExtra<UsersItem>(EXTRA_USER) as UsersItem
        username = userExtra.login.toString()

        supportActionBar?.title = username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        detailUserViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailUserViewModel::class.java)

        detailUserViewModel.getDataUser().observe(this, getUser)
        detailUserViewModel.setUser(username)

        showLoading(true)

    }

    private val getUser: Observer<DetailUserResponse> =
        Observer<DetailUserResponse> {
            val usernameText = "@${username}"

            Glide.with(this).load(it.avatarUrl).into(binding.imgUserAvatar)
            binding.tvUserName.text = it.name
            binding.tvUserUsername.text = usernameText
            binding.tvUserCompany.text = it.company
            binding.tvUserLocation.text = it.location
            binding.tvUserRepositories.text = it.publicRepos.toString()
            binding.tvUserFollowers.text = it.followers.toString()
            binding.tvUserFollowing.text = it.following.toString()

            detailUsersResponse = it
            showLoading(false)
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
                intent.putExtra(Intent.EXTRA_SUBJECT, "Sharing User")
                intent.putExtra(Intent.EXTRA_TEXT, "https://github.com/${username}")
                startActivity(Intent.createChooser(intent, "Share User"))
                return true
            }

            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun favoriteState() {
        database.use {
            val result = select(FavoriteUser.TABLE_FAVORITE)
                .whereArgs(
                    "(USERNAME = {username})",
                    "username" to username
                )
            val favorite = result.parseList(classParser<FavoriteUser>())
            Log.d("Favorite", favorite.toString())
            isFavorite = favorite.isNotEmpty()
        }
    }

    private fun setFavorite() {
        favoriteState()
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
            database.use {
                insert(
                    FavoriteUser.TABLE_FAVORITE,
                    FavoriteUser.USERNAME to detailUsersResponse.login,
                    FavoriteUser.USER_LINK to detailUsersResponse.htmlUrl,
                    FavoriteUser.USER_AVATAR to detailUsersResponse.avatarUrl
                )
            }
            binding.root.snackbar(getString(R.string.add_to_favorite))
        } catch (e: SQLiteConstraintException) {
            binding.root.snackbar(e.localizedMessage!!).show()
        }
    }

    private fun removeFromFavorite() {
        try {
            database.use {
                delete(
                    FavoriteUser.TABLE_FAVORITE,
                    "(USERNAME = {username})",
                    "username" to username
                )
            }
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