package com.tapisdev.kangservice.activity.pengguna

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tapisdev.kangservice.R
import kotlinx.android.synthetic.main.activity_detail_toko.*

class DetailTokoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_toko)

        ivBack.setOnClickListener {
            onBackPressed()
        }
    }
}
