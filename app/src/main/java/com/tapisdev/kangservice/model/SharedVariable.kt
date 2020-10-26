package com.tapisdev.kangservice.model

import com.google.android.gms.maps.model.LatLng

class SharedVariable {
    //open var selectedIdPenyedia = "-"

    companion object {
        var totalKeranjang : Int = 0
        var lokasiPenyedia  = LatLng(0.0,0.0)
    }
}