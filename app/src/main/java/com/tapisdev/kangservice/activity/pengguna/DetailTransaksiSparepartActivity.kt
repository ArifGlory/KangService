package com.tapisdev.kangservice.activity.pengguna

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.activity.InvoiceActivity
import com.tapisdev.kangservice.adapter.AdapterDetailPesanan
import com.tapisdev.kangservice.adapter.AdapterSparepart
import com.tapisdev.kangservice.model.CartSparepart
import com.tapisdev.kangservice.model.Pesanan
import com.tapisdev.kangservice.model.Sparepart
import com.tapisdev.kangservice.model.UserModel
import com.tapisdev.kangservice.util.PermissionHelper
import kotlinx.android.synthetic.main.activity_detail_toko.*
import kotlinx.android.synthetic.main.activity_detail_transaksi_sparepart.*
import kotlinx.android.synthetic.main.activity_detail_transaksi_sparepart.ivBack
import java.io.Serializable
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class DetailTransaksiSparepartActivity : BaseActivity(){

    lateinit var i : Intent
    lateinit var pesanan : Pesanan
    lateinit var toko : UserModel


    var listCart = java.util.ArrayList<CartSparepart>()
    lateinit var adapter: AdapterDetailPesanan

    var TAG_GET_DETAILPESANAN = "detailpesananGET"

    var TAG_GET_USER = "penyediaGET"
    var fotoBitmap : Bitmap? = null
    private var fileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_transaksi_sparepart)

        i = intent
        pesanan = i.getSerializableExtra("pesanan") as Pesanan
        Log.d("pesanan"," id : "+pesanan.pesananId)

        adapter = AdapterDetailPesanan(listCart)
        rvDetailPesanan.setHasFixedSize(true)
        rvDetailPesanan.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        rvDetailPesanan.adapter = adapter

        ivBack.setOnClickListener {
            onBackPressed()
        }
        tvBuktiBayar.setOnClickListener {
            val i = Intent(this,BuktiBayarActivity::class.java)
            i.putExtra("pesanan",pesanan as Serializable)
            startActivity(i)
        }
        tvKeInvoice.setOnClickListener {
            val i = Intent(this,InvoiceActivity::class.java)
            i.putExtra("pesanan",pesanan as Serializable)
            startActivity(i)
        }

        updateUI()
        getDataToko()
        getDataPesanan()
    }

    fun getDataToko(){
        userRef.document(pesanan.idAdmin.toString()).get().addOnCompleteListener { task ->
            if (task.isSuccessful){
                val document = task.result
                if (document != null) {
                    if (document.exists()) {
                        Log.d(TAG_GET_USER, "DocumentSnapshot data: " + document.data)
                        //convert doc to object
                        toko = document.toObject(UserModel::class.java)!!
                        tvToko.setText(toko.name)
                        tvDeskripsiPenyedia.setText(toko.deskripsi)

                    } else {
                        Log.d(TAG_GET_USER, "No such document")
                    }
                }
            }else{
                showErrorMessage("Pengguna tidak ditemukan")
                Log.d(TAG_GET_USER,"err : "+task.exception)
            }
        }
    }

    fun getDataPesanan(){
        detailpesananRef.get().addOnSuccessListener { result ->
            listCart.clear()
            //Log.d(TAG_GET_CATERING," datanya "+result.documents)
            for (document in result){
                //Log.d(TAG_GET_CATERING, "Datanya : "+document.data)
                var cart : CartSparepart = document.toObject(CartSparepart::class.java)
                if (cart.idPesanan.equals(pesanan.pesananId)){
                    listCart.add(cart)
                }
            }
            countTotalPrice()
            adapter.notifyDataSetChanged()

        }.addOnFailureListener { exception ->
            showErrorMessage("terjadi kesalahan : "+exception.message)
            Log.d(TAG_GET_DETAILPESANAN,"err : "+exception.message)
        }
    }

    fun countTotalPrice(){
        var totalPrice = 0
        val nf = NumberFormat.getNumberInstance(Locale.GERMAN)
        val df = nf as DecimalFormat

        for (c in 0 until listCart.size){
            var subtotal = listCart.get(c).harga?.times(listCart.get(c).jumlah!!)
            if (subtotal != null) {
                totalPrice += subtotal
            }
        }
        tvTotalPrice.setText("Total Bayar : Rp. "+df.format(totalPrice))
    }

    fun updateUI(){
        tvTanggal.setText("Tanggal pesan : "+pesanan.tanggalPesan?.let { convertDate(it) })
        tvAlamat.setText("Alamat : "+pesanan.alamat)
        tvStatus.setText(pesanan.status)

        if(pesanan.status.equals("menunggu konfirmasi") || pesanan.status.equals("pesanan ditolak")){
            tvKeInvoice.visibility = View.INVISIBLE
        }
    }

}
