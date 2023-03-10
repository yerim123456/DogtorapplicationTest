package com.example.dogtorapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.dogtorapplication.databinding.ActivityWriteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class WriteActivity : AppCompatActivity() {
    lateinit var binding: ActivityWriteBinding
    lateinit var filePath: String

    private lateinit var auth: FirebaseAuth
    lateinit var db : FirebaseFirestore
    lateinit var storage : FirebaseStorage
    lateinit var local:String
    lateinit var name:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = Firebase.storage

        binding.camera.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*"
            )
            requestLauncher.launch(intent)
        }

        binding.backBtn2.setOnClickListener{
            finish()
        }

        binding.finish.setOnClickListener {
            if (binding.imageView2.drawable !== null && binding.etCategory.text.isNotEmpty() && binding.etTitle.text.isNotEmpty() && binding.etContent.text.isNotEmpty()) {
                //store ??? ?????? ???????????? ????????? document id ????????? ????????? ?????? ?????? ??????
                saveStore()

            } else if (binding.imageView2.drawable == null) {
                Toast.makeText(this, "????????? ????????? ????????????.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "???????????? ?????? ??????????????????.", Toast.LENGTH_SHORT).show()
            }

            // ????????? ?????? ????????????
            db.collection("userplus")
                .whereEqualTo("userID", auth.uid.toString())
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        var userDTO = document.toObject(userplusImformation::class.java)
                        local = userDTO.local.toString()
                        name = userDTO.userName.toString()

                    }
                }
        }

        binding.etCategory.setOnClickListener(View.OnClickListener { v ->
            val popupMenu = PopupMenu(this, v)
            val inflater = popupMenu.menuInflater
            val menu = popupMenu.menu
            inflater.inflate(R.menu.category, menu)
            popupMenu.setOnMenuItemClickListener { item ->

                var x =item.title.toString()  // ????????? ???????????? ????????????

                binding.etCategory.setText(x)    // ???????????? ??????????????? ????????????

                false
            }
            popupMenu.show()
        })
    }


    val requestLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult())
    {
        if(it.resultCode === android.app.Activity.RESULT_OK){
            Glide
                .with(getApplicationContext())
                .load(it.data?.data)
                .apply(RequestOptions().override(250, 200))
                .centerCrop()
                .into(binding.imageView2)

            val cursor = contentResolver.query(it.data?.data as Uri,
                arrayOf<String>(MediaStore.Images.Media.DATA), null, null, null);
            cursor?.moveToFirst().let {
                filePath=cursor?.getString(0) as String
            }
        }
    }

    private fun saveStore(){

        // ????????? ?????? ????????????
        db.collection("userplus")
            .whereEqualTo("userID",auth.uid.toString())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    var userDTO = document.toObject(userplusImformation::class.java)


                    // ????????? ?????????
                    val data = mapOf(
                        "userID" to userDTO.userID.toString(),
                        "local" to userDTO.local.toString(), // ??????
                        "name" to userDTO.userName.toString(), // ??????
                        "title" to binding.etTitle.text.toString(), // ??? ??????
                        "content" to binding.etContent.text.toString(), // ??? ??????
                        "category" to binding.etCategory.text.toString(), // ????????????
                        "date" to dateToString(Date()), // ??? ??????
                        "imgUrl" to Uri.fromFile(File(filePath)).toString() // imgUri
                    )

                    // ????????? id ?????? ?????? ??????
                    db.collection("post")
                        .add(data)
                        .addOnSuccessListener {
                            // ??????????????? ????????? ?????? ??? id????????? ??????????????? ????????? ?????????
                            uploadImage(it.id)
                        }
                }
            }


    }

    private fun uploadImage(docId: String){

        // ??????????????? ???????????? StorageReference ??????
        val storageRef: StorageReference = storage.reference

        // ?????? ??????????????? ????????? ???????????? StorageReference ??????
        val imgRef: StorageReference = storageRef.child("images/${docId}.jpg")

        // ?????? ?????????
        var file = Uri.fromFile(File(filePath))
        imgRef.putFile(file)
            .addOnFailureListener { // ??????
                Log.d("lee"," failure."+it)
            }.addOnSuccessListener { // ??????
                Toast.makeText(this, "???????????? ?????????????????????.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
    }

    fun dateToString(date: Date): String {
        val format = SimpleDateFormat("MM.dd hh:mm")
        return format.format(date)
    }
}

