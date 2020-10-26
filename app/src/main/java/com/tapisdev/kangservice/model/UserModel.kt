package com.tapisdev.kangservice.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class UserModel(
    var name : String = "",
    var email : String = "",
    var foto : String = "",
    var phone : String = "",
    var jenis : String = "",
    var uId : String = "",
    var alamat : String = "",
    var latlon : String = "",
    var deskripsi : String = ""
) : Parcelable,java.io.Serializable