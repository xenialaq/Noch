package xyz.mnch.noch


import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.EditTextPreference.OnBindEditTextListener
import androidx.preference.Preference
import androidx.preference.Preference.SummaryProvider
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager


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

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            findPreference<EditTextPreference?>("router_addr")?.
                setOnBindEditTextListener { editText -> editText.setSingleLine() }
            findPreference<EditTextPreference?>("access_point_address_range")?.
                setOnBindEditTextListener { editText -> editText.setSingleLine() }

            setPasswordStyle("router_password")
            setPasswordStyle("access_point_password")
        }

        private fun setPasswordStyle(key: String) {
            val pref: EditTextPreference = findPreference(key) ?: return
            pref.summaryProvider = SummaryProvider<Preference?> {
                val password: String = PreferenceManager.getDefaultSharedPreferences(
                    context
                ).getString(key, "")!!
                if (password == "") {
                    "Not set"
                } else {
                    "".padStart(password.length, '*')
                }
            }

            pref.setOnBindEditTextListener { editText ->
                editText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                pref.summaryProvider = SummaryProvider<Preference?> {
                    "".padStart(
                        editText.text.toString().length,
                        '*'
                    )
                }
            }
        }
    }
}