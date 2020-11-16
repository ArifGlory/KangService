package com.tapisdev.kangservice.activity.pengguna

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.model.*
import kotlinx.android.synthetic.main.activity_detail_layanan.*
import kotlinx.android.synthetic.main.activity_detail_layanan_user.*
import java.text.SimpleDateFormat
import java.util.*

class PesanlLayananPerbaikanActivity : BaseActivity() {

    lateinit var layanan : Layanan
    lateinit var i : Intent
    var TAG_SIMPAN = "simpanPerbaikan"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_layanan_user)
        mUserPref = UserPreference(this)

        i = intent
        layanan = i.getSerializableExtra("layanan") as Layanan
        Log.d("layanan"," id : "+layanan.layananId)

        tvnamaLayananUser.setText(layanan.nama)
        tvRincianLayanan.setText(layanan.rincianLayanan)

        tvSend.setOnClickListener {
            checkValidation()
        }
    }

    fun checkValidation(){
        var getAlamat = edAlamatUser.text.toString()
        var getMerkHp = edMerkHP.text.toString()

        if (getAlamat.equals("") || getAlamat.length == 0){
            showErrorMessage("Alamat harus diisi")
        }else if (getMerkHp.equals("") || getMerkHp.length == 0){
            showErrorMessage("Merk HP harus diisi")
        }
        else{
            savePerbaikan(getAlamat,getMerkHp)
        }
    }

    fun savePerbaikan(alamat : String,merkHp : String){
        showLoading(this)
        val date = getCurrentDateTime()
        val dateInString = date.toString("yyyy-MM-dd HH:mm:ss")
        Log.d("dateNow",""+dateInString)

        var idAdmin = layanan.idAdmin
        var idPerbaikan = UUID.randomUUID().toString()

        var perbaikan  = Perbaikan(
            idPerbaikan,
            layanan.layananId,
            idAdmin,
            auth.currentUser?.uid,
            layanan.nama,
            merkHp,
            alamat,
            layanan.harga,
            dateInString,
            "",
            "menunggu konfirmasi"
        )

        perbaikanRef.document(idPerbaikan).set(perbaikan).addOnCompleteListener { task ->
            if (task.isSuccessful){
                //lanjut simpen detail pesanan
                dismissLoading()
                showSuccessMessage("Permintaan Perbaikan Berhasil")
                onBackPressed()
            }else{
                dismissLoading()
                showLongErrorMessage("Penyimpanan data gagal")
                Log.d(TAG_SIMPAN,"err : "+task.exception)
            }
        }

    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
}
