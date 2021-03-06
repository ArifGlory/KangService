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
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.model.Perbaikan
import com.tapisdev.kangservice.model.UserModel
import kotlinx.android.synthetic.main.activity_detail_perbaikan.*
import kotlinx.android.synthetic.main.activity_detail_perbaikan.tvAlamatAdmin
import kotlinx.android.synthetic.main.activity_detail_perbaikan.tvNamaPemesan
import kotlinx.android.synthetic.main.activity_detail_perbaikan.tvStatusAdmin
import kotlinx.android.synthetic.main.activity_detail_perbaikan.tvTanggalAdmin
import kotlinx.android.synthetic.main.activity_detail_perbaikan.tvTotalPriceAdmin
import kotlinx.android.synthetic.main.activity_detail_perbaikan.tvUbahStatus
import kotlinx.android.synthetic.main.activity_detail_transaksi_sparepart_admin.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class DetailPerbaikanActivity : BaseActivity() {

    lateinit var perbaikan : Perbaikan
    lateinit var i : Intent
    lateinit var user : UserModel

    var TAG_GET_DETAILPESANAN = "detailpesananGET"
    var selectedStatus = "none"
    var TAG_GET_USER = "userGET"
    var TAG_UBAH_STATUS = "ubahStatus"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_perbaikan)

        i = intent
        perbaikan = i.getSerializableExtra("perbaikan") as Perbaikan
        Log.d("perbaikan"," id : "+perbaikan.layananId)

        ivBackPerbaikan.setOnClickListener {
            onBackPressed()
        }
        tvUbahStatus.setOnClickListener {
            showDialogUbahStatus()
        }


        updateUI()
        getDataUser()
    }

    fun updateUI(){
        tvMerkHp.setText(perbaikan.merkHp)
        tvTanggalAdmin.setText("Tanggal pesan : "+perbaikan.tanggalMulai?.let { convertTanggal(it) })
        tvAlamatAdmin.setText("Alamat : "+perbaikan.alamatUser)
        tvStatusAdmin.setText(perbaikan.status)

        val nf = NumberFormat.getNumberInstance(Locale.GERMAN)
        val df = nf as DecimalFormat
        tvTotalPriceAdmin.setText(perbaikan.namaLayanan+"\n Rp. "+df.format(perbaikan.harga))

        if (perbaikan.status.equals("Service selesai")){
            tvUbahStatus.visibility = View.INVISIBLE
            tvUbahStatus.isEnabled = false
        }
    }

    fun getDataUser(){
        userRef.document(perbaikan.idUser.toString()).get().addOnCompleteListener { task ->
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

    fun showDialogUbahStatus(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dlg_ubah_status_service)
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
                if (selected.equals("Pilih Status Service")){
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
        perbaikanRef.document(perbaikan.perbaikanId.toString()).update("status",newStatus).addOnCompleteListener { task ->
            dismissLoading()
            if (task.isSuccessful){
                perbaikan.status = newStatus
                tvStatusAdmin.setText(perbaikan.status)
                showSuccessMessage("Status berhasil diubah")
                updateUI()
                //onBackPressed()
            }else{
                showLongErrorMessage("terjadi kesalahan, coba lagi nanti")
                Log.d(TAG_UBAH_STATUS,"err : "+task.exception)
            }
        }
    }

    fun convertTanggal(tanggal : String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd")
        //val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        val output = formatter.format(parser.parse(tanggal))

        return output
    }
}
