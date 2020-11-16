package com.tapisdev.kangservice.activity.pengguna

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.activity.admin.TransaksiPerbaikanAdminActivity
import com.tapisdev.kangservice.activity.admin.TransaksiSparepartAdminActivity
import com.tapisdev.kangservice.model.UserPreference
import kotlinx.android.synthetic.main.activity_jenis_transaksi.*

class JenisTransaksiActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jenis_transaksi)
        mUserPref = UserPreference(this)

        ivBack.setOnClickListener {
            onBackPressed()
        }
        tvGoToTransSparepart.setOnClickListener {
            if (mUserPref.getJenisUser().equals("admin")){
                val i = Intent(this,TransaksiSparepartAdminActivity::class.java)
                startActivity(i)
            }else{
                val i = Intent(this,TransaksiSparepartActivity::class.java)
                startActivity(i)
            }

        }
        tvGoToTransService.setOnClickListener {
            if (mUserPref.getJenisUser().equals("admin")){
                val i = Intent(this,TransaksiPerbaikanAdminActivity::class.java)
                startActivity(i)
            }else{
                val i = Intent(this,TransaksiPerbaikanActivity::class.java)
                startActivity(i)
            }

        }
    }
}
