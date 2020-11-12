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
import com.tapisdev.kangservice.activity.admin.DetailLayananActivity
import com.tapisdev.kangservice.activity.admin.DetailSparepartActivity
import com.tapisdev.kangservice.model.Layanan
import com.tapisdev.kangservice.model.Sparepart
import com.tapisdev.kangservice.model.UserPreference
import kotlinx.android.synthetic.main.item_layanan.view.*
import kotlinx.android.synthetic.main.item_layanan.view.lineLayanan
import kotlinx.android.synthetic.main.item_layanan.view.tvEstimasi
import kotlinx.android.synthetic.main.item_layanan.view.tvNamaLayanan
import kotlinx.android.synthetic.main.item_layanan_user.view.*
import kotlinx.android.synthetic.main.item_sparepart.view.*
import kotlinx.android.synthetic.main.item_sparepart.view.tvHarga
import java.io.Serializable
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterLayananUser(private val list:ArrayList<Layanan>) : RecyclerView.Adapter<AdapterLayananUser.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_layanan_user,parent,false))
    }

    override fun getItemCount(): Int = list?.size
    lateinit var mUserPref : UserPreference

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val nf = NumberFormat.getNumberInstance(Locale.GERMAN)
        val df = nf as DecimalFormat

        holder.view.tvNamaLayanan.text = list?.get(position)?.nama
        holder.view.tvHarga.text ="Rp. " +df.format(list?.get(position)?.harga)
        holder.view.tvEstimasi.text ="Estimasi  " +list?.get(position)?.estimasiWaktu + " hari"
        holder.view.tvRincianLayanan.text ="Rincian :   \n" +list?.get(position)?.rincianLayanan
        mUserPref = UserPreference(holder.view.tvEstimasi.context)

        holder.view.lineLayanan.setOnClickListener {
            Log.d("adapterIsi",""+list.get(position).toString())


        }

    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

}