package com.ddroy.tapcounter.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
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
import com.google.android.material.appbar.MaterialToolbar

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding = FragmentSettingsBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

      /*  binding.click.setOnClickListener{
            (activity as? BaseActivity)?.savePreference(THEME_BLUE)


        }
        binding.click2.setOnClickListener{
            (activity as? BaseActivity)?.savePreference(THEME_PINK)


        }*/
        if (savedInstanceState == null) {
            childFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment2())
                .commit()
        }

        binding.rateAppBtn.setOnClickListener {
            rateApp()
        }

        val toolbar = view.findViewById<MaterialToolbar>(R.id.topAppBar)
        toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.countingFragment)
        }
    }


    private fun rateApp() {
        val packageName = activity?.packageName
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
        try {
            startActivity(intent)
        } catch (e: Exception) {
            // If Play Store app is not available, open the Play Store website
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
            startActivity(webIntent)
        }
    }

    class SettingsFragment2 : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            setupVibrationPreference()
            setupSoundPreference()
            setupVolumeButtonPreference()
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