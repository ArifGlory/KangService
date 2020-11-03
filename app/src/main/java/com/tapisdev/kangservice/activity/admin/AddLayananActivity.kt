package com.tapisdev.kangservice.activity.admin

import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.model.Layanan
import com.tapisdev.kangservice.model.UserPreference
import com.tapisdev.kangservice.util.PermissionHelper
import kotlinx.android.synthetic.main.activity_add_layanan.*
import kotlinx.android.synthetic.main.activity_add_sparepart.*
import kotlinx.android.synthetic.main.activity_add_sparepart.tvAdd

class AddLayananActivity : BaseActivity() {

    var TAG_SIMPAN = "simpanLayanan"
    lateinit var layanan : Layanan

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    lateinit var  permissionHelper : PermissionHelper
    var fotoBitmap : Bitmap? = null
    private var fileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_layanan)
        mUserPref = UserPreference(this)

        tvAddLayanan.setOnClickListener {
            checkValidation()
        }
    }

    fun checkValidation(){
        var getName = edNamaLayanan.text.toString()
        var getHarga = edHargaLayanan.text.toString()
        var getEstimasi = edEstimasi.text.toString()
        var getRincian = edRincian.text.toString()

        if (getName.equals("") || getName.length == 0){
            showErrorMessage("Nama Belum diisi")
        } else if (getHarga.equals("") || getHarga.length == 0){
            showErrorMessage("Harga Belum diisi")
        } else if (getEstimasi.equals("") || getEstimasi.length == 0){
            showErrorMessage("Estimasi Belum diisi")
        }else if (getRincian.equals("") || getRincian.length == 0){
            showErrorMessage("Rincian layanan Belum diisi")
        }
        else {
            var harga = Integer.parseInt(getHarga)
            layanan = Layanan("",
                getName,
                harga,
                getEstimasi,
                getRincian,
                auth.currentUser?.uid
            )
            saveLayanan()
        }
    }

    fun saveLayanan(){
        showLoading(this)
        showInfoMessage("Sedang menyimpan ke database..")
        layananRef.document().set(layanan).addOnCompleteListener{
                task ->
            dismissLoading()
            if (task.isSuccessful){
                showSuccessMessage("Data berhasil ditambahkan")
                onBackPressed()
            }else{
                showLongErrorMessage("Penyimpanan data gagal")
                Log.d(TAG_SIMPAN,"err : "+task.exception)
            }
        }
    }
}
