package com.tapisdev.kangservice.activity.pengguna

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.model.Perbaikan
import com.tapisdev.kangservice.model.UserModel
import kotlinx.android.synthetic.main.activity_detail_perbaikan.*
import kotlinx.android.synthetic.main.activity_detail_perbaikan.tvAlamatAdmin
import kotlinx.android.synthetic.main.activity_detail_perbaikan.tvMerkHp
import kotlinx.android.synthetic.main.activity_detail_perbaikan.tvTanggalAdmin
import kotlinx.android.synthetic.main.activity_detail_perbaikan_user.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class DetailPerbaikanUserActivity : BaseActivity() {

    lateinit var perbaikan : Perbaikan
    lateinit var i : Intent
    lateinit var user : UserModel
    var TAG_GET_USER = "userGET"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_perbaikan_user)

        i = intent
        perbaikan = i.getSerializableExtra("perbaikan") as Perbaikan
        Log.d("perbaikan"," id : "+perbaikan.layananId)

        ivBackPerbaikanUser.setOnClickListener {
            onBackPressed()
        }

        updaateUI()
        getDataUser()
    }

    fun updaateUI(){
        tvMerkHp.setText("Merk HP : "+perbaikan.merkHp)
        tvTanggalAdmin.setText("Tanggal pesan : "+perbaikan.tanggalMulai?.let { convertTanggal(it) })
        tvAlamatAdmin.setText("Alamat : "+perbaikan.alamatUser)
        tvStatusPerbaikan.setText(perbaikan.status)

        val nf = NumberFormat.getNumberInstance(Locale.GERMAN)
        val df = nf as DecimalFormat
        tvTotalPriceUser.setText(perbaikan.namaLayanan+"\n Rp. "+df.format(perbaikan.harga))

    }

    fun getDataUser(){
        userRef.document(perbaikan.idAdmin.toString()).get().addOnCompleteListener { task ->
            if (task.isSuccessful){
                val document = task.result
                if (document != null) {
                    if (document.exists()) {
                        Log.d(TAG_GET_USER, "DocumentSnapshot data: " + document.data)
                        //convert doc to object
                        user = document.toObject(UserModel::class.java)!!
                        tvNamaToko.setText(user.name)

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
