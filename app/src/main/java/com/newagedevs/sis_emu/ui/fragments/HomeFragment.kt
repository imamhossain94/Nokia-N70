package com.newagedevs.sis_emu.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.newagedevs.sis_emu.R
import com.newagedevs.sis_emu.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var networkIcon:ImageView
    private lateinit var toast: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        networkIcon = view.findViewById(R.id.networkIcon)
        toast = view.findViewById(R.id.toast)

        viewModel.toast.observe(viewLifecycleOwner) {
            view.post {
                try {
                    toast.text = it
                } catch (_:Exception) {}
            }
        }


    }



}