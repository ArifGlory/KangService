package com.tapisdev.kangservice.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Pesanan(
    var pesananId: String? = "",
    var idAdmin: String? = "",
    var idUser: String? = "",
    var alamat: String? = "",
    var tanggalPesan: String? = "",
    var buktiBayar: String? = "",
    var status: String? = "",
    var stokCalculated: Boolean? = false
) : Parcelable, java.io.Serializable