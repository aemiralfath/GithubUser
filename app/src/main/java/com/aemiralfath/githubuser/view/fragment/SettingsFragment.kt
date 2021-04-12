package com.aemiralfath.githubuser.view.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.aemiralfath.githubuser.R
import com.aemiralfath.githubuser.service.ReminderReceiver

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {
        private lateinit var REMINDER: String
        private lateinit var LANGUAGE: String
    }

    private lateinit var changeLanguage: Preference
    private lateinit var reminderReceiver: ReminderReceiver
    private lateinit var isReminderPreference: SwitchPreferenceCompat

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        init()
        setSummaries()
    }

    private fun init() {
        REMINDER = resources.getString(R.string.switch_reminder)
        LANGUAGE = resources.getString(R.string.list_language)
        reminderReceiver = ReminderReceiver()
        changeLanguage = findPreference<Preference>(LANGUAGE) as Preference
        isReminderPreference =
            findPreference<SwitchPreferenceCompat>(REMINDER) as SwitchPreferenceCompat
    }

    private fun setSummaries() {
        val sh = preferenceManager.sharedPreferences
        isReminderPreference.isChecked =
            sh.getBoolean(REMINDER, false) && reminderReceiver.isReminderSet(requireContext())

        isReminderPreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            if (isReminderPreference.isChecked) {
                reminderReceiver.setDailyReminder(
                    requireContext(),
                    ReminderReceiver.TYPE_DAILY,
                    "17:52",
                    "Let's find popular user on Github"
                )
            } else {
                reminderReceiver.cancelAlarm(requireContext())
            }
            true
        }

        changeLanguage.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            true
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == REMINDER) {
            isReminderPreference.isChecked = sharedPreferences.getBoolean(REMINDER, false)
        }
    }

}