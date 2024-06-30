package com.example.quoteofthebay

import MainViewModel
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    lateinit var mainViewModel: MainViewModel

    private lateinit var quoteText: TextView
    private lateinit var quoteAuthor: TextView
    private lateinit var favoriteButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        quoteText = findViewById(R.id.quoteText)
        quoteAuthor = findViewById(R.id.quoteAuthor)
        favoriteButton = findViewById(R.id.favoriteButton)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mainViewModel = ViewModelProvider(this, MainViewModelFactory(application)).get(MainViewModel::class.java)

        setQuote(mainViewModel.getQuote())
        updateFavoriteButtonState()
    }

    private fun setQuote(quote: quote) {
        quoteText.text = quote.text
        quoteAuthor.text = quote.author
    }

    private fun updateFavoriteButtonState() {
        val isLiked = mainViewModel.getQuote().isLiked == 1
        val favoriteDrawable = if (isLiked) R.drawable.ic_favorite_filled else R.drawable.ic_favorite
        favoriteButton.setImageResource(favoriteDrawable)
    }

    fun onPrevious(view: View) {
        setQuote(mainViewModel.prevQuote())
        updateFavoriteButtonState()
    }

    fun onNext(view: View) {
        setQuote(mainViewModel.nextQuote())
        updateFavoriteButtonState()
    }

    fun onShare(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, mainViewModel.getQuote().text)
        startActivity(intent)
    }

    fun onFavorite(view: View) {
        mainViewModel.toggleLikedStatus(mainViewModel.getQuote().id)
        updateFavoriteButtonState()
    }

}
