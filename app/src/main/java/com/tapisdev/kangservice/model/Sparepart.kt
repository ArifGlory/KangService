package com.tapisdev.kangservice.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Sparepart(
    var sparepartId: String? = "",
    var nama: String? = "",
    var harga: Int? = 0,
    var stok: Int? = 0,
    var foto: String? = "",
    var idAdmin: String? = ""
) : Parcelable, java.io.Serializable