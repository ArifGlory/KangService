package com.tapisdev.kangservice.activity.pengguna

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.adapter.AdapterSparepart
import com.tapisdev.kangservice.model.Sparepart
import com.tapisdev.kangservice.model.UserModel
import kotlinx.android.synthetic.main.activity_detail_toko.*
import java.net.URLEncoder


class DetailTokoActivity : BaseActivity() {

    lateinit var toko : UserModel
    lateinit var i : Intent
    var TAG_GET_Sparepart = "getSparepart"
    lateinit var adapter: AdapterSparepart

    var listSparepart = ArrayList<Sparepart>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_toko)
        i = intent
        toko = i.getSerializableExtra("toko") as UserModel
        Log.d("toko"," id : "+toko.uId)

        adapter = AdapterSparepart(listSparepart)
        rvSparepart.setHasFixedSize(true)
        rvSparepart.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        rvSparepart.adapter = adapter

        ivBack.setOnClickListener {
            onBackPressed()
        }
        tvKeLayanan.setOnClickListener {
            val i = Intent(this,ListLayananUserActivity::class.java)
            i.putExtra("idToko",toko.uId)
            startActivity(i)
        }
        tvHubungi.setOnClickListener {
            /*val sendIntent = Intent("android.intent.action.MAIN")
            sendIntent.component = ComponentName("com.whatsapp", "com.whatsapp.Conversation")
            sendIntent.putExtra(
                "jid",
                PhoneNumberUtils.stripSeparators(toko.phone) + "@s.whatsapp.net"
            )
            startActivity(sendIntent)*/
            var msg = "Halo Kang Service, saya ingin konsultasi.."
            var phone = toko.phone
            var firstChar = phone.take(1) as String
            var newPhone = ""

            if (firstChar.equals("0")){
                newPhone = phone.substring(1,phone.length)
                newPhone = "62"+newPhone
            }else{
                newPhone = phone
            }
            Log.d("tag_phone",""+newPhone)


            try {
                val packageManager: PackageManager = this.getPackageManager()
                val i = Intent(Intent.ACTION_VIEW)
                val url =
                    "https://api.whatsapp.com/send?phone=" + newPhone + "&text=" + URLEncoder.encode(
                        msg,
                        "UTF-8"
                    )
                i.setPackage("com.whatsapp")
                i.data = Uri.parse(url)
                if (i.resolveActivity(packageManager) != null) {
                    startActivity(i)
                } else {
                    showErrorMessage("nomor WA error")
                }
            } catch (e: Exception) {
                Log.e("ERROR WHATSAPP", e.toString())
                showErrorMessage("Terjadi kesalahan, coba lagi nanti")
            }
        }
        tvLocation.setOnClickListener {
            if (toko.latlon.equals("none") || toko.latlon.equals("")){
                showInfoMessage("Toko ini belum memilih titik lokasinya")
            }else{
                val i = Intent(this,MapsTokoActivity::class.java)
                i.putExtra("latlon",toko.latlon)
                startActivity(i)
            }

        }


        updateUI()
        getDataMySparepart()
    }

    fun updateUI(){
        tvNamaToko.setText(toko.name)
        tvLocation.setText(toko.alamat)

        Glide.with(this)
            .load(toko.foto)
            .into(ivToko)
    }

    fun getDataMySparepart(){
        sparepartRef.get().addOnSuccessListener { result ->
            listSparepart.clear()
            //Log.d(TAG_GET_Sparepart," datanya "+result.documents)
            for (document in result){
                //Log.d(TAG_GET_Sparepart, "Datanya : "+document.data)
                var sparepart : Sparepart = document.toObject(Sparepart::class.java)
                sparepart.sparepartId = document.id
                if (sparepart.idAdmin.equals(toko.uId)){
                    listSparepart.add(sparepart)
                }
            }
            if (listSparepart.size == 0){
                animation_view_toko.setAnimation(R.raw.empty_box)
                animation_view_toko.playAnimation()
                animation_view_toko.loop(false)
            }else{
                animation_view_toko.visibility = View.INVISIBLE
            }
            adapter.notifyDataSetChanged()

        }.addOnFailureListener { exception ->
            showErrorMessage("terjadi kesalahan : "+exception.message)
            Log.d(TAG_GET_Sparepart,"err : "+exception.message)
        }
    }

}
