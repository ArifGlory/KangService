package com.tapisdev.kangservice.activity.pengguna

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.R
import kotlinx.android.synthetic.main.activity_jenis_transaksi.*

class JenisTransaksiActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jenis_transaksi)

        ivBack.setOnClickListener {
            onBackPressed()
        }
        tvGoToTransSparepart.setOnClickListener {
            val i = Intent(this,TransaksiSparepartActivity::class.java)
            startActivity(i)
        }
        tvGoToTransService.setOnClickListener {

        }
    }
}
