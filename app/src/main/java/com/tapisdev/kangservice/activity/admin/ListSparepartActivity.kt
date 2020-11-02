package com.tapisdev.kangservice.activity.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.adapter.AdapterSparepart
import com.tapisdev.kangservice.model.Sparepart
import com.tapisdev.kangservice.model.UserPreference
import kotlinx.android.synthetic.main.activity_list_sparepart.*

class ListSparepartActivity : BaseActivity() {

    lateinit var edSearchSparepart : EditText
    var TAG_GET_Sparepart = "getSparepart"
    lateinit var adapter: AdapterSparepart

    var listSparepart = ArrayList<Sparepart>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_sparepart)
        mUserPref = UserPreference(this)

        adapter = AdapterSparepart(listSparepart)
        rvSparepart.setHasFixedSize(true)
        rvSparepart.layoutManager = LinearLayoutManager(this)
        rvSparepart.adapter = adapter

        fab.setOnClickListener {
            val i = Intent(this,AddSparepartActivity::class.java)
            startActivity(i)
        }

    }
}
