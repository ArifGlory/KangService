package com.tapisdev.kangservice.activity.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.model.Perbaikan
import com.tapisdev.kangservice.model.UserModel
import kotlinx.android.synthetic.main.activity_detail_perbaikan.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class DetailPerbaikanActivity : BaseActivity() {

    lateinit var perbaikan : Perbaikan
    lateinit var i : Intent
    lateinit var user : UserModel

    var TAG_GET_DETAILPESANAN = "detailpesananGET"
    var selectedStatus = "none"
    var TAG_GET_USER = "userGET"
    var TAG_UBAH_STATUS = "ubahStatus"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_perbaikan)

        i = intent
        perbaikan = i.getSerializableExtra("perbaikan") as Perbaikan
        Log.d("perbaikan"," id : "+perbaikan.layananId)

        ivBackPerbaikan.setOnClickListener {
            onBackPressed()
        }


        updateUI()
        getDataUser()
    }

    fun updateUI(){
        tvMerkHp.setText(perbaikan.merkHp)
        tvTanggalAdmin.setText("Tanggal pesan : "+perbaikan.tanggalMulai?.let { convertTanggal(it) })
        tvAlamatAdmin.setText("Alamat : "+perbaikan.alamatUser)
        tvStatusAdmin.setText(perbaikan.status)

        val nf = NumberFormat.getNumberInstance(Locale.GERMAN)
        val df = nf as DecimalFormat
        tvTotalPriceAdmin.setText(perbaikan.namaLayanan+"\n Rp. "+df.format(perbaikan.harga))

        if (perbaikan.status.equals("pesanan selesai")){
            tvUbahStatus.visibility = View.INVISIBLE
            tvUbahStatus.isEnabled = false
        }
    }

    fun getDataUser(){
        userRef.document(perbaikan.idUser.toString()).get().addOnCompleteListener { task ->
            if (task.isSuccessful){
                val document = task.result
                if (document != null) {
                    if (document.exists()) {
                        Log.d(TAG_GET_USER, "DocumentSnapshot data: " + document.data)
                        //convert doc to object
                        user = document.toObject(UserModel::class.java)!!
                        tvNamaPemesan.setText(user.name)

                    } else {
                        Log.d(TAG_GET_USER, "No such document")
                    }
                }
            }else{
                showErrorMessage("Pengguna tidak ditemukan")
                Log.d(TAG_GET_USER,"err : "+task.exception)
            }
        }
    }

    fun convertTanggal(tanggal : String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd")
        //val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        val output = formatter.format(parser.parse(tanggal))

        return output
    }
}
