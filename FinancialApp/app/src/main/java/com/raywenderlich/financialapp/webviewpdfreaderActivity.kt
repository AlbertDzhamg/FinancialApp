package com.raywenderlich.financialapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import com.raywenderlich.financialapp.databinding.ActivityWebviewpdfreaderBinding

class webviewpdfreaderActivity : AppCompatActivity() {


    lateinit var binding: ActivityWebviewpdfreaderBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewpdfreaderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pdfViewerURL = "https://drive.google.com/viewerng/viewer?embedded=true&url="
        binding.webViewReadBooks.settings.javaScriptEnabled = true
        binding.webViewReadBooks.settings.setSupportZoom(true)

        val pdfURL = "https://www.orimi.com/pdf-test.pdf"
        val url = pdfViewerURL + pdfURL
        binding.webViewReadBooks.loadUrl(url)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { // обработка нажатия кнопки навигации
                onBackPressedDispatcher.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}