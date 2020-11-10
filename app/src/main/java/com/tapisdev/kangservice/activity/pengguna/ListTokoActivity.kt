package com.tapisdev.kangservice.activity.pengguna

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.adapter.AdapterToko
import com.tapisdev.kangservice.model.UserModel
import kotlinx.android.synthetic.main.activity_list_layanan.*
import kotlinx.android.synthetic.main.activity_list_toko.*
import kotlinx.android.synthetic.main.activity_list_toko.animation_view_layanan

class ListTokoActivity : BaseActivity() {

    var TAG_GET_Layanan = "getToko"
    lateinit var adapter: AdapterToko

    var listToko = ArrayList<UserModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_toko)

        adapter = AdapterToko(listToko)
        rvToko.setHasFixedSize(true)
        rvToko.layoutManager = GridLayoutManager(this, 2)
        rvToko.adapter = adapter

        edSearchToko.doOnTextChanged { text, start, before, count ->
            var query = text.toString().toLowerCase().trim()
            var listSearchToko = ArrayList<UserModel>()

            for (c in 0 until listToko.size){
                var namaToko = listToko.get(c).name.toString().toLowerCase().trim()
                if (namaToko.contains(query)){
                    listSearchToko.add(listToko.get(c))
                }
            }
            Log.d("search"," "+query)

            adapter = AdapterToko(listSearchToko)
            rvToko.layoutManager = GridLayoutManager(this, 2)
            rvToko.adapter = adapter
            adapter.notifyDataSetChanged()
        }


        getDataToko()
    }

    fun getDataToko(){
        userRef.get().addOnSuccessListener { result ->
            listToko.clear()
            //Log.d(TAG_GET_Sparepart," datanya "+result.documents)
            for (document in result){
                //Log.d(TAG_GET_Sparepart, "Datanya : "+document.data)
                var toko: UserModel = document.toObject(UserModel::class.java)
                toko.uId = document.id
                if (toko.jenis.equals("admin")){
                    listToko.add(toko)
                }
            }
            if (listToko.size == 0){
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

    override fun onResume() {
        super.onResume()
        getDataToko()
    }
}
