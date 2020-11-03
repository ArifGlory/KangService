package com.tapisdev.kangservice.activity.admin

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
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.model.Sparepart
import com.tapisdev.kangservice.util.PermissionHelper
import kotlinx.android.synthetic.main.activity_detail_sparepart.*
import kotlinx.serialization.json.Json.Default.context
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.ArrayList

class DetailSparepartActivity : BaseActivity(),PermissionHelper.PermissionListener {

    lateinit var sparepart : Sparepart
    var state = "view"
    var TAG_DELETE = "deleteSparepart"
    var TAG_EDIT = "editSparepart"
    lateinit var i : Intent

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var storageReference: StorageReference? = null
    lateinit var  permissionHelper : PermissionHelper
    var fotoBitmap : Bitmap? = null
    private var fileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_sparepart)

        i = intent
        sparepart = i.getSerializableExtra("sparepart") as Sparepart
        storageReference = FirebaseStorage.getInstance().reference.child("images")

        permissionHelper = PermissionHelper(this)
        permissionHelper.setPermissionListener(this)

        tvEdit.setOnClickListener {
            state = "edit"
            updateUI()
        }
        ivSparepart.setOnClickListener {
            launchGallery()
        }
        tvDelete.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Anda yakin menghapus ini ?")
                .setContentText("Data yang sudah dihapus tidak bisa dikembalikan")
                .setConfirmText("Ya")
                .setConfirmClickListener { sDialog ->
                    sDialog.dismissWithAnimation()
                    showLoading(this)
                    sparepartRef.document(sparepart.sparepartId.toString()).delete().addOnSuccessListener {
                        dismissLoading()
                        showSuccessMessage("Data berhasil dihapus")
                        onBackPressed()
                        Log.d("deleteDoc", "DocumentSnapshot successfully deleted!")
                    }.addOnFailureListener {
                            e ->
                        dismissLoading()
                        showErrorMessage("terjadi kesalahan "+e)
                        Log.w("deleteDoc", "Error deleting document", e)
                    }

                }
                .setCancelButton(
                    "Tidak"
                ) { sDialog -> sDialog.dismissWithAnimation() }
                .show()
        }
        tvSaveEdit.setOnClickListener {
            checkValidation()
        }

        updateUI()
    }

    fun checkValidation(){
        var getName = edFullName.text.toString()
        var getHarga = edHarga.text.toString()
        var getStok = edStok.text.toString()

        if (getName.equals("") || getName.length == 0){
            showErrorMessage("Nama Belum diisi")
        } else if (getHarga.equals("") || getHarga.length == 0){
            showErrorMessage("Harga Belum diisi")
        } else if (getStok.equals("") || getStok.length == 0){
            showErrorMessage("Stok Belum diisi")
        }else if (fileUri == null) {
            val hrg = Integer.parseInt(getHarga)
            val stok = Integer.parseInt(getStok)
            updateDataOnly(getName,hrg,stok)
        }else{
            val hrg = Integer.parseInt(getHarga)
            val stok = Integer.parseInt(getStok)
            uploadAndUpdate(getName,hrg,stok)
        }
    }

    fun updateDataOnly(name : String,harga : Int,stok : Int){
        showLoading(this)
        sparepartRef.document(sparepart.sparepartId.toString()).update("nama",name)
        sparepartRef.document(sparepart.sparepartId.toString()).update("harga",harga)
        sparepartRef.document(sparepart.sparepartId.toString()).update("stok",stok).addOnCompleteListener { task ->
            dismissLoading()
            if (task.isSuccessful){
                showSuccessMessage("Ubah data berhasil")
                onBackPressed()
            }else{
                showLongErrorMessage("terjadi kesalahan : "+task.exception)
                Log.d(TAG_EDIT,"err : "+task.exception)
            }
        }
    }

    fun uploadAndUpdate(name : String,harga : Int,stok : Int){
        showLoading(this)
        if (fileUri != null){
            val baos = ByteArrayOutputStream()
            fotoBitmap?.compress(Bitmap.CompressFormat.JPEG,50,baos)
            val data: ByteArray = baos.toByteArray()

            val fileReference = storageReference!!.child(System.currentTimeMillis().toString())
            val uploadTask = fileReference.putBytes(data)

            uploadTask.addOnFailureListener {
                    exception -> Log.d(TAG_EDIT, exception.toString())
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
                        Log.d(TAG_EDIT,"download URL : "+ downloadUri.toString())// This is the one you should store

                        sparepartRef.document(sparepart.sparepartId.toString()).update("nama",name)
                        sparepartRef.document(sparepart.sparepartId.toString()).update("harga",harga)
                        sparepartRef.document(sparepart.sparepartId.toString()).update("foto",url)
                        sparepartRef.document(sparepart.sparepartId.toString()).update("stok",stok).addOnCompleteListener { task ->
                            dismissLoading()
                            if (task.isSuccessful){
                                showSuccessMessage("Ubah data berhasil")
                                onBackPressed()
                            }else{
                                showLongErrorMessage("terjadi kesalahan : "+task.exception)
                                Log.d(TAG_EDIT,"err : "+task.exception)
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

    fun updateUI(){

        if(state.equals("view")){
            edFullName.setText(sparepart.nama)
            edHarga.setText("Rp. "+sparepart.harga)
            edStok.setText("Stok : "+sparepart.stok)
            Glide.with(this)
                .load(sparepart.foto)
                .into(ivSparepart)

            tvTitle.setText("Detail Sparepart")

            tvInfoFoto.visibility = View.INVISIBLE

            edFullName.isEnabled = false
            edHarga.isEnabled = false
            edStok.isEnabled = false
            ivSparepart.isEnabled = false
            tvSaveEdit.isEnabled = false
        }else{
            edHarga.setText(""+sparepart.harga)
            edStok.setText(""+sparepart.stok)
            tvTitle.setText("Ubah Data Sparepart")

            edFullName.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_onlycorner_gary));
            edHarga.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_onlycorner_gary));
            edStok.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_onlycorner_gary));

            tvInfoFoto.visibility = View.VISIBLE
            tvSaveEdit.visibility = View.VISIBLE

            edFullName.isEnabled = true
            edHarga.isEnabled = true
            edStok.isEnabled = true
            ivSparepart.isEnabled = true
            tvSaveEdit.isEnabled = true
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
                ivSparepart.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
