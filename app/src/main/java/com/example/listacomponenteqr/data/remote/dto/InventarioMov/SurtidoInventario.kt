package com.example.listacomponenteqr.data.remote.dto.InventarioMov

import com.google.gson.annotations.SerializedName

data class SurtidoInventario (

    @SerializedName("respuesta"   ) var respuesta   : Int?    = null,
    @SerializedName("descripcion" ) var descripcion : String? = null,
    @SerializedName("refaccionid" ) var refaccionid : String? = null,
    @SerializedName("subcentro"   ) var subcentro   : String? = null

)