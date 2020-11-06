package com.tapisdev.kangservice.activity.admin

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.storage.StorageReference
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.model.Layanan
import com.tapisdev.kangservice.util.PermissionHelper
import kotlinx.android.synthetic.main.activity_add_layanan.*
import kotlinx.android.synthetic.main.activity_detail_layanan.*
import kotlinx.android.synthetic.main.activity_detail_layanan.edEstimasi
import kotlinx.android.synthetic.main.activity_detail_layanan.edHargaLayanan
import kotlinx.android.synthetic.main.activity_detail_layanan.edNamaLayanan
import kotlinx.android.synthetic.main.activity_detail_layanan.edRincian
import kotlinx.android.synthetic.main.activity_detail_sparepart.*
import kotlinx.android.synthetic.main.activity_detail_sparepart.tvTitle

class DetailLayananActivity : BaseActivity() {

    lateinit var layanan : Layanan
    var state = "view"
    var TAG_DELETE = "deleteLayanan"
    var TAG_EDIT = "editLayanan"
    lateinit var i : Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_layanan)

        i = intent
        layanan = i.getSerializableExtra("layanan") as Layanan
        Log.d("layanan"," id : "+layanan.layananId)

        tvSaveUpdate.setOnClickListener {
            checkValidation()
        }
        tvDeleteLayanan.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Anda yakin menghapus ini ?")
                .setContentText("Data yang sudah dihapus tidak bisa dikembalikan")
                .setConfirmText("Ya")
                .setConfirmClickListener { sDialog ->
                    sDialog.dismissWithAnimation()
                    showLoading(this)
                    layananRef.document(layanan.layananId.toString()).delete().addOnSuccessListener {
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
        tvEditLayanan.setOnClickListener {
            state = "edit"
            updateUI()
        }

        updateUI()
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
            layanan = Layanan(layanan.layananId,
                getName,
                harga,
                getEstimasi,
                getRincian,
                auth.currentUser?.uid
            )
            updateLayanan()
        }
    }

    fun updateLayanan(){
        showLoading(this)
        showInfoMessage("Sedang menyimpan ke database..")
        layananRef.document(layanan.layananId.toString()).update("nama",layanan.nama)
        layananRef.document(layanan.layananId.toString()).update("harga",layanan.harga)
        layananRef.document(layanan.layananId.toString()).update("estimasiWaktu",layanan.estimasiWaktu)
        layananRef.document(layanan.layananId.toString()).update("rincianLayanan",layanan.rincianLayanan).addOnCompleteListener { task ->
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

    fun updateUI(){

        if(state.equals("view")){
            edNamaLayanan.setText(layanan.nama)
            edHargaLayanan.setText("Rp. "+layanan.harga)
            edEstimasi.setText("Estimasi "+layanan.estimasiWaktu + " hari")
            edRincian.setText(layanan.rincianLayanan)

            tvTitle.setText("Detail Layanan Kendala")


            edNamaLayanan.isEnabled = false
            edHargaLayanan.isEnabled = false
            edEstimasi.isEnabled = false
            edRincian.isEnabled = false

            tvSaveUpdate.isEnabled = false
        }else{
            edNamaLayanan.setText(""+layanan.nama)
            edHargaLayanan.setText(""+layanan.harga)
            edEstimasi.setText(""+layanan.estimasiWaktu)
            edRincian.setText(""+layanan.rincianLayanan)
            tvTitle.setText("Ubah Data Layanan")

            edNamaLayanan.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_onlycorner_gary));
            edHargaLayanan.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_onlycorner_gary));
            edEstimasi.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_onlycorner_gary));
            edRincian.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_onlycorner_gary));

            tvSaveUpdate.visibility = View.VISIBLE

            edNamaLayanan.isEnabled = true
            edHargaLayanan.isEnabled = true
            edEstimasi.isEnabled = true
            edRincian.isEnabled = true
            tvSaveUpdate.isEnabled = true
        }
    }
}
