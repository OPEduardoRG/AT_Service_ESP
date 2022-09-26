package com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion

import com.example.listacomponenteqr.data.remote.dto.RecibirMat.RecibirMat1
import com.google.gson.annotations.SerializedName

data class PedidoMaterial (
    @SerializedName("usuarioid" ) var usuarioid : String?              = null,
    @SerializedName("salaid"    ) var salaid    : String?              = null,
    @SerializedName("almacenId" ) var almacenId : String?              = null,
    @SerializedName("fechaHora" ) var fechaHora : String?              = null,
    @SerializedName("solicitud" ) var solicitud : ArrayList<Solicitud> = arrayListOf()
)

data class DevolucionMaterial(
    @SerializedName("usuarioid"    ) var usuarioid: String?              = null,
    //@SerializedName("estatus"      ) var estatus    : String?              = null,
    @SerializedName("observaciones") var comentarios: String?              = null,
    @SerializedName("devolucion"   ) var solicitud: ArrayList<Solicitud> = arrayListOf(),
)

data class DevolucionPorEstatus(
    @SerializedName("Correcto"     ) var correcto  : ArrayList<Solicitud> ,
    @SerializedName("Revision"     ) var revision  : ArrayList<Solicitud> ,
    @SerializedName("RMA"          ) var RMA       : ArrayList<Solicitud> ,
    @SerializedName("Desechos"     ) var Desechos  : ArrayList<Solicitud> ,
    @SerializedName("Nuevo"        ) var Nuevo     : ArrayList<Solicitud> ,
)

data class DevSolicitudMaterial (
    @SerializedName("usuarioid" ) var usuarioid : String?              = null,
    @SerializedName("solicitud" ) var solicitud : ArrayList<Solicitud> = arrayListOf()
)

data class DescuentoMaterial (
    @SerializedName("usuarioid"      ) var usuarioid      : String?              = null,
    @SerializedName("salaid"         ) var salaid         : String?              = null,
    @SerializedName("serie"          ) var seriex         : String?              = null,
    @SerializedName("observacionesx" ) var observacionesx : String?              = null,
    @SerializedName("descuento"      ) var descuento      : ArrayList<Solicitud> = arrayListOf()
)

data class DescuentoMaterialResp (
    @SerializedName("respuesta"   ) var respuesta   : String?           = null,
    @SerializedName("descripcion" ) var descripcion : ArrayList<String> = arrayListOf()
)

data class Solicitud_QR(
    @SerializedName("usuarioid" ) var usuarioid : String?              = null,
    @SerializedName("maquina"   ) var maquina   : String?              = null,
    @SerializedName("codigo"    ) var codigo    : String?              = null,
    @SerializedName("almacenId" ) var almacenId : String?              = null,
    @SerializedName("fechaHora" ) var fechaHora : String?              = null,
)

data class PostRecibirMaterial (
    @SerializedName("usuarioid" ) var usuarioid : String?              = null,
    @SerializedName("material"  ) var material : ArrayList<RecibirMat1> = arrayListOf()
)