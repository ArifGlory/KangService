package com.tapisdev.kangservice.activity.pengguna

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.MainActivity
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.model.UserPreference
import com.tapisdev.kangservice.util.PermissionHelper
import kotlinx.android.synthetic.main.activity_profil.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.ArrayList

class ProfilUserActivity : BaseActivity() ,PermissionHelper.PermissionListener{

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    lateinit var  permissionHelper : PermissionHelper
    var fotoBitmap : Bitmap? = null
    private var fileUri: Uri? = null

    var TAG_GAMBAR = "ubahGambar"
    var TAG_EDIT = "editProfil"
    var state = "view"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil_user)

        mUserPref = UserPreference(this)
        storageReference = FirebaseStorage.getInstance().reference.child("images")

        permissionHelper = PermissionHelper(this)
        permissionHelper.setPermissionListener(this)

        tvLogout.setOnClickListener {
            auth.signOut()
            clearSession()
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
        ivGallery.setOnClickListener {
            launchGallery()
        }
        tvEnableUpdate.setOnClickListener {
            state = "edit"
            updateUI()
        }
        tvSaveUpdate.setOnClickListener {
            checkValidation()
        }

        updateUI()
    }

    fun checkValidation(){
        var getName = edUserName.text.toString()
        var getMobileNumber = edMobileNumber.text.toString()

        if (getName.equals("") || getName.length == 0){
            showErrorMessage("Nama Belum diisi")
        } else if (getMobileNumber.equals("") || getMobileNumber.length == 0){
            showErrorMessage("No. Handphone Belum diisi")
        }
        else {
            showLoading(this)
            userRef.document(auth.currentUser?.uid.toString()).update("name",getName)
            userRef.document(auth.currentUser?.uid.toString()).update("phone",getMobileNumber).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    dismissLoading()
                    edMobileNumber.setText(getMobileNumber)
                    edUserName.setText(getName)
                    state = "view"

                    mUserPref.saveName(getName)
                    mUserPref.savePhone(getMobileNumber)

                    updateUI()
                    showSuccessMessage("Data berhasil diubah")
                    onBackPressed()
                }else{
                    dismissLoading()
                    showLongErrorMessage("Penyimpanan data gagal")
                    Log.d(TAG_EDIT,"err : "+task.exception)
                }
            }
        }
    }

    fun updateUI(){

        if(state.equals("view")){
            if (!mUserPref.getFoto().equals("")){
                Glide.with(this)
                    .load(mUserPref.getFoto())
                    .into(ivProfile)
            }
            edUserName.setText(mUserPref.getName())
            edMobileNumber.setText(mUserPref.getPhone())

            edUserName.isEnabled = false
            edMobileNumber.isEnabled = false
        }else{
            edUserName.background = resources.getDrawable(R.drawable.bg_onlycorner_gary)
            edMobileNumber.background = resources.getDrawable(R.drawable.bg_onlycorner_gary)

            edUserName.isEnabled = true
            edMobileNumber.isEnabled = true

            tvEnableUpdate.visibility = View.INVISIBLE
            tvSaveUpdate.visibility = View.VISIBLE

            tvSaveUpdate.isEnabled = true
        }

    }


    fun clearSession(){
        mUserPref.saveName("")
        mUserPref.saveEmail("")
        mUserPref.saveFoto("")
        mUserPref.saveJenisUser("none")
        mUserPref.savePhone("")
        mUserPref.saveAlamat("none")
        mUserPref.saveLatlon("")
    }

    fun uploadAndUpdate(){
        showLoading(this)
        if (fileUri != null){
            val baos = ByteArrayOutputStream()
            fotoBitmap?.compress(Bitmap.CompressFormat.JPEG,50,baos)
            val data: ByteArray = baos.toByteArray()

            val fileReference = storageReference!!.child(System.currentTimeMillis().toString())
            val uploadTask = fileReference.putBytes(data)

            uploadTask.addOnFailureListener {
                    exception -> Log.d(TAG_GAMBAR, exception.toString())
            }.addOnSuccessListener {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                showSuccessMessage("Image Berhasil di upload")
                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                    }

                    fileReference.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        val url = downloadUri!!.toString()
                        Log.d(TAG_GAMBAR,"download URL : "+ downloadUri.toString())// This is the one you should store

                        userRef.document(auth.currentUser?.uid.toString()).update("foto",url).addOnCompleteListener { task ->
                            dismissLoading()
                            if (task.isSuccessful){
                                showSuccessMessage("Upload foto berhasil")
                                mUserPref.saveFoto(url)
                                ivProfile.setImageBitmap(fotoBitmap)
                            }else{
                                showErrorMessage("terjadi kesalahan : "+task.exception)
                                Log.d(TAG_GAMBAR,"err : "+task.exception)
                            }
                        }

                    } else {
                        dismissLoading()
                        showErrorMessage("Terjadi kesalahan, coba lagi nanti")
                    }
                }
            }.addOnProgressListener { taskSnapshot ->
                val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                pDialogLoading.setTitleText("Uploaded " + progress.toInt() + "%...")
            }

        }else{
            dismissLoading()
            showErrorMessage("Belum memilih gambar")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            fileUri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                fotoBitmap = bitmap
                uploadAndUpdate()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun launchGallery() {
        var listPermissions: MutableList<String> = ArrayList()
        listPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        listPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        permissionHelper.checkAndRequestPermissions(listPermissions)
    }

    override fun onPermissionCheckDone() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }
}
