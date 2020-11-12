package com.tapisdev.kangservice.activity.pengguna

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.adapter.AdapterKeranjangSparepart
import com.tapisdev.kangservice.model.SharedVariable
import kotlinx.android.synthetic.main.activity_cart_sparepart.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class CartSparepartActivity : BaseActivity() {

    lateinit var adapter: AdapterKeranjangSparepart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_sparepart)

        adapter = AdapterKeranjangSparepart(SharedVariable.listCart)

        rvKeranjang.setHasFixedSize(true)
        rvKeranjang.layoutManager = LinearLayoutManager(this)
        rvKeranjang.adapter = adapter

        if (SharedVariable.listCart.size == 0){
            animation_view.setAnimation(R.raw.empty_box)
            animation_view.playAnimation()
            animation_view.loop(false)
        }else{
            animation_view.visibility = View.INVISIBLE
            adapter.notifyDataSetChanged()
        }


        ivBack.setOnClickListener {
            onBackPressed()
        }
        tvGoToMakeOrder.setOnClickListener {
            if (SharedVariable.listCart.size < 1){
                showErrorMessage("Anda belom memilih sparepart")
            }else{
                val i = Intent(applicationContext, KonfirmasiPesanActivity::class.java)
                startActivity(i)
            }

        }

        countTotal()
    }

    fun countTotal(){
        if(SharedVariable.listCart.size == 0){
            tvPriceTotal.setText("Total : Rp. 0")
            tvPriceTotal.visibility = View.INVISIBLE
            tvGoToMakeOrder.visibility = View.INVISIBLE
        }else{
            var total = 0;
            for (i in 0 until SharedVariable.listCart.size){
                Log.d("isiCart",""+SharedVariable.listCart.get(i).toString())
                var subtotal = SharedVariable.listCart.get(i).harga?.times(SharedVariable.listCart.get(i).jumlah!!)
                if (subtotal != null) {
                    total += subtotal
                }
            }
            val nf = NumberFormat.getNumberInstance(Locale.GERMAN)
            val df = nf as DecimalFormat

            SharedVariable.totalKeranjang = total
            tvPriceTotal.setText("Total : Rp. "+df.format(total))
            tvGoToMakeOrder.visibility = View.VISIBLE
        }
    }

    fun refreshList(){
        adapter.notifyDataSetChanged()
        countTotal()
    }
}
