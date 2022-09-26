package com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion

import com.google.gson.annotations.SerializedName

data class Solicitud (

    @SerializedName("codigo"   ) var codigo   : String? = null,
    @SerializedName("cantidad" ) var cantidad : String? = null,
    @SerializedName("desc"     ) var desc     : String? = null,
    @SerializedName("estatus"  ) var estatus  : String? = null,
)
