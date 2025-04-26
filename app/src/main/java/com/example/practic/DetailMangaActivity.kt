package com.example.practic

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.practic.data.Manga

class DetailMangaActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var mangaTitleTop: TextView
    private lateinit var mangaTitleBottom: TextView
    private lateinit var views: TextView
    private lateinit var chaptersInfo: TextView
    private lateinit var rating: TextView
    private lateinit var description: TextView
    private lateinit var backButton: ImageButton
    private lateinit var favourites_btn: View
    private lateinit var read_button: View
    private lateinit var modalDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manga_info)

        modalDialog = Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            val view = LayoutInflater.from(context).inflate(R.layout.modal_window, null)
            setContentView(view)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            setCancelable(true)
        }

        imageView = findViewById(R.id.imageView)
        mangaTitleTop = findViewById(R.id.manga_title)
        mangaTitleBottom = findViewById(R.id.textView7)
        views = findViewById(R.id.views)
        chaptersInfo = findViewById(R.id.chapters_info)
        rating = findViewById(R.id.rating)
        description = findViewById(R.id.description)
        backButton = findViewById(R.id.back_button)

        favourites_btn = findViewById(R.id.favourites_btn)
        read_button = findViewById(R.id.read_button)

        backButton.setOnClickListener{
            onBackPressed()
        }

        favourites_btn.setOnClickListener {
            showModalDialog()
        }

        read_button.setOnClickListener {
            showModalDialog()
        }
    }
    private fun showModalDialog() {
        if (!modalDialog.isShowing) {
            modalDialog.show()

            val modalOverlay = modalDialog.window?.decorView
            modalOverlay?.setOnClickListener {
                if (modalDialog.isShowing) {
                    modalDialog.dismiss()
                }
            }
        }
    }
}
