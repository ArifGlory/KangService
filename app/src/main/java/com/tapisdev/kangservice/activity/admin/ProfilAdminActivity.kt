package com.tapisdev.kangservice.activity.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.MainActivity
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.model.UserPreference
import kotlinx.android.synthetic.main.activity_profil.*

class ProfilAdminActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)
        mUserPref = UserPreference(this)

        tvLogout.setOnClickListener {
            auth.signOut()
            clearSession()
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }

    fun clearSession(){
        mUserPref.saveName("")
        mUserPref.saveEmail("")
        mUserPref.saveFoto("")
        mUserPref.saveJenisUser("none")
        mUserPref.savePhone("")
        mUserPref.saveAlamat("none")
        mUserPref.saveLatlon("")
    }
}
