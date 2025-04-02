package com.example.firebaseapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnLogin: Button
    private lateinit var btnShowData: Button
    private lateinit var txtUserData: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        btnRegister = findViewById(R.id.btnRegister)
        btnLogin = findViewById(R.id.btnLogin)
        btnShowData = findViewById(R.id.btnShowData)
        txtUserData = findViewById(R.id.txtUserData)

        btnRegister.setOnClickListener {
            signUpByEmailPassword(edtEmail.text.toString(), edtPassword.text.toString())
        }
        btnLogin.setOnClickListener {
            signInByEmailPassword(edtEmail.text.toString(), edtPassword.text.toString())
        }
        btnShowData.setOnClickListener { showUserData() }
    }

    private fun saveUserToDatabase(email: String) {
        val uid = auth.currentUser?.uid ?: return
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(uid)
        val user = mapOf("email" to email)

        userRef.setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Lưu thông tin thành công!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Lưu thông tin thất bại!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun signUpByEmailPassword(email: String, password: String) {
        if (email.isNotEmpty() && password.length >= 6) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        txtUserData.text = "Thông báo: Bạn đã đăng ký tài khoản thành công!"
                    } else {
                        Toast.makeText(this, "Lỗi: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Nhập email hợp lệ và mật khẩu >= 6 ký tự!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInByEmailPassword(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        txtUserData.text = "Thông báo: Bạn đã đăng nhập thành công, có thể nhấn \"Hiển thị dữ liệu\" để xem thông tin!"
                        saveUserToDatabase(email)
                    } else {
                        Toast.makeText(this, "Lỗi: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Vui lòng nhập email và mật khẩu!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showUserData() {
        val uid = auth.currentUser?.uid ?: return
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(uid)

        userRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val email = snapshot.child("email").value.toString()
                txtUserData.text = "Email của bạn: $email"
            } else {
                txtUserData.text = "Không tìm thấy dữ liệu!"
            }
        }.addOnFailureListener {
            txtUserData.text = "Lỗi tải dữ liệu!"
        }
    }
}
