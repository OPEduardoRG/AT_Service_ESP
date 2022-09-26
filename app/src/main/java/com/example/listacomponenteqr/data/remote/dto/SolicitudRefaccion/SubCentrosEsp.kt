package com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion

import com.google.gson.annotations.SerializedName

data class SubCentrosEsp (
    @SerializedName("subcentros" ) var subcentros : ArrayList<Subcentros> = arrayListOf()
)
data class Subcentros (
    @SerializedName("idSub"  ) var idSub  : String? = null,
    @SerializedName("nombre" ) var nombre : String? = null
)