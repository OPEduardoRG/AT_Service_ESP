package com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion

import com.google.gson.annotations.SerializedName

data class SalasRegionM (
    @SerializedName("salas" ) var salas : ArrayList<Salas> = arrayListOf()
)
data class Salas (

    @SerializedName("salaid"   ) var salaid   : String? = null,
    @SerializedName("nombre"   ) var nombre   : String? = null,
    @SerializedName("officeID" ) var officeID : String? = null

)