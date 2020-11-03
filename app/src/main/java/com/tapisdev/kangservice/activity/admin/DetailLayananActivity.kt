package com.tapisdev.kangservice.activity.admin

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.storage.StorageReference
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.model.Layanan
import com.tapisdev.kangservice.util.PermissionHelper
import kotlinx.android.synthetic.main.activity_detail_layanan.*
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

        tvSaveUpdate.setOnClickListener {

        }
        
        updateUI()
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
