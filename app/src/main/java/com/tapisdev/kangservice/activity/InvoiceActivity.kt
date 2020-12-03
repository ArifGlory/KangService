package com.tapisdev.kangservice.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.adapter.AdapterDetailPesanan
import com.tapisdev.kangservice.model.CartSparepart
import com.tapisdev.kangservice.model.Pesanan
import com.tapisdev.kangservice.model.UserModel
import kotlinx.android.synthetic.main.activity_invoice.*
import java.io.File
import java.io.FileOutputStream
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class InvoiceActivity : BaseActivity() {

    lateinit var i : Intent
    lateinit var pesanan : Pesanan
    lateinit var toko : UserModel
    lateinit var user : UserModel

    var listCart = java.util.ArrayList<CartSparepart>()
    lateinit var adapter: AdapterDetailPesanan
    var TAG_GET_DETAILPESANAN = "detailpesananGET"
    var TAG_GET_USER = "penyediaGET"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice)

        i = intent
        pesanan = i.getSerializableExtra("pesanan") as Pesanan
        Log.d("pesanan"," id : "+pesanan.pesananId)

        val rootView = window.decorView.findViewById<View>(R.id.rootView)

        adapter = AdapterDetailPesanan(listCart)
        rvDetailPesananInvoice.setHasFixedSize(true)
        rvDetailPesananInvoice.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        rvDetailPesananInvoice.adapter = adapter

        updateUI()
        getDataUser()
        getDataToko()
        getDataPesanan()
    }

    fun updateUI(){
        tvTanggalNota.setText("Tanggal pesan : "+pesanan.tanggalPesan?.let { convertDate(it) })
        tvAlamatInvoice.setText(""+pesanan.alamat)
        tvKodePesanan.setText("#"+pesanan.pesananId!!.take(8))
    }

    fun getDataUser(){
        userRef.document(pesanan.idUser.toString()).get().addOnCompleteListener { task ->
            if (task.isSuccessful){
                val document = task.result
                if (document != null) {
                    if (document.exists()) {
                        Log.d(TAG_GET_USER, "DocumentSnapshot data: " + document.data)
                        //convert doc to object
                        user = document.toObject(UserModel::class.java)!!
                        tvNamaPemesan.setText(user.name)

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

    fun getDataToko(){
        userRef.document(pesanan.idAdmin.toString()).get().addOnCompleteListener { task ->
            if (task.isSuccessful){
                val document = task.result
                if (document != null) {
                    if (document.exists()) {
                        Log.d(TAG_GET_USER, "DocumentSnapshot data: " + document.data)
                        //convert doc to object
                        toko = document.toObject(UserModel::class.java)!!
                        tvNamaToko.setText(toko.name)

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
        tvTotalPriceInvoice.setText("Rp. "+df.format(totalPrice))
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

}
