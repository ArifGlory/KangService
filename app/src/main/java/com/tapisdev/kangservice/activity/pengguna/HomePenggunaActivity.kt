package com.tapisdev.kangservice.activity.pengguna

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.R
import kotlinx.android.synthetic.main.activity_home_pengguna.*

class HomePenggunaActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_pengguna)

        rlToko.setOnClickListener {
            val i = Intent(this, ListTokoActivity::class.java)
            startActivity(i)
        }
    }
}
