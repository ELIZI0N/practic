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

class AddMangaActivity : AppCompatActivity() {

    private lateinit var repository: Repository
    private var selectedImageUri: Uri? = null
    private var selectedImagePath: String? = null

    private lateinit var imagePreview: ImageView
    private lateinit var btnSelectImage: Button
    private lateinit var etTitle: EditText
    private lateinit var spinnerType: Spinner
    private lateinit var etAuthor: EditText
    private lateinit var etReleaseYear: EditText
    private lateinit var etChapters: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnAddManga: Button
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
        setContentView(R.layout.manga_add)

        repository = Repository(this)
        initViews()
        setupTypeDropdown()
        setupClickListeners()
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
        btnAddManga = findViewById(R.id.btnAddManga)
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

        btnAddManga.setOnClickListener {
            addMangaToDatabase()
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

    private fun addMangaToDatabase() {
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

        if (selectedImagePath == null) {
            Toast.makeText(this, "Выберите изображение", Toast.LENGTH_SHORT).show()
            return
        }

        val releaseYear = releaseYearText.toIntOrNull()
        val chapters = chaptersText.toIntOrNull()

        if (releaseYear == null) {
            Toast.makeText(this, "Год релиза должен быть числом", Toast.LENGTH_SHORT).show()
            return
        }

        if (releaseYear < 1770 || releaseYear > 2025) {
            Toast.makeText(this, "Год релиза должен быть между 1770 и 2025", Toast.LENGTH_SHORT).show()
            return
        }

        if (chapters == null || chapters <= 0) {
            Toast.makeText(this, "Количество глав должно быть положительным числом", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val allMangas = repository.getAllMangasAsList()
                val maxPopularity = allMangas.maxByOrNull { it.popularity ?: 0 }?.popularity ?: 0
                val newPopularity = maxPopularity + 1

                val manga = Manga(
                    name = title,
                    images = selectedImagePath!!,
                    authors = author,
                    release = releaseYear,
                    views = "0",
                    chapters = chapters,
                    score = 0.0,
                    synopsis = description,
                    popularity = newPopularity,
                    type = type
                )

                try {
                    repository.addManga(manga)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@AddMangaActivity, "Манга успешно добавлена!", Toast.LENGTH_SHORT).show()
                        // Очистка формы после успешного добавления
                        clearForm()

                        // Возврат на главный экран
                        val intent = Intent(this@AddMangaActivity, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            putExtra("SELECT_TAB", "home")
                        }
                        startActivity(intent)
                        finish()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@AddMangaActivity, "Ошибка при добавлении манги: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddMangaActivity, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun clearForm() {
        etTitle.text?.clear()
        etAuthor.text?.clear()
        etReleaseYear.text?.clear()
        etChapters.text?.clear()
        etDescription.text?.clear()
        spinnerType.setSelection(0)
        imagePreview.setImageResource(android.R.color.transparent)
        selectedImageUri = null
        selectedImagePath = null
    }
}