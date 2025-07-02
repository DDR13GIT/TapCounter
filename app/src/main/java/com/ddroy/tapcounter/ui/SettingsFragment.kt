package com.ddroy.tapcounter.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import androidx.preference.EditTextPreference
import android.text.InputFilter
import android.text.InputType
import android.widget.Toast
import com.ddroy.tapcounter.BaseActivity
import com.ddroy.tapcounter.R
import com.ddroy.tapcounter.constants.ThemeConstants.THEME_BLUE
import com.ddroy.tapcounter.constants.ThemeConstants.THEME_GREEN
import com.ddroy.tapcounter.constants.ThemeConstants.THEME_PINK
import com.ddroy.tapcounter.databinding.FragmentSettingsBinding
import com.ddroy.tapcounter.sharedPreference.PreferenceKeys
import com.ddroy.tapcounter.utils.ScreenManager
import com.google.android.material.appbar.MaterialToolbar
import androidx.core.net.toUri
import androidx.preference.Preference
import com.ddroy.tapcounter.BaseFragment
import com.ddroy.tapcounter.navigation.Navigation
import com.ddroy.tapcounter.utils.getThemColor

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private lateinit var binding: FragmentSettingsBinding

    override fun getHomeFragmentId(): Int {
        return R.id.settingsFragment
    }

    override fun getFragmentInstance(): Fragment {
        return this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding = FragmentSettingsBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            childFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SubSettingsFragment())
                .commit()
        }

        binding.rateAppBtn.setOnClickListener {
            rateApp()
        }

        val toolbar = view.findViewById<MaterialToolbar>(R.id.topAppBar)
        toolbar.setNavigationOnClickListener {
            Navigation.navigate(this, R.id.settingsFragment, R.id.countingFragment)
        }
    }


    private fun rateApp() {
        val packageName = activity?.packageName
        val intent = Intent(Intent.ACTION_VIEW, "market://details?id=$packageName".toUri())
        try {
            startActivity(intent)
        } catch (e: Exception) {
            // If Play Store app is not available, open the Play Store website
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                "https://play.google.com/store/apps/details?id=$packageName".toUri()
            )
            startActivity(webIntent)
        }
    }

    class SubSettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            updatePreferencesIcon()
            setupScreenOnPreference()
            setupThemePreference()
            setupLoopModePreference()
        }

        private fun setupVibrationPreference() {
            findPreference<SwitchPreferenceCompat>(PreferenceKeys.PREF_VIBRATION_ENABLED)?.setOnPreferenceChangeListener { _, newValue ->
                // Handle vibration preference change if needed
                true
            }
        }

        private fun setupSoundPreference() {
            findPreference<SwitchPreferenceCompat>(PreferenceKeys.PREF_SOUND_ENABLED)?.setOnPreferenceChangeListener { _, newValue ->
                // Handle sound preference change if needed
                true
            }
        }

        private fun setupVolumeButtonPreference() {
            findPreference<SwitchPreferenceCompat>(PreferenceKeys.PREF_VOLUME_ENABLED)?.setOnPreferenceChangeListener { _, newValue ->
                // Handle volume button preference change if needed
                true
            }
        }

        private fun setupScreenOnPreference() {
            findPreference<SwitchPreferenceCompat>(PreferenceKeys.PREF_SCREEN_ON)?.setOnPreferenceChangeListener { _, newValue ->
                // Handle screen on preference change if needed
                if (newValue == true) {
                    ScreenManager(activity).keepScreenOn(true)
                } else {
                    ScreenManager(activity).keepScreenOn(false)
                }

                true
            }
        }

        private fun setupThemePreference() {
            findPreference<ListPreference>(PreferenceKeys.PREF_THEME)?.setOnPreferenceChangeListener { _, newValue ->
                when (newValue) {
                    "Green" ->  (activity as? BaseActivity)?.savePreference(THEME_GREEN)
                    "Pink" ->  (activity as? BaseActivity)?.savePreference(THEME_PINK)
                    "Blue" ->  (activity as? BaseActivity)?.savePreference(THEME_BLUE)
                    else -> (activity as? BaseActivity)?.savePreference(THEME_PINK)
                }
                true
            }
        }

        private fun updatePreferencesIcon() {
            val iconColor = requireContext().getThemColor(R.attr.settingsIcon)
            val preferenceKeys = listOf(
                PreferenceKeys.PREF_VIBRATION_ENABLED,
                PreferenceKeys.PREF_SOUND_ENABLED,
                PreferenceKeys.PREF_VOLUME_ENABLED,
                PreferenceKeys.PREF_SCREEN_ON,
                PreferenceKeys.PREF_THEME,
                PreferenceKeys.PREF_LOOP_MODE_ENABLED,
                PreferenceKeys.PREF_LOOP_NUMBER,
                PreferenceKeys.VERSION_PREFERENCE
            )

            preferenceKeys.forEach { key ->
                findPreference<Preference>(key)?.let { preference ->
                    preference.icon = preference.icon?.mutate()?.apply {
                        setTint(iconColor)
                    }
                }
            }
        }


        private fun setupLoopModePreference() {
            findPreference<SwitchPreferenceCompat>(PreferenceKeys.PREF_LOOP_MODE_ENABLED)?.setOnPreferenceChangeListener { _, newValue ->
                true
            }

            findPreference<EditTextPreference>(PreferenceKeys.PREF_LOOP_NUMBER)?.apply {
                setOnBindEditTextListener { editText ->
                    editText.inputType = InputType.TYPE_CLASS_NUMBER
                    editText.filters = arrayOf(InputFilter.LengthFilter(3))
                }
                setOnPreferenceChangeListener { _, newValue ->
                    val number = (newValue as String).toIntOrNull()
                    if (number != null && number in 1..999) {
                        true
                    } else {
                        Toast.makeText(context, "Please enter a number between 1 and 999", Toast.LENGTH_SHORT).show()
                        false
                    }
                }
            }
        }
    }
}