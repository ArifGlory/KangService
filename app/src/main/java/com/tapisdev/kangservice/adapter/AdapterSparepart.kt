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
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val nf = NumberFormat.getNumberInstance(Locale.GERMAN)
        val df = nf as DecimalFormat

        holder.view.tvNamaSparepart.text = list?.get(position)?.nama
        holder.view.tvHarga.text ="Rp. " +df.format(list?.get(position)?.harga)
        holder.view.tvStok.text ="Stok : " +df.format(list?.get(position)?.stok)
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
                //memasukkan ke keranjang
                if (list.get(position)?.stok!! > 0){
                    showDialog(holder,position)
                }else{
                    Toasty.error(holder.view.lineSparepart.context, "Stok Barang tidak memadai", Toast.LENGTH_SHORT, true).show()
                }
            }

        }

    }

    private fun showDialog(holder: Holder,position: Int) {
        val dialog = Dialog(holder.view.ivSparepart.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dlg_confomation)
        val edJumlah = dialog.findViewById(R.id.edJumlah) as EditText
        val tvAdd = dialog.findViewById(R.id.tvAdd) as TextView
        val tvCancel = dialog.findViewById(R.id.tvCancel) as TextView


        tvAdd.setOnClickListener {

            if (edJumlah.text.toString().equals("") || edJumlah.text.toString().length == 0){
                holder.view.ivSparepart.context?.let { Toasty.error(it, "Anda belum memasukkan jumlah pemesanan", Toast.LENGTH_SHORT, true).show() }
            }else if (Integer.parseInt(edJumlah.text.toString()) > list.get(position)?.stok!! ){
                holder.view.ivSparepart.context?.let { Toasty.error(it, "Jumlah melebihi batas stok !", Toast.LENGTH_SHORT, true).show() }
            }
            else{
                var jml = Integer.parseInt(edJumlah.text.toString())
                var cartSparepart : CartSparepart = CartSparepart(UUID.randomUUID().toString(),
                    list?.get(position)?.nama,
                    list?.get(position)?.harga,
                    list?.get(position)?.foto,
                    list?.get(position)?.idAdmin,
                    auth.currentUser?.uid,
                    jml,
                    ""
                )
                SharedVariable.listCart.add(cartSparepart)
                SharedVariable.IdPenyediaCartSparepart = list.get(position).idAdmin.toString()
                dialog.dismiss()
                holder.view.ivSparepart.context?.let { Toasty.success(it, "Berhasil menambahkan kedalam keranjang", Toast.LENGTH_SHORT, true).show() }
            }
        }
        tvCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

}