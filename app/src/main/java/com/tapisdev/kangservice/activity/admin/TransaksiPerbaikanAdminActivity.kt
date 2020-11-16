package com.tapisdev.kangservice.activity.admin

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
import com.tapisdev.kangservice.adapter.AdapterPerbaikan
import com.tapisdev.kangservice.model.Perbaikan
import kotlinx.android.synthetic.main.activity_list_layanan.*
import kotlinx.android.synthetic.main.activity_transaksi_perbaikan_admin.*

class TransaksiPerbaikanAdminActivity : BaseActivity() {

    var TAG_GET = "getPerbaikan"
    lateinit var adapter: AdapterPerbaikan

    var listPerbaikan = ArrayList<Perbaikan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi_perbaikan_admin)

        adapter = AdapterPerbaikan(listPerbaikan)
        rvPerbaikan.setHasFixedSize(true)
        rvPerbaikan.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        rvPerbaikan.adapter = adapter

        edSearchPerbaikan.doOnTextChanged { text, start, before, count ->
            var query = text.toString().toLowerCase().trim()
            var listSearchLayanan = ArrayList<Perbaikan>()

            for (c in 0 until listPerbaikan.size){
                var namaLayanan = listPerbaikan.get(c).namaLayanan.toString().toLowerCase().trim()
                if (namaLayanan.contains(query)){
                    listSearchLayanan.add(listPerbaikan.get(c))
                }
            }

            adapter = AdapterPerbaikan(listSearchLayanan)
            rvPerbaikan.layoutManager = LinearLayoutManager(this)
            rvPerbaikan.adapter = adapter
            adapter.notifyDataSetChanged()
        }


        getDataMyPerbaikan()
    }

    fun getDataMyPerbaikan(){
        perbaikanRef.get().addOnSuccessListener { result ->
            listPerbaikan.clear()
            //Log.d(TAG_GET_Sparepart," datanya "+result.documents)
            for (document in result){
                //Log.d(TAG_GET_Sparepart, "Datanya : "+document.data)
                var perbaikan : Perbaikan = document.toObject(Perbaikan::class.java)
                perbaikan.perbaikanId = document.id
                if (perbaikan.idAdmin.equals(auth.currentUser?.uid)){
                    listPerbaikan.add(perbaikan)
                }
            }
            if (listPerbaikan.size == 0){
                animation_view_perbaikan.setAnimation(R.raw.empty_box)
                animation_view_perbaikan.playAnimation()
                animation_view_perbaikan.loop(false)
            }else{
                animation_view_perbaikan.visibility = View.INVISIBLE
            }
            adapter.notifyDataSetChanged()

        }.addOnFailureListener { exception ->
            showErrorMessage("terjadi kesalahan : "+exception.message)
            Log.d(TAG_GET,"err : "+exception.message)
        }
    }
}
