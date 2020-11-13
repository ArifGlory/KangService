package com.tapisdev.kangservice.activity.admin

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.activity.pengguna.BuktiBayarActivity
import com.tapisdev.kangservice.adapter.AdapterDetailPesanan
import com.tapisdev.kangservice.model.CartSparepart
import com.tapisdev.kangservice.model.Pesanan
import com.tapisdev.kangservice.model.UserModel
import kotlinx.android.synthetic.main.activity_detail_transaksi_sparepart.*
import kotlinx.android.synthetic.main.activity_detail_transaksi_sparepart_admin.*
import kotlinx.android.synthetic.main.activity_detail_transaksi_sparepart_admin.rvDetailPesanan
import java.io.Serializable
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class DetailTransaksiSparepartAdminActivity : BaseActivity() {

    lateinit var i : Intent
    lateinit var pesanan : Pesanan
    lateinit var user : UserModel


    var listCart = java.util.ArrayList<CartSparepart>()
    lateinit var adapter: AdapterDetailPesanan

    var TAG_GET_DETAILPESANAN = "detailpesananGET"
    var selectedStatus = "none"
    var TAG_GET_USER = "userGET"
    var TAG_UBAH_STATUS = "ubahStatus"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_transaksi_sparepart_admin)

        i = intent
        pesanan = i.getSerializableExtra("pesanan") as Pesanan
        Log.d("pesanan"," id : "+pesanan.pesananId)

        adapter = AdapterDetailPesanan(listCart)
        rvDetailPesanan.setHasFixedSize(true)
        rvDetailPesanan.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        rvDetailPesanan.adapter = adapter

        ivBackDetail.setOnClickListener {
            onBackPressed()
        }
        tvUbahStatus.setOnClickListener {
            showDialogUbahStatus()
        }
        tvBuktiBayarAdmin.setOnClickListener {
            val i = Intent(this, BuktiBayarActivity::class.java)
            i.putExtra("pesanan",pesanan as Serializable)
            startActivity(i)
        }

        updateUI()
        getDataUser()
        getDataPesanan()
    }

    fun showDialogUbahStatus(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dlg_ubah_status_pesan)
        val spStatus  = dialog.findViewById(R.id.spStatus) as Spinner
        val tvCancel = dialog.findViewById(R.id.tvCancel) as TextView
        val tvAdd = dialog.findViewById(R.id.tvAdd) as TextView

        spStatus.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                Log.d(TAG_UBAH_STATUS, "jenis nya "+ parent?.getItemAtPosition(position).toString())
                var selected = parent?.getItemAtPosition(position).toString()
                if (selected.equals("Pilih Status Pesanan")){
                    selectedStatus = "none"
                }else{
                    selectedStatus = selected
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        tvCancel.setOnClickListener {
            dialog.dismiss()
        }
        tvAdd.setOnClickListener {
            if(selectedStatus.equals("none")){
                showErrorMessage("anda belum memilih status")
            }else{
                updateStatusPesanan(selectedStatus)
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    fun updateStatusPesanan(newStatus : String){
        showLoading(this)
        pesanananRef.document(pesanan.pesananId.toString()).update("status",newStatus).addOnCompleteListener { task ->
            dismissLoading()
            if (task.isSuccessful){
                pesanan.status = newStatus
                tvStatusAdmin.setText(pesanan.status)
                showSuccessMessage("Status berhasil diubah")
                updateUI()
                //onBackPressed()
            }else{
                showLongErrorMessage("terjadi kesalahan, coba lagi nanti")
                Log.d(TAG_UBAH_STATUS,"err : "+task.exception)
            }
        }
    }

    fun updateUI(){
        tvTanggalAdmin.setText("Tanggal pesan : "+pesanan.tanggalPesan?.let { convertDate(it) })
        tvAlamatAdmin.setText("Alamat : "+pesanan.alamat)
        tvStatusAdmin.setText(pesanan.status)

        if (pesanan.status.equals("pesanan selesai")){
            tvUbahStatus.visibility = View.INVISIBLE
            tvUbahStatus.isEnabled = false
        }
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
                        tvAlamatPemesan.setText("Telepon : "+user.phone)

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
                if (cart.idAdmin.equals(auth.currentUser?.uid) && cart.idUser.equals(pesanan.idUser)){
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
        tvTotalPriceAdmin.setText("Total Bayar : Rp. "+df.format(totalPrice))
    }
}
