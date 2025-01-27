package com.example.practic.ui.accaunt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.practic.R
import android.widget.TextView

class AccauntFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_accaunt, container, false)

        val textView: TextView = view.findViewById(R.id.textView4)

        return view
    }
}