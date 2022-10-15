package com.example.ch17_database

import android.os.Bundle
import android.text.TextUtils
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class MySettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        //여기에 우리가 만든 설정 xml파일을 알려주면됨
        setPreferencesFromResource(R.xml.settings, rootKey)

        val idPreference: EditTextPreference? = findPreference("id")
        val colorPreference: ListPreference? = findPreference("color")

        //유저가 지정한 색을 화면에 나오게끔 설정하는역할 = summary
        colorPreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
        idPreference?.summaryProvider =
            Preference.SummaryProvider<EditTextPreference>{preference ->
                val text = preference.text
                if (TextUtils.isEmpty(text)){
                    "설정이 되지 않았습니다."
                }else {
                    "설정된 id 값은 $text 입니다."
                }
            }

    }
}