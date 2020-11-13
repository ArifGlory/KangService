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
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.model.Pesanan
import com.tapisdev.kangservice.model.UserPreference
import com.tapisdev.kangservice.util.PermissionHelper
import kotlinx.android.synthetic.main.activity_bukti_bayar.*
import kotlinx.android.synthetic.main.activity_detail_sparepart.*
import kotlinx.android.synthetic.main.dlg_confomation.*
import java.io.ByteArrayOutputStream
import java.io.IOException

class BuktiBayarActivity : BaseActivity(),PermissionHelper.PermissionListener {

    var TAG_BUKTI = "buktiBayar"
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var fileUri: Uri? = null
    var fotoBitmap : Bitmap? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    lateinit var  permissionHelper : PermissionHelper
    lateinit var i : Intent
    lateinit var pesanan : Pesanan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bukti_bayar)
        i = intent
        pesanan = i.getSerializableExtra("pesanan") as Pesanan
        mUserPref = UserPreference(this)

        storageReference = FirebaseStorage.getInstance().reference.child("images")
        permissionHelper = PermissionHelper(this)
        permissionHelper.setPermissionListener(this)

        updateUI()

        ivBuktiBayar.setOnClickListener { 
            launchGallery()
        }
        tvAddBukti.setOnClickListener {
            checkValidation()
        }
    }

    fun updateUI(){
        if (!pesanan.buktiBayar.equals("")){
            Glide.with(this)
                .load(pesanan.buktiBayar)
                .into(ivBuktiBayar)
            tvAddBukti.setText("Simpan Perubahan")
        }else{
            tvAddBukti.setText("Tambahkan")
        }

        if (mUserPref.getJenisUser().equals("admin")){
            tvAddBukti.visibility = View.INVISIBLE
            tvHint.visibility = View.INVISIBLE
        }
    }

    fun checkValidation(){
        if (fileUri == null){
            showErrorMessage("Anda belum memilih gambar")
        }else{
            uploadAndUpdate()
        }
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
                    exception -> Log.d(TAG_BUKTI, exception.toString())
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
                        Log.d(TAG_BUKTI,"download URL : "+ downloadUri.toString())// This is the one you should store

                        pesanananRef.document(pesanan.pesananId.toString()).update("buktiBayar",url).addOnCompleteListener { task ->
                            dismissLoading()
                            if (task.isSuccessful){
                                showSuccessMessage("Data Bukti bayar disimpan")
                                val i = Intent(this,TransaksiSparepartActivity::class.java)
                                startActivity(i)
                            }else{
                                showLongErrorMessage("terjadi kesalahan : "+task.exception)
                                Log.d(TAG_BUKTI,"err : "+task.exception)
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
                ivBuktiBayar.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun launchGallery() {
        var listPermissions: MutableList<String> = java.util.ArrayList()
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
