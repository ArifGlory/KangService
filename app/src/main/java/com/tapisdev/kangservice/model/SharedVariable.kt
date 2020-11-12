package com.tapisdev.kangservice.model

import com.google.android.gms.maps.model.LatLng

class SharedVariable {
    //open var selectedIdPenyedia = "-"

    companion object {
        var totalKeranjang : Int = 0
        var IdPenyediaCartSparepart: String = "-"
        var lokasiToko  = LatLng(0.0,0.0)
        var listCart = ArrayList<CartSparepart>()
    }
}