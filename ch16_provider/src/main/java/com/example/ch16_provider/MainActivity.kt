package com.example.ch16_provider

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.ch16_provider.databinding.ActivityMainBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var filePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //gallery request launcher..................
        val requestGalleryLauncher = registerForActivityResult(
            //intent를 발생시키는 역할.
            ActivityResultContracts.StartActivityForResult())
        //되돌아 왔을 때 콜백처리.
        {
            //사진 사이즈를 좀 줄여서 얼마나 줄일지를 계산하는 코드
            try{
                val calRatio = calculateInSampleSize(
                    it.data!!.data!!,
                    resources.getDimensionPixelSize(R.dimen.imgSize),
                    resources.getDimensionPixelSize(R.dimen.imgSize)
                )
                //줄이는 작업은 우리가 할 필요x bitmap팩토리가 해주는 역할.
                val option = BitmapFactory.Options()
                option.inSampleSize = calRatio

                var inputStream = contentResolver.openInputStream(it.data!!.data!!)
                //bitmap으로 이미지 목록에서 선택한 이미지를 얻어냄.
                val bitmap = BitmapFactory.decodeStream(inputStream,null,option)
                inputStream!!.close()
                inputStream = null

                //이미지 활용
                bitmap?.let{
                    //선택한 이미지를 화면에 출력
                    binding.userImageView.setImageBitmap(bitmap)
                }?:let{
                    Log.d("SSu","bitmap null")
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }


        binding.galleryButton.setOnClickListener {
            //gallery app........................
            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            //갤러리앱의 목록화면 출력
            requestGalleryLauncher.launch(intent)
        }

        //camera request launcher.................
        val requestCameraFilelauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            //되돌아 왔을 때 사후처리
            val calRatio = calculateInSampleSize(
                Uri.fromFile(File(filePath)),
                resources.getDimensionPixelSize(R.dimen.imgSize),
                resources.getDimensionPixelSize(R.dimen.imgSize)
            )
            val option = BitmapFactory.Options()
            option.inSampleSize = calRatio
            //파일패스정보를 알고 있으니 바로 bitmap정보를 얻도록 함.
            val bitmap = BitmapFactory.decodeFile(filePath, option)
            bitmap?.let {
                binding.userImageView.setImageBitmap(bitmap)
            }
        }


        binding.cameraButton.setOnClickListener {
            //camera app......................
            //파일 준비...............파일에 사진 저장 후 되돌아오는 과정
            val timeStamp: String =
                //파일 이름 중복을 방지
                SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            //파일 저장위치를 지정.
            val storageDir: File? =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            //실제 이미지가 저장될 파일, 카메라로 찍은 사진이 저장된 후에 넘어감.
            val file = File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                storageDir
            )
            filePath = file.absolutePath
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.example.ch16_provider.fileprovider",
                file
            )
            //아래 액션문자열을 실행하면 카메라 앱이 실행.
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //우리가 준비한 파일정보를 넘겨주고 사진을 파일에 write
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            requestCameraFilelauncher.launch(intent)
        }
    }

    private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        try {
            var inputStream = contentResolver.openInputStream(fileUri)

            //inJustDecodeBounds 값을 true 로 설정한 상태에서 decodeXXX() 를 호출.
            //로딩 하고자 하는 이미지의 각종 정보가 options 에 설정 된다.
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream!!.close()
            inputStream = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //비율 계산........................
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1
        //inSampleSize 비율 계산
        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

}