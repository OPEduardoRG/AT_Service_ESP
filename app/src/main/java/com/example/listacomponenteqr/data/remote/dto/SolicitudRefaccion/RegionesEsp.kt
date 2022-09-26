package com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion

import com.google.gson.annotations.SerializedName

data class RegionesEsp(
    @SerializedName("regionidx"   ) var regionidx   : String? = null,
    @SerializedName("nombre"      ) var nombre      : String? = null,
)