package com.tapisdev.kangservice.activity.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.kangservice.R
import com.tapisdev.kangservice.adapter.AdapterSparepart
import com.tapisdev.kangservice.model.Sparepart
import com.tapisdev.kangservice.model.UserPreference
import kotlinx.android.synthetic.main.activity_list_sparepart.*

class ListSparepartActivity : BaseActivity() {

    var TAG_GET_Sparepart = "getSparepart"
    lateinit var adapter: AdapterSparepart

    var listSparepart = ArrayList<Sparepart>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_sparepart)
        mUserPref = UserPreference(this)

        adapter = AdapterSparepart(listSparepart)
        rvSparepart.setHasFixedSize(true)
        rvSparepart.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        rvSparepart.adapter = adapter

        fab.setOnClickListener {
            val i = Intent(this,AddSparepartActivity::class.java)
            startActivity(i)
        }
        edSearchSparepart.doOnTextChanged { text, start, before, count ->
            var query = text.toString().toLowerCase().trim()
            var listSearchCatering = ArrayList<Sparepart>()

            for (c in 0 until listSparepart.size){
                var namaCatering = listSparepart.get(c).nama.toString().toLowerCase().trim()
                if (namaCatering.contains(query)){
                    listSearchCatering.add(listSparepart.get(c))
                }
            }

            adapter = AdapterSparepart(listSearchCatering)
            rvSparepart.layoutManager = LinearLayoutManager(this)
            rvSparepart.adapter = adapter
            adapter.notifyDataSetChanged()
        }

        getDataMySparepart()
    }

    fun getDataMySparepart(){
        sparepartRef.get().addOnSuccessListener { result ->
            listSparepart.clear()
            //Log.d(TAG_GET_Sparepart," datanya "+result.documents)
            for (document in result){
                //Log.d(TAG_GET_Sparepart, "Datanya : "+document.data)
                var sparepart : Sparepart = document.toObject(Sparepart::class.java)
                sparepart.sparepartId = document.id
                if (sparepart.idAdmin.equals(auth.currentUser?.uid)){
                    listSparepart.add(sparepart)
                }
            }
            if (listSparepart.size == 0){
                animation_view.setAnimation(R.raw.empty_box)
                animation_view.playAnimation()
                animation_view.loop(false)
            }else{
                animation_view.visibility = View.INVISIBLE
            }
            adapter.notifyDataSetChanged()

        }.addOnFailureListener { exception ->
            showErrorMessage("terjadi kesalahan : "+exception.message)
            Log.d(TAG_GET_Sparepart,"err : "+exception.message)
        }
    }

    override fun onResume() {
        super.onResume()
        getDataMySparepart()
    }
}
