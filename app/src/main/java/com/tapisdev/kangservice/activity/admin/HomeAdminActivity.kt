package com.tapisdev.kangservice.activity.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.MainActivity
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.model.UserPreference
import kotlinx.android.synthetic.main.activity_home_admin.*

class HomeAdminActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)
        mUserPref = UserPreference(this)

        rlProfil.setOnClickListener {
            val i = Intent(this, ProfilAdminActivity::class.java)
            startActivity(i)
        }

        updateUI()
    }

    fun updateUI(){
        tvNamaToko.setText(mUserPref.getName())
        if (mUserPref.getDeskripsi().equals("") || mUserPref.getDeskripsi()?.length == 0){
            tvDeskripsiToko.setText("Deskripsi toko belum di update")
        }else{
            tvDeskripsiToko.setText(mUserPref.getDeskripsi())
        }

        if (!mUserPref.getFoto().equals("")){
            Glide.with(this)
                .load(mUserPref.getFoto())
                .into(ivProfilToko)
        }
    }
}
