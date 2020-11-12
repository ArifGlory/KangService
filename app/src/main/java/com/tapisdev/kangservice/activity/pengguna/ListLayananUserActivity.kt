package com.tapisdev.kangservice.activity.pengguna

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.adapter.AdapterLayanan
import com.tapisdev.kangservice.adapter.AdapterLayananUser
import com.tapisdev.kangservice.model.Layanan
import kotlinx.android.synthetic.main.activity_list_layanan.*
import kotlinx.android.synthetic.main.activity_list_layanan_user.*
import kotlinx.android.synthetic.main.activity_list_layanan_user.animation_view_layanan

class ListLayananUserActivity : BaseActivity() {

    var TAG_GET_Layanan = "getLayanan"
    lateinit var adapter: AdapterLayananUser
    var listLayanan = ArrayList<Layanan>()

    var idToko = ""
    lateinit var i : Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_layanan_user)

        i = intent
        idToko = i.getStringExtra("idToko") as String

        adapter = AdapterLayananUser(listLayanan)
        rvLayananUser.setHasFixedSize(true)
        rvLayananUser.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        rvLayananUser.adapter = adapter

        edSearchLayananUser.doOnTextChanged { text, start, before, count ->
            var query = text.toString().toLowerCase().trim()
            var listSearchLayanan = ArrayList<Layanan>()

            for (c in 0 until listLayanan.size){
                var namaLayanan = listLayanan.get(c).nama.toString().toLowerCase().trim()
                if (namaLayanan.contains(query)){
                    listSearchLayanan.add(listLayanan.get(c))
                }
            }

            adapter = AdapterLayananUser(listSearchLayanan)
            rvLayananUser.layoutManager = LinearLayoutManager(this)
            rvLayananUser.adapter = adapter
            adapter.notifyDataSetChanged()
        }



        getDataMyLayanan()
    }

    fun getDataMyLayanan(){
        layananRef.get().addOnSuccessListener { result ->
            listLayanan.clear()
            //Log.d(TAG_GET_Sparepart," datanya "+result.documents)
            for (document in result){
                //Log.d(TAG_GET_Sparepart, "Datanya : "+document.data)
                var layanan : Layanan = document.toObject(Layanan::class.java)
                layanan.layananId = document.id
                if (layanan.idAdmin.equals(idToko)){
                    listLayanan.add(layanan)
                }
            }
            if (listLayanan.size == 0){
                animation_view_layanan.setAnimation(R.raw.empty_box)
                animation_view_layanan.playAnimation()
                animation_view_layanan.loop(false)
            }else{
                animation_view_layanan.visibility = View.INVISIBLE
            }
            adapter.notifyDataSetChanged()

        }.addOnFailureListener { exception ->
            showErrorMessage("terjadi kesalahan : "+exception.message)
            Log.d(TAG_GET_Layanan,"err : "+exception.message)
        }
    }
}
