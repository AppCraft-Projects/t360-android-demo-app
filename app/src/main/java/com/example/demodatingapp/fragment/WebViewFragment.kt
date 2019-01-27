package com.example.demodatingapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.demodatingapp.databinding.FragmentWebviewBinding

class WebViewFragment: Fragment() {

    lateinit var url: String

    lateinit var binding: com.example.demodatingapp.databinding.FragmentWebviewBinding

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        url = WebViewFragmentArgs.fromBundle(arguments!!).tosUrl
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWebviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.webview.loadUrl(url)
    }
}