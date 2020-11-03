package com.tapisdev.kangservice.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.opengl.GLDebugHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.activity.admin.DetailSparepartActivity
import com.tapisdev.kangservice.model.Layanan
import com.tapisdev.kangservice.model.Sparepart
import kotlinx.android.synthetic.main.item_layanan.view.*
import kotlinx.android.synthetic.main.item_sparepart.view.*
import kotlinx.android.synthetic.main.item_sparepart.view.tvHarga
import java.io.Serializable

class AdapterLayanan(private val list:ArrayList<Layanan>) : RecyclerView.Adapter<AdapterLayanan.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_layanan,parent,false))
    }

    override fun getItemCount(): Int = list?.size

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.view.tvNamaLayanan.text = list?.get(position)?.nama
        holder.view.tvHarga.text ="Rp. " +list?.get(position)?.harga
        holder.view.tvEstimasi.text ="Estimasi  " +list?.get(position)?.estimasiWaktu + " hari"

        holder.view.lineLayanan.setOnClickListener {
            Log.d("adapterIsi",""+list.get(position).toString())
            /*val i = Intent(holder.view.lineSparepart.context, DetailSparepartActivity::class.java)
            i.putExtra("sparepart",list.get(position) as Serializable)
            holder.view.lineSparepart.context.startActivity(i)*/
        }

    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

}