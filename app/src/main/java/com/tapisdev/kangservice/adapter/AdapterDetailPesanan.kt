package com.tapisdev.kangservice.adapter

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.opengl.GLDebugHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.activity.admin.DetailSparepartActivity
import com.tapisdev.kangservice.model.CartSparepart
import com.tapisdev.kangservice.model.SharedVariable
import com.tapisdev.kangservice.model.Sparepart
import com.tapisdev.kangservice.model.UserPreference
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.item_detail_pesanan.view.*
import kotlinx.android.synthetic.main.item_sparepart.view.*
import kotlinx.android.synthetic.main.item_sparepart.view.ivSparepart
import kotlinx.android.synthetic.main.item_sparepart.view.lineSparepart
import kotlinx.android.synthetic.main.item_sparepart.view.tvHarga
import kotlinx.android.synthetic.main.item_sparepart.view.tvNamaSparepart
import java.io.Serializable
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterDetailPesanan(private val list:ArrayList<CartSparepart>) : RecyclerView.Adapter<AdapterDetailPesanan.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_detail_pesanan,parent,false))
    }

    override fun getItemCount(): Int = list?.size
    lateinit var mUserPref : UserPreference
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val nf = NumberFormat.getNumberInstance(Locale.GERMAN)
        val df = nf as DecimalFormat

        holder.view.tvNamaSparepart.text = list?.get(position)?.nama
        holder.view.tvHarga.text ="Rp. " +df.format(list?.get(position)?.harga)
        holder.view.tvJumlahDipesan.text ="Jumlah dipesan : " +df.format(list?.get(position)?.jumlah)
        mUserPref = UserPreference(holder.view.tvHarga.context)

        Glide.with(holder.view.ivSparepart.context)
            .load(list?.get(position)?.foto)
            .into(holder.view.ivSparepart)

        holder.view.lineSparepart.setOnClickListener {
            Log.d("adapterIsi",""+list.get(position).toString())

        }

    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

}