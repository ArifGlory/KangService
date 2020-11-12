package com.tapisdev.kangservice.activity.pengguna

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
import com.tapisdev.kangservice.model.Layanan
import com.tapisdev.kangservice.model.Pesanan
import kotlinx.android.synthetic.main.activity_list_layanan_user.*
import kotlinx.android.synthetic.main.activity_transaksi_sparepart.*

class TransaksiSparepartActivity : BaseActivity() {

    lateinit var adapter: AdapterPesananUser

    var TAG_GET = "getTransaksi"
    var listPesanan = ArrayList<Pesanan>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi_sparepart)

        adapter = AdapterPesananUser(listPesanan)
        rvPesananUser.setHasFixedSize(true)
        rvPesananUser.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        rvPesananUser.adapter = adapter

        edSearchPesananUser.doOnTextChanged { text, start, before, count ->
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
            rvPesananUser.layoutManager = LinearLayoutManager(this)
            rvPesananUser.adapter = adapter
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
                if (pesanan.idUser.equals(auth.currentUser?.uid)){
                    listPesanan.add(pesanan)
                }
            }
            if (listPesanan.size == 0){
                animation_view_pesanan.setAnimation(R.raw.empty_box)
                animation_view_pesanan.playAnimation()
                animation_view_pesanan.loop(false)
            }else{
                animation_view_pesanan.visibility = View.INVISIBLE
            }
            adapter.notifyDataSetChanged()

        }.addOnFailureListener { exception ->
            showErrorMessage("terjadi kesalahan : "+exception.message)
            Log.d(TAG_GET,"err : "+exception.message)
        }
    }
}
