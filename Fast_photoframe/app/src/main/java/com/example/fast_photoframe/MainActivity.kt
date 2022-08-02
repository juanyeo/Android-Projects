package com.example.fast_photoframe

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val addPhotoButton: Button by lazy {
        findViewById<Button>(R.id.addPhotoButton)
    }

    private val startAnimateButton: Button by lazy {
        findViewById<Button>(R.id.startAnimateButton)
    }

    private val imageViewList: List<ImageView> by lazy {
        mutableListOf<ImageView>().apply {
            add(findViewById(R.id.imageView11))
            add(findViewById(R.id.imageView12))
            add(findViewById(R.id.imageView13))
            add(findViewById(R.id.imageView21))
            add(findViewById(R.id.imageView22))
            add(findViewById(R.id.imageView23))
        }
    }

    private var imageUriList: MutableList<Uri> = mutableListOf()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAddPhotoButton()
        initStartAnimateButton()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initAddPhotoButton() {
        addPhotoButton.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED -> {
                    choosePhotoFromGallery()
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    showPermissionContextPopup()
                }
                else -> requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
            }
        }
    }

    private fun initStartAnimateButton() {
        startAnimateButton.setOnClickListener {
            if (imageUriList.isEmpty()) return@setOnClickListener

            Log.d("ggg", "프린트프" + imageUriList.size)
            val intent = Intent(this, PhotoFrameActivity::class.java)
            intent.putExtra("uriListSize", imageUriList.size)
            for (i: Int in 0..(imageUriList.size-1)) {
                intent.putExtra("image$i", imageUriList[i].toString())
            }
            startActivity(intent)
        }
    }

    private fun choosePhotoFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) return

        if (requestCode == 2000) {
            val selectedImageUri: Uri? = data?.data

            if (selectedImageUri != null) {
                if (imageUriList.size >= 6) {
                    Toast.makeText(this, "사진을 더이상 선택할 수 없습니다", Toast.LENGTH_SHORT).show()
                    return
                }

                imageUriList.add(selectedImageUri)
                imageViewList[imageUriList.size -1].setImageURI(selectedImageUri)
            } else {
                Toast.makeText(this, "사진을 가져오지 못했습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요한 이유")
            .setMessage("필요한 이유 구구절절 설명")
            .setPositiveButton("권한설정") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
            }
            .setNegativeButton("취소") { _, _ -> }
            .create()
            .show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1000) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choosePhotoFromGallery()
            } else {
                Toast.makeText(this, "권한을 거부하였습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }
}