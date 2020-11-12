package com.tapisdev.kangservice.activity.pengguna

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.model.SharedVariable
import com.tapisdev.kangservice.model.UserPreference
import kotlinx.android.synthetic.main.activity_home_pengguna.*

class HomePenggunaActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_pengguna)
        mUserPref = UserPreference(this)

        rlToko.setOnClickListener {
            val i = Intent(this, ListTokoActivity::class.java)
            startActivity(i)
        }
        rlProfilUser.setOnClickListener {
            val i = Intent(this, ProfilUserActivity::class.java)
            startActivity(i)
        }
        rlKeranjang.setOnClickListener {
            val i = Intent(this, CartSparepartActivity::class.java)
            startActivity(i)
        }

        updateUI()
    }

    fun updateUI(){
        tvNamaUser.setText(mUserPref.getName())
        if (mUserPref.getFoto().equals("")){
            ivProfilUser.setImageResource(R.drawable.ic_user)
        }else{
            Glide.with(this)
                .load(mUserPref.getFoto())
                .into(ivProfilUser)
        }
        tvStatusKeranjang.setText("Total di Keranjang : "+SharedVariable.listCart.size)
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }
}
