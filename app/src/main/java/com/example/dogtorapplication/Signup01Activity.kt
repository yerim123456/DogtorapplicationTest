package com.example.dogtorapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.dogtorapplication.databinding.ActivitySignup01Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class Signup01Activity : AppCompatActivity() {

    // 뷰 바인딩을 위한 객체 획득
    lateinit var binding: ActivitySignup01Binding
    lateinit var db : FirebaseFirestore

    // 파이어 베이스 회원가입을 위한 객체 획득
    private lateinit var auth: FirebaseAuth
    var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivitySignup01Binding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = Firebase.auth // 객체 정의

        // 가입 버튼 클릭 시 가입 진행될 수 있도록
        binding.loginSubmitButton.setOnClickListener {
            signinEmail()

        }

        // 취소 버튼 클릭 시 뒤로갈 수 있도록
        binding.loginCancleButton.setOnClickListener {
            finish()
        }

    }
    fun checkAuth():Boolean{
        val currentUser = auth.currentUser
        return currentUser?.let{
            email = currentUser.email
            currentUser.isEmailVerified
        } ?: let{
            false
        }
    }
    fun signinEmail(){
        // createUserWithEmailAndPassword() 이용하여 사용자 생성
        auth.createUserWithEmailAndPassword(binding.emailEditText.text.toString(),binding.numberEditText.text.toString())
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful){ // 생성이 되었다면
                    var userInfo = userplusImformation() // 데이터 구조
                    userInfo.userID = auth?.uid
                    db?.collection("userplus")
                        ?.document(auth?.uid.toString())
                        ?.set(userInfo)

                    val nextIntent = Intent(this, LoginActivity::class.java)
                    startActivity(nextIntent)
                }
                else{ // 생성을 못했다면
                    Toast.makeText(this,task.exception?.message, Toast.LENGTH_LONG).show() // 토스트 메세지 띄우기
                    //Show the error message
                }
            }
    }
}