package com.aemiralfath.githubuser.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aemiralfath.githubuser.R
import com.aemiralfath.githubuser.view.fragment.SettingsFragment

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()

        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}