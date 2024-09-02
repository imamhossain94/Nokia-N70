package com.newagedevs.sis_emu.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.newagedevs.sis_emu.R

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var networkIcon:ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        networkIcon = view.findViewById(R.id.networkIcon)
    }




}