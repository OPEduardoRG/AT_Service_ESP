package com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion

import com.google.gson.annotations.SerializedName

data class CodigoRefaccion (
    @SerializedName("refacciones" ) var refacciones : ArrayList<Refacciones> = arrayListOf()
)

data class Refacciones (

    @SerializedName("codigo" ) var codigo : String? = null,
    @SerializedName("nombre" ) var nombre : String? = null,
    @SerializedName("granel" ) var granel : String? = null

)