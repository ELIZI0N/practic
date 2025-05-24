package com.example.practic

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.practic.data.Manga
import com.example.practic.data.MangaType
import com.example.practic.data_base.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailMangaActivity : AppCompatActivity() {

    private lateinit var repository: Repository

    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manga_info)

        repository = Repository(this)

        // Получаем объект Manga из Intent
        val manga = intent.getParcelableExtra<Manga>("manga")

        // Проверяем, что объект Manga не null
        if (manga != null) {
            // Находим View элементы в layout
            val mangaImage: ImageView = findViewById(R.id.image_info)
            val mangaTitle: TextView = findViewById(R.id.manga_title)
            val views: TextView = findViewById(R.id.views)
            val chaptersInfo: TextView = findViewById(R.id.chapters_info)
            val rating: TextView = findViewById(R.id.rating)
            val description: TextView = findViewById(R.id.description)
            val typeTextView: TextView = findViewById(R.id.manga_type)
            val mangaName: TextView = findViewById(R.id.textView7)

            // Заполняем View данными из объекта Manga
            Glide.with(this)
                .load(manga.images)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .centerCrop()
                .into(mangaImage)

            mangaTitle.text = manga.name
            views.text = "Просмотров: ${manga.views ?: "N/A"}"
            chaptersInfo.text = "Главы: ${manga.chapters ?: "N/A"}"
            rating.text = manga.score?.toString() ?: "N/A"
            description.text = manga.synopsis
            mangaName.text = manga.name
            typeTextView.text = manga.typeId.toString()

        } else {
            // Обрабатываем случай, когда объект Manga не был передан
            // Например, можно вывести сообщение об ошибке или закрыть Activity
            finish()
        }

        backButton = findViewById(R.id.back_button)

        backButton.setOnClickListener{
            onBackPressed()
        }
    }
}