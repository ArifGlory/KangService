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
import com.tapisdev.kangservice.model.Sparepart
import com.tapisdev.kangservice.model.UserPreference
import kotlinx.android.synthetic.main.item_sparepart.view.*
import java.io.Serializable
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterSparepart(private val list:ArrayList<Sparepart>) : RecyclerView.Adapter<AdapterSparepart.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_sparepart,parent,false))
    }

    override fun getItemCount(): Int = list?.size
    lateinit var mUserPref : UserPreference

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val nf = NumberFormat.getNumberInstance(Locale.GERMAN)
        val df = nf as DecimalFormat

        holder.view.tvNamaSparepart.text = list?.get(position)?.nama
        holder.view.tvHarga.text ="Rp. " +df.format(list?.get(position)?.harga)
        mUserPref = UserPreference(holder.view.tvHarga.context)

        Glide.with(holder.view.ivSparepart.context)
            .load(list?.get(position)?.foto)
            .into(holder.view.ivSparepart)

        holder.view.lineSparepart.setOnClickListener {
            Log.d("adapterIsi",""+list.get(position).toString())
            if (mUserPref.getJenisUser().equals("admin")){
                val i = Intent(holder.view.lineSparepart.context, DetailSparepartActivity::class.java)
                i.putExtra("sparepart",list.get(position) as Serializable)
                holder.view.lineSparepart.context.startActivity(i)
            }else{

            }

        }

    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

}