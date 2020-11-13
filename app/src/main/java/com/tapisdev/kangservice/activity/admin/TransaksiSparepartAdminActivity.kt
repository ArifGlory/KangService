package com.tapisdev.kangservice.activity.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tapisdev.PesananPesanan.adapter.AdapterPesananUser
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.model.Pesanan
import kotlinx.android.synthetic.main.activity_transaksi_sparepart.*
import kotlinx.android.synthetic.main.activity_transaksi_sparepart_admin.*

class TransaksiSparepartAdminActivity : BaseActivity() {

    lateinit var adapter: AdapterPesananUser

    var TAG_GET = "getTransaksi"
    var listPesanan = ArrayList<Pesanan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi_sparepart_admin)

        adapter = AdapterPesananUser(listPesanan)
        rvPesananAdmin.setHasFixedSize(true)
        rvPesananAdmin.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        rvPesananAdmin.adapter = adapter

        edSearchPesananAdmin.doOnTextChanged { text, start, before, count ->
            var query = text.toString().toLowerCase().trim()
            var listSearchPesanan = ArrayList<Pesanan>()

            for (c in 0 until listPesanan.size){
                var tanggal = listPesanan.get(c).tanggalPesan.toString().toLowerCase().trim()
                var alamat = listPesanan.get(c).alamat.toString().toLowerCase().trim()

                tanggal = convertDate(tanggal)

                if (tanggal.contains(query) || alamat.contains(query)){
                    listSearchPesanan.add(listPesanan.get(c))
                }
            }

            adapter = AdapterPesananUser(listSearchPesanan)
            rvPesananAdmin.layoutManager = LinearLayoutManager(this)
            rvPesananAdmin.adapter = adapter
            adapter.notifyDataSetChanged()
        }
        
        getDataMyPesanan()
    }

    fun getDataMyPesanan(){
        pesanananRef.get().addOnSuccessListener { result ->
            listPesanan.clear()
            //Log.d(TAG_GET_Sparepart," datanya "+result.documents)
            for (document in result){
                //Log.d(TAG_GET_Sparepart, "Datanya : "+document.data)
                var pesanan : Pesanan = document.toObject(Pesanan::class.java)
                pesanan.pesananId = document.id
                if (pesanan.idAdmin.equals(auth.currentUser?.uid)){
                    listPesanan.add(pesanan)
                }
            }
            if (listPesanan.size == 0){
                animation_view_admin.setAnimation(R.raw.empty_box)
                animation_view_admin.playAnimation()
                animation_view_admin.loop(false)
            }else{
                animation_view_admin.visibility = View.INVISIBLE
            }
            adapter.notifyDataSetChanged()

        }.addOnFailureListener { exception ->
            showErrorMessage("terjadi kesalahan : "+exception.message)
            Log.d(TAG_GET,"err : "+exception.message)
        }
    }

    override fun onResume() {
        super.onResume()
        getDataMyPesanan()
    }
}
