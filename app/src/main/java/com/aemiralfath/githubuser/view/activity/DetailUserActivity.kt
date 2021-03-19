package com.aemiralfath.githubuser.view.activity

import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.aemiralfath.githubuser.R
import com.aemiralfath.githubuser.databinding.ActivityDetailUserBinding
import com.aemiralfath.githubuser.model.UserData.getDataUsername
import com.aemiralfath.githubuser.model.db.FavoriteUser
import com.aemiralfath.githubuser.model.db.database
import com.aemiralfath.githubuser.model.entity.User
import com.bumptech.glide.Glide
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar

class DetailUserActivity : AppCompatActivity() {

    private var isFavorite: Boolean = false

    private lateinit var user: User
    private lateinit var menuItem: Menu
    private lateinit var binding: ActivityDetailUserBinding

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userExtra = intent.getParcelableExtra<User>(EXTRA_USER) as User
        user = userExtra.username?.let { getDataUsername(it) }!!

        supportActionBar?.title = user.username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val username = "@${user.username}"

        Glide.with(this).load(user.avatar).into(binding.imgUserAvatar)
        binding.tvUserName.text = user.name
        binding.tvUserUsername.text = username
        binding.tvUserCompany.text = user.company
        binding.tvUserLocation.text = user.location
        binding.tvUserRepositories.text = user.repository
        binding.tvUserFollowers.text = user.follower
        binding.tvUserFollowing.text = user.following
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
                intent.putExtra(Intent.EXTRA_TEXT, "https://github.com/${user.username}")
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
                    "username" to user.username!!
                )
            val favorite = result.parseList(classParser<FavoriteUser>())
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
                    FavoriteUser.USERNAME to user.username,
                    FavoriteUser.USER_NAME to user.name,
                    FavoriteUser.USER_COMPANY to user.company,
                    FavoriteUser.USER_AVATAR to user.avatar
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
                    "username" to user.username!!
                )
            }
            binding.root.snackbar(getString(R.string.remove_from_favorite))
        } catch (e: SQLiteConstraintException) {
            binding.root.snackbar(e.localizedMessage!!)
        }
    }
}