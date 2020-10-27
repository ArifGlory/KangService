package com.tapisdev.kangservice.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import com.google.android.gms.tasks.OnCompleteListener
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.MainActivity
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.model.UserModel
import com.tapisdev.kangservice.model.UserPreference
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {

    var selectedJenisUser = "none"
    var TAG_JENIS = "jenisUser"
    var TAG_SIMPAN = "simpanUser"
    lateinit var userModel : UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mUserPref = UserPreference(this)

        tvLogin.setOnClickListener {
            val i = Intent(applicationContext, MainActivity::class.java)
            startActivity(i)
        }

        spJenisUser.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                Log.d(TAG_JENIS, "jenis nya "+ parent?.getItemAtPosition(position).toString())
                var selected = parent?.getItemAtPosition(position).toString()
                if (selected.equals("Pilih Jenis Pengguna")){
                    selectedJenisUser = "none"
                }else if (selected.equals("Admin Catering/Tenda")){
                    selectedJenisUser = "admin"
                }else if (selected.equals("Pengguna")){
                    selectedJenisUser = "pengguna"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        btnDaftar.setOnClickListener {
            checkValidation()
        }
    }

    fun checkValidation(){
        var getName = etNama.text.toString()
        var getEmail = etEmail.text.toString()
        var getPhone = etNope.text.toString()
        var getPassword = etPassword.text.toString()
        var getConfirmPassword = etPassword2.text.toString()

        if (getName.equals("") || getName.length == 0){
            showErrorMessage("Nama Belum diisi")
        }else  if (getEmail.equals("") || getEmail.length == 0){
            showErrorMessage("email Belum diisi")
        }
        else  if (getPhone.equals("") || getPhone.length == 0){
            showErrorMessage("phone Belum diisi")
        }else  if (getPassword.equals("") || getEmail.length == 0){
            showErrorMessage("password Belum diisi")
        }else  if (getConfirmPassword.equals("") || getConfirmPassword.length == 0){
            showErrorMessage("konfirmasi passsword Belum diisi")
        }else if (selectedJenisUser.equals("none")){
            showErrorMessage("Jenis Pengguna Belum dipilih")
        }else if (!getPassword.equals(getConfirmPassword)){
            showErrorMessage("Konfirmasi password tidak valid")
        }else if(getPassword.length < 8 || getConfirmPassword.length < 8){
            showErrorMessage("Password harus lebih dari 8 karakter")
        }
        else{
            userModel = UserModel(getName,getEmail,"",getPhone,selectedJenisUser,"","none","none","")
            Log.d(TAG_SIMPAN," namanya : "+userModel.name)

            registerUser(userModel.email,getPassword)
        }
    }

    fun registerUser(email : String,pass : String){
        showLoading(this)
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this, OnCompleteListener{task ->
            if (task.isSuccessful){
                var userId = auth.currentUser?.uid

                if (userId != null) {
                    userModel.uId = userId
                    userRef.document(userId).set(userModel).addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            dismissLoading()
                            showLongSuccessMessage("Pendaftaran Berhasil, Silakan Login Untuk Melanjutkan")
                            val i = Intent(applicationContext,MainActivity::class.java)
                            startActivity(i)
                        }else{
                            dismissLoading()
                            showLongErrorMessage("Error pendaftaran, coba lagi nanti ")
                            Log.d(TAG_SIMPAN,"err : "+task.exception)
                        }
                    }
                }else{
                    showLongErrorMessage("user id tidak di dapatkan")
                }
            }else{
                dismissLoading()
                if(task.exception?.equals("com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account.")!!){
                    showLongErrorMessage("Email sudah pernah digunakan ")
                }else{
                    showLongErrorMessage("Error pendaftaran, Cek apakah email sudah pernah digunakan / belum dan  coba lagi nanti ")
                    Log.d(TAG_SIMPAN,"err : "+task.exception)
                }

            }
        })
    }


}
