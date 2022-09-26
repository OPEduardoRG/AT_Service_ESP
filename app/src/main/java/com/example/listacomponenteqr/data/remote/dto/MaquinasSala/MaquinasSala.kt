package com.example.listacomponenteqr.data.remote.dto.MaquinasSala

import com.google.gson.annotations.SerializedName

data class MaquinasSala (
    @SerializedName("serie"       ) var serie       : String?                = null,
    @SerializedName("mueble"      ) var mueble      : String?                = null,
    @SerializedName("componentes" ) var componentes : ArrayList<Componentes> = arrayListOf()
)

data class Componentes (
    @SerializedName("clave"  ) var clave  : String? = null,
    @SerializedName("nombre" ) var nombre : String? = null
)
