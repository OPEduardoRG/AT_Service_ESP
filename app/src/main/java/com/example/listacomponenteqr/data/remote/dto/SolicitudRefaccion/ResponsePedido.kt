package com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion

import com.google.gson.annotations.SerializedName

data class ResponsePedido (
    @SerializedName("respuesta" ) var respuesta : String? = null,
    @SerializedName("solicitud" ) var solicitud : String? = null
)
data class Responsedevolucion (
    @SerializedName("respuesta"  ) var respuesta  : String? = null,
    @SerializedName("devolucion" ) var devolucion : String? = null
)
data class ResponseValida (
    @SerializedName("respuesta"   ) var respuesta   : String? = null,
    @SerializedName("descripcion" ) var descripcion : String? = null,
    @SerializedName("granel"      ) var granel      : String? = null
)

data class ValidaQR(
    @SerializedName("usuarioid"   ) var usuarioid   : String? = null,
    @SerializedName("codigo"   ) var codigo   : String? = null,
)

data class ResponsePedidoQR (
    @SerializedName("respuesta"   ) var respuesta   : String? = null,
    @SerializedName("solicitud"   ) var solicitud   : String? = null,
    @SerializedName("descripcion" ) var descripcion : String? = null
)

data class ingresoMat (
    @SerializedName("respuesta"   ) var respuesta   : String? = null,
    @SerializedName("ingreso"     ) var ingreso   : String? = null,
)