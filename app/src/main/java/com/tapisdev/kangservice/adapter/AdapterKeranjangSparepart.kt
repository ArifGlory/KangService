package com.tapisdev.kangservice.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.activity.pengguna.CartSparepartActivity
import com.tapisdev.kangservice.model.CartSparepart
import kotlinx.android.synthetic.main.item_keranjang.view.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class AdapterKeranjangSparepart(private val list:ArrayList<CartSparepart>) : RecyclerView.Adapter<AdapterKeranjangSparepart.Holder>(){
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_keranjang,parent,false))
    }

    override fun getItemCount(): Int = list?.size

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val nf = NumberFormat.getNumberInstance(Locale.GERMAN)
        val df = nf as DecimalFormat

        holder.view.tvName.text = list?.get(position)?.nama
        holder.view.tvJumlahDipesan.text = "Jumlah Dipesan : "+list?.get(position)?.jumlah
        holder.view.tvPrice.text = "Rp. "+df.format(list?.get(position)?.harga)

        Glide.with(holder.view.ivKeranjang.context)
            .load(list?.get(position)?.foto)
            .into(holder.view.ivKeranjang)

        holder.view.ivDeleteCartItem.setOnClickListener {
            list?.removeAt(position)
            if (holder.view.ivKeranjang.context is CartSparepartActivity) {
                (holder.view.ivKeranjang.context as CartSparepartActivity).refreshList()
            }
        }


    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)


}