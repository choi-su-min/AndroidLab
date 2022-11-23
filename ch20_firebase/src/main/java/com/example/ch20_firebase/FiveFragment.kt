package com.example.ch20_firebase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ch20_firebase.databinding.FragmentFiveBinding

class FiveFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val binding: FragmentFiveBinding by lazy { FragmentFiveBinding.inflate(layoutInflater) }

        var keylist = listOf(
            "꿈미래개척 상담",
            "영어인증제",
            "개척인증제"
        )

        binding.textView01.text = keylist[0]
        binding.textView02.text = keylist[1]
        binding.textView03.text = keylist[2]

        binding.layout01.setOnClickListener {
            if (binding.infrom1.visibility == View.VISIBLE) {
                binding.infrom1.visibility = View.GONE
                binding.layoutBtn01.animate().apply {
                    duration = 200
                    rotation(0f)
                }
            } else {
                binding.infrom1.visibility = View.VISIBLE
                binding.layoutBtn01.animate().apply {
                    duration = 200
                    rotation(180f)
                }
            }
        }

        binding.layout02.setOnClickListener {
            if (binding.infrom2.visibility == View.VISIBLE) {
                binding.infrom2.visibility = View.GONE
                binding.layoutBtn02.animate().apply {
                    duration = 200
                    rotation(0f)
                }
            } else {
                binding.infrom2.visibility = View.VISIBLE
                binding.layoutBtn02.animate().apply {
                    duration = 200
                    rotation(180f)
                }
            }
        }

        binding.layout03.setOnClickListener {
            if (binding.infrom3.visibility == View.VISIBLE) {
                binding.infrom3.visibility = View.GONE
                binding.layoutBtn03.animate().apply {
                    duration = 200
                    rotation(0f)
                }
            } else {
                binding.infrom3.visibility = View.VISIBLE
                binding.layoutBtn03.animate().apply {
                    duration = 200
                    rotation(180f)
                }
            }
        }

        return binding.root
    }

}