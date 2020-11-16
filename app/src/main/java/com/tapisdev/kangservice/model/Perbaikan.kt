package com.tapisdev.kangservice.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Perbaikan(
    var perbaikanId: String? = "",
    var layananId: String? = "",
    var idAdmin: String? = "",
    var idUser: String? = "",
    var namaLayanan: String? = "",
    var merkHp: String? = "",
    var alamatUser: String? = "",
    var harga: Int? = 0,
    var tanggalMulai: String? = "",
    var tanggalSelesai: String? = "",
    var status: String? = ""
) : Parcelable, java.io.Serializable