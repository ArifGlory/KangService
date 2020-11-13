package com.tapisdev.PesananPesanan.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.activity.admin.DetailTransaksiSparepartAdminActivity
import com.tapisdev.kangservice.activity.pengguna.DetailTransaksiSparepartActivity
import com.tapisdev.kangservice.model.Pesanan
import com.tapisdev.kangservice.model.UserPreference
import kotlinx.android.synthetic.main.item_pesanan_user.view.*
import java.io.Serializable
import java.text.SimpleDateFormat


class AdapterPesananUser(private val list:ArrayList<Pesanan>) : RecyclerView.Adapter<AdapterPesananUser.Holder>(){

    lateinit var mUserPref : UserPreference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_pesanan_user,parent,false))
    }

    override fun getItemCount(): Int = list?.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        mUserPref = UserPreference(holder.view.linePesanan.context)

        var tanggalPesan = list?.get(position)?.tanggalPesan?.let { convertDate(it) }
        holder.view.tvTanggal.text = "Pesanan tanggal "+tanggalPesan
        holder.view.tvAlamat.text = "Alamat Kirim di "+list?.get(position)?.alamat
        holder.view.tvStatus.text = list?.get(position)?.status
        holder.view.ivFoodCart.setImageResource(R.drawable.ic_add_shopping_cart_black_24dp)

        holder.view.linePesanan.setOnClickListener {
            Log.d("adapterIsi",""+list.get(position).toString())

            if (mUserPref.getJenisUser().equals("admin")){
                val i = Intent(holder.view.linePesanan.context, DetailTransaksiSparepartAdminActivity::class.java)
                i.putExtra("pesanan",list.get(position) as Serializable)
                holder.view.linePesanan.context.startActivity(i)
            }else{
                val i = Intent(holder.view.linePesanan.context, DetailTransaksiSparepartActivity::class.java)
                i.putExtra("pesanan",list.get(position) as Serializable)
                holder.view.linePesanan.context.startActivity(i)
            }


        }

    }

    fun convertDate(tanggal : String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        //val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        val output = formatter.format(parser.parse(tanggal))

        return output
    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

}