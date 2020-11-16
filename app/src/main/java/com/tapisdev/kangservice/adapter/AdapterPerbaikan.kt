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
import com.tapisdev.kangservice.activity.admin.DetailPerbaikanActivity
import com.tapisdev.kangservice.activity.admin.DetailSparepartActivity
import com.tapisdev.kangservice.activity.pengguna.DetailPerbaikanUserActivity
import com.tapisdev.kangservice.activity.pengguna.PesanlLayananPerbaikanActivity
import com.tapisdev.kangservice.model.Layanan
import com.tapisdev.kangservice.model.Perbaikan
import com.tapisdev.kangservice.model.Sparepart
import com.tapisdev.kangservice.model.UserPreference
import kotlinx.android.synthetic.main.item_layanan.view.*
import kotlinx.android.synthetic.main.item_layanan.view.tvEstimasi
import kotlinx.android.synthetic.main.item_layanan.view.tvNamaLayanan
import kotlinx.android.synthetic.main.item_layanan_user.view.*
import kotlinx.android.synthetic.main.item_permintaan_perbaikan.view.*
import kotlinx.android.synthetic.main.item_sparepart.view.*
import kotlinx.android.synthetic.main.item_sparepart.view.tvHarga
import java.io.Serializable
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterPerbaikan(private val list:ArrayList<Perbaikan>) : RecyclerView.Adapter<AdapterPerbaikan.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_permintaan_perbaikan,parent,false))
    }

    override fun getItemCount(): Int = list?.size
    lateinit var mUserPref : UserPreference

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val nf = NumberFormat.getNumberInstance(Locale.GERMAN)
        val df = nf as DecimalFormat

        var tanggalPesan = list?.get(position)?.tanggalMulai?.let { convertDate(it) }
        holder.view.tvNamaLayanan.text = list?.get(position)?.namaLayanan
        holder.view.tvMerkHp.text = list?.get(position)?.merkHp
        holder.view.tvTanggal.text = "Dipesan pada "+tanggalPesan
        mUserPref = UserPreference(holder.view.linePerbaikan.context)

        holder.view.linePerbaikan.setOnClickListener {
            Log.d("adapterIsi",""+list.get(position).toString())
            if (mUserPref.getJenisUser().equals("admin")){
                val i = Intent(holder.view.linePerbaikan.context, DetailPerbaikanActivity::class.java)
                i.putExtra("perbaikan",list.get(position) as Serializable)
                holder.view.linePerbaikan.context.startActivity(i)
            }else{
                val i = Intent(holder.view.linePerbaikan.context, DetailPerbaikanUserActivity::class.java)
                i.putExtra("perbaikan",list.get(position) as Serializable)
                holder.view.linePerbaikan.context.startActivity(i)
            }

        }

    }

    fun convertDate(tanggal : String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd")
        //val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        val output = formatter.format(parser.parse(tanggal))

        return output
    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

}