package com.tapisdev.kangservice.activity.pengguna

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.adapter.AdapterSparepart
import com.tapisdev.kangservice.model.Sparepart
import com.tapisdev.kangservice.model.UserModel
import kotlinx.android.synthetic.main.activity_detail_toko.*
import kotlinx.android.synthetic.main.activity_detail_toko.rvSparepart
import kotlinx.android.synthetic.main.activity_list_sparepart.*

class DetailTokoActivity : BaseActivity() {

    lateinit var toko : UserModel
    lateinit var i : Intent
    var TAG_GET_Sparepart = "getSparepart"
    lateinit var adapter: AdapterSparepart

    var listSparepart = ArrayList<Sparepart>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_toko)
        i = intent
        toko = i.getSerializableExtra("toko") as UserModel
        Log.d("toko"," id : "+toko.uId)

        adapter = AdapterSparepart(listSparepart)
        rvSparepart.setHasFixedSize(true)
        rvSparepart.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        rvSparepart.adapter = adapter

        ivBack.setOnClickListener {
            onBackPressed()
        }


        updateUI()
        getDataMySparepart()
    }

    fun updateUI(){
        tvNamaToko.setText(toko.name)
        tvLocation.setText(toko.alamat)

        Glide.with(this)
            .load(toko.foto)
            .into(ivToko)
    }

    fun getDataMySparepart(){
        sparepartRef.get().addOnSuccessListener { result ->
            listSparepart.clear()
            //Log.d(TAG_GET_Sparepart," datanya "+result.documents)
            for (document in result){
                //Log.d(TAG_GET_Sparepart, "Datanya : "+document.data)
                var sparepart : Sparepart = document.toObject(Sparepart::class.java)
                sparepart.sparepartId = document.id
                if (sparepart.idAdmin.equals(toko.uId)){
                    listSparepart.add(sparepart)
                }
            }
            if (listSparepart.size == 0){
                animation_view_toko.setAnimation(R.raw.empty_box)
                animation_view_toko.playAnimation()
                animation_view_toko.loop(false)
            }else{
                animation_view_toko.visibility = View.INVISIBLE
            }
            adapter.notifyDataSetChanged()

        }.addOnFailureListener { exception ->
            showErrorMessage("terjadi kesalahan : "+exception.message)
            Log.d(TAG_GET_Sparepart,"err : "+exception.message)
        }
    }

}
