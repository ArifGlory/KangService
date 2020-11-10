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
import com.tapisdev.kangservice.activity.pengguna.DetailTokoActivity
import com.tapisdev.kangservice.model.UserModel
import kotlinx.android.synthetic.main.item_toko.view.*
import java.io.Serializable

class AdapterToko(private val list:ArrayList<UserModel>) : RecyclerView.Adapter<AdapterToko.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_toko,parent,false))
    }

    override fun getItemCount(): Int = list?.size

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.view.tvNamaToko.text = list?.get(position)?.name
        holder.view.tvLocation.text =list?.get(position)?.alamat

        Glide.with(holder.view.ivToko.context)
            .load(list?.get(position)?.foto)
            .into(holder.view.ivToko)

        holder.view.lineToko.setOnClickListener {
            Log.d("adapterIsi",""+list.get(position).toString())
            val i = Intent(holder.view.lineToko.context, DetailTokoActivity::class.java)
            i.putExtra("toko",list.get(position) as Serializable)
            holder.view.lineToko.context.startActivity(i)
        }

    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

}