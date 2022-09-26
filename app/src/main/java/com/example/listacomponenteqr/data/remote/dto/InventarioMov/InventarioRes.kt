package com.example.listacomponenteqr.data.remote.dto.InventarioMov

import com.google.gson.annotations.SerializedName

data class InventarioRes (
    @SerializedName("refacciones" ) var refacciones : ArrayList<Refacciones> = arrayListOf()
)
data class Refacciones (
    @SerializedName("codigo"   ) var codigo   : String? = null,
    @SerializedName("nombre"   ) var nombre   : String? = null,
    @SerializedName("cantidad" ) var cantidad : String? = null,
    @SerializedName("estatus"  ) var estatus  : String? = null,
)
data class Moviemiento (
    @SerializedName("respuesta"   ) var respuesta   : Int?    = null,
    @SerializedName("descripcion" ) var descripcion : String? = null
)
data class InventarioSh (
    var string   : String? = null,
    var int : Int? = null
)