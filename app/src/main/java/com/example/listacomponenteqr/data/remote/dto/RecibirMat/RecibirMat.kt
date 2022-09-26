package com.example.listacomponenteqr.data.remote.dto.RecibirMat

import com.google.gson.annotations.SerializedName

data class RecibirMat (
    @SerializedName("codigo"          ) var codigo         : String? = null,
    @SerializedName("cantidad"        ) var cantidad       : String? = null,
    @SerializedName("estatus"         ) var estatus        : String? = null,
    @SerializedName("desc"            ) var desc           : String? = null,
)
data class RecibirMat1 (
    @SerializedName("codigo"          ) var codigo         : String? = null,
    @SerializedName("cantidad"        ) var cantidad       : String? = null,
    @SerializedName("estatus"         ) var estatus        : String? = null,
)
