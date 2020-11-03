package com.tapisdev.kangservice.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Layanan(
    var layananId: String? = "",
    var nama: String? = "",
    var harga: Int? = 0,
    var estimasiWaktu: String? = "",
    var rincianLayanan: String? = "",
    var idAdmin: String? = ""
) : Parcelable, java.io.Serializable