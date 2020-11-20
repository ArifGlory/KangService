package com.tapisdev.kangservice.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class CartSparepart(
    var cartId: String? = "",
    var nama: String? = "",
    var harga: Int? = 0,
    var foto: String? = "",
    var idAdmin: String? = "",
    var idUser: String? = "",
    var jumlah: Int? = 0,
    var idPesanan: String? = "",
    var idSparepart: String? = ""
) : Parcelable, java.io.Serializable