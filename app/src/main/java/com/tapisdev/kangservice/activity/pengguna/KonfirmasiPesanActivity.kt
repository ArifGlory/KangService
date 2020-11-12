package com.tapisdev.kangservice.activity.pengguna

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.model.Pesanan
import com.tapisdev.kangservice.model.SharedVariable
import kotlinx.android.synthetic.main.activity_konfirmasi_pesan.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class KonfirmasiPesanActivity : BaseActivity() {

    var TAG_SIMPAN_PESANAN = "simpanPesanan"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_konfirmasi_pesan)

        val nf = NumberFormat.getNumberInstance(Locale.GERMAN)
        val df = nf as DecimalFormat
        tvTotalPrice.setText("Rp. "+ df.format(SharedVariable.totalKeranjang))

        tvPesan.setOnClickListener {
            checkValidation()
        }
    }

    fun checkValidation(){
        var getAlamat = edAlamat.text.toString()

        if (getAlamat.equals("") || getAlamat.length == 0){
            showErrorMessage("Alamat harus diisi")
        }else{
            savePesanan(getAlamat)
        }
    }

    fun savePesanan(alamat : String){
        showLoading(this)
        val date = getCurrentDateTime()
        val dateInString = date.toString("yyyy-MM-dd HH:mm:ss")
        Log.d("dateNow",""+dateInString)

        var idAdmin = SharedVariable.listCart.get(0).idAdmin
        var idPesanan = UUID.randomUUID().toString()

        var pesanan  = Pesanan(idPesanan,
            idAdmin,
            auth.currentUser?.uid,
            alamat,
            dateInString,
            "",
            "menunggu konfirmasi"
        )
        pesanananRef.document(idPesanan).set(pesanan).addOnCompleteListener { task ->
            if (task.isSuccessful){
                //lanjut simpen detail pesanan
                for (i in 0 until SharedVariable.listCart.size){
                    if (i == SharedVariable.listCart.size - 1){
                        //jika udah cart yang terakhir
                        var cart = SharedVariable.listCart.get(i)
                        cart.idPesanan = idPesanan

                        detailpesananRef.document(cart.cartId.toString()).set(cart).addOnCompleteListener { task ->
                            if (task.isSuccessful){
                                dismissLoading()
                                showSuccessMessage("Pemesanan Berhasil")
                                SharedVariable.listCart.clear()
                                val i = Intent(applicationContext,HomePenggunaActivity::class.java)
                                startActivity(i)
                                finish()
                            }else{
                                dismissLoading()
                                showLongErrorMessage("Penyimpanan detail pesanan gagal")
                                Log.d(TAG_SIMPAN_PESANAN,"err : "+task.exception)
                            }
                        }
                    }else{
                        var cart = SharedVariable.listCart.get(i)
                        cart.idPesanan = idPesanan
                        detailpesananRef.document(cart.cartId.toString()).set(cart)
                    }
                }

            }else{
                dismissLoading()
                showLongErrorMessage("Penyimpanan data gagal")
                Log.d(TAG_SIMPAN_PESANAN,"err : "+task.exception)
            }
        }

    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
}
