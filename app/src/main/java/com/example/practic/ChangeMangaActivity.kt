package com.example.practic

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.practic.data.Manga
import com.example.practic.data_base.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ChangeMangaActivity : AppCompatActivity() {

    private lateinit var repository: Repository
    private var selectedImageUri: Uri? = null
    private var selectedImagePath: String? = null
    private var currentManga: Manga? = null

    private lateinit var imagePreview: ImageView
    private lateinit var btnSelectImage: Button
    private lateinit var etTitle: EditText
    private lateinit var spinnerType: Spinner
    private lateinit var etAuthor: EditText
    private lateinit var etReleaseYear: EditText
    private lateinit var etChapters: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnUpdateManga: Button
    private lateinit var btnDeleteManga: Button
    private lateinit var backButton: ImageButton

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedImageUri = uri
                Glide.with(this)
                    .load(uri)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imagePreview)

                selectedImagePath = copyImageToAppStorage(uri)
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openGallery()
        } else {
            Toast.makeText(
                this,
                "Разрешение необходимо для выбора изображения",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manga_change)

        repository = Repository(this)
        initViews()
        setupTypeDropdown()
        setupClickListeners()
        loadMangaData()
    }

    private fun initViews() {
        imagePreview = findViewById(R.id.imagePreview)
        btnSelectImage = findViewById(R.id.btnSelectImage)
        etTitle = findViewById(R.id.etTitle)
        spinnerType = findViewById(R.id.spinnerType)
        etAuthor = findViewById(R.id.etAuthor)
        etReleaseYear = findViewById(R.id.etReleaseYear)
        etChapters = findViewById(R.id.etChapters)
        etDescription = findViewById(R.id.etDescription)
        btnUpdateManga = findViewById(R.id.btnUpdateManga)
        btnDeleteManga = findViewById(R.id.btnDeleteManga)
        backButton = findViewById(R.id.back_button)

        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupTypeDropdown() {
        val types = arrayOf("Манга", "Маньхуа", "Манхва")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, types)
        spinnerType.adapter = adapter
    }

    private fun setupClickListeners() {
        btnSelectImage.setOnClickListener {
            checkPermissionAndOpenGallery()
        }

        btnUpdateManga.setOnClickListener {
            updateMangaInDatabase()
        }

        btnDeleteManga.setOnClickListener {
            deleteMangaFromDatabase()
        }
    }

    private fun loadMangaData() {
        val mangaId = intent.getIntExtra("MANGA_ID", -1)

        if (mangaId == -1) {
            Toast.makeText(this, "Ошибка: ID манги не получен", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val manga = repository.getMangaById(mangaId)
                withContext(Dispatchers.Main) {
                    if (manga != null) {
                        currentManga = manga
                        populateForm(manga)
                    } else {
                        Toast.makeText(this@ChangeMangaActivity, "Манга не найдена", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ChangeMangaActivity, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun populateForm(manga: Manga) {
        etTitle.setText(manga.name)
        etAuthor.setText(manga.authors)
        etReleaseYear.setText(manga.release.toString())
        etChapters.setText(manga.chapters?.toString() ?: "0")
        etDescription.setText(manga.synopsis)

        val types = arrayOf("Манга", "Маньхуа", "Манхва")
        val position = types.indexOf(manga.type)
        if (position != -1) {
            spinnerType.setSelection(position)
        }

        if (manga.images.isNotEmpty()) {
            try {
                Glide.with(this)
                    .load(manga.images)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imagePreview)
                selectedImagePath = manga.images
            } catch (e: Exception) {
                imagePreview.setImageResource(R.drawable.ic_launcher_foreground)
            }
        }
    }

    private fun checkPermissionAndOpenGallery() {
        val permission = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                Manifest.permission.READ_MEDIA_IMAGES
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                null
            }
            else -> {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
        }

        if (permission != null && ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(permission)
        } else {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
    }

    private fun copyImageToAppStorage(uri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageFileName = "MANGA_$timeStamp.jpg"
            val file = File(filesDir, imageFileName)

            val outputStream = FileOutputStream(file)
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun updateMangaInDatabase() {
        val title = etTitle.text.toString().trim()
        val type = spinnerType.selectedItem?.toString()?.trim() ?: ""
        val author = etAuthor.text.toString().trim()
        val releaseYearText = etReleaseYear.text.toString().trim()
        val chaptersText = etChapters.text.toString().trim()
        val description = etDescription.text.toString().trim()

        if (title.isEmpty() || type.isEmpty() || author.isEmpty() ||
            releaseYearText.isEmpty() || chaptersText.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        // Проверка длины названия (не более 250 символов)
        if (title.length > 250) {
            Toast.makeText(this, "Название не должно превышать 250 символов", Toast.LENGTH_SHORT).show()
            etTitle.error = "Максимум 250 символов"
            return
        }

        // Проверка длины авторов (не более 100 символов)
        if (author.length > 100) {
            Toast.makeText(this, "Имена авторов не должны превышать 100 символов", Toast.LENGTH_SHORT).show()
            etAuthor.error = "Максимум 100 символов"
            return
        }

        // Проверка длины описания (не более 2000 символов)
        if (description.length > 2000) {
            Toast.makeText(this, "Описание не должно превышать 2000 символов", Toast.LENGTH_SHORT).show()
            etDescription.error = "Максимум 2000 символов"
            return
        }

        val releaseYear = releaseYearText.toIntOrNull()
        val chapters = chaptersText.toIntOrNull()

        if (releaseYear == null) {
            Toast.makeText(this, "Год релиза должен быть числом", Toast.LENGTH_SHORT).show()
            etReleaseYear.error = "Введите корректный год"
            return
        }

        if (releaseYear < 1770 || releaseYear > 2025) {
            Toast.makeText(this, "Год релиза должен быть между 1770 и 2025", Toast.LENGTH_SHORT).show()
            etReleaseYear.error = "Год должен быть между 1770 и 2025"
            return
        }

        if (chapters == null || chapters <= 0) {
            Toast.makeText(this, "Количество глав должно быть положительным числом", Toast.LENGTH_SHORT).show()
            etChapters.error = "Введите положительное число"
            return
        }

        currentManga?.let { manga ->
            val updatedManga = manga.copy(
                name = title,
                images = selectedImagePath ?: manga.images,
                authors = author,
                release = releaseYear,
                chapters = chapters,
                synopsis = description,
                type = type
            )

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val result = repository.updateManga(updatedManga)
                    withContext(Dispatchers.Main) {
                        if (result > 0) {
                            Toast.makeText(this@ChangeMangaActivity, "Манга успешно обновлена!", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@ChangeMangaActivity, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                putExtra("SELECT_TAB", "home")
                            }
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@ChangeMangaActivity, "Ошибка при обновлении манги", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ChangeMangaActivity, "Ошибка при обновлении манги: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } ?: run {
            Toast.makeText(this, "Манга не загружена", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteMangaFromDatabase() {
        currentManga?.let { manga ->
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Удаление манги")
                .setMessage("Вы уверены, что хотите удалить \"${manga.name}\"?")
                .setPositiveButton("Удалить") { _, _ ->
                    performDelete(manga.id)
                }
                .setNegativeButton("Отмена", null)
                .show()
        } ?: run {
            Toast.makeText(this, "Манга не загружена", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performDelete(mangaId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = repository.deleteManga(mangaId)
                withContext(Dispatchers.Main) {
                    if (result > 0) {
                        Toast.makeText(this@ChangeMangaActivity, "Манга успешно удалена!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@ChangeMangaActivity, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            putExtra("SELECT_TAB", "home")
                        }
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@ChangeMangaActivity, "Ошибка при удалении манги", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ChangeMangaActivity, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}