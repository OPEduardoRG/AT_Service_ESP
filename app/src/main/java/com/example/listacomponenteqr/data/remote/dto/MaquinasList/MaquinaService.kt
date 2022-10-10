package com.example.listacomponenteqr.data.remote

import com.example.listacomponenteqr.utils.Constants
import com.example.listacomponenteqr.data.remote.dto.InventarioMov.InventarioRes
import com.example.listacomponenteqr.data.remote.dto.InventarioMov.Moviemiento
import com.example.listacomponenteqr.data.remote.dto.Login.LoginrespDto
import com.example.listacomponenteqr.data.remote.dto.MaquinaActivate.ActivateMaquinaDto
import com.example.listacomponenteqr.data.remote.dto.MaquinaActivate.ActivationMaqComp
import com.example.listacomponenteqr.data.remote.dto.MaquinasList.MaquinaDto
import com.example.listacomponenteqr.data.remote.dto.SolicitudRefaccion.*
import com.example.listacomponenteqr.data.remote.dto.InventarioMov.SurtidoInventario
import com.example.listacomponenteqr.data.remote.dto.MaquinasSala.MaquinasSala
import retrofit2.Response
import retrofit2.http.*

interface ServiceApi {
    @GET(Constants.API_LOGIN)
    suspend fun getLogin(
        @Query("usuariox") usuario : String,
        @Query("passx") pass : String) : Response<LoginrespDto>

    @GET(Constants.API_LIST_MAQUINA)
    suspend fun getlistmaquina(
        @Query("n") n : String,
        @Query("idmaquina")idmaquina : String): List<MaquinaDto>

    @GET(Constants.API_ACTIVACION_MAQUINA)
    suspend fun getActivate(
        @Query("n") n : String,
        @Query("idmaquina")idmaquina :String): ActivateMaquinaDto

    @GET(Constants.API_ACTIV_MAQUINA_COMPONENTE)
    suspend fun getActMaqComp(
        @Query("n")n :String,
        @Query("idmaquina")idmaquina :String,
        @Query("codigo1x") codigo1x : String,
        @Query("codigo2x") codigo2x : String,
        @Query("usuarioid") usuarioid : Int): ActivationMaqComp

    @GET(Constants.API_REMPLAZO_COMP)
    suspend fun postRemplazoComponete(
        @Query("n"        ) n        :Int,
        @Query("idmaquina")idmaquina :String,
        @Query("codigo1"  ) codigo1  : String,
        @Query("codigo2"  ) codigo2  : String,
        @Query("usuarioid") usuarioid : Int): ActivationMaqComp

    @GET(Constants.API_CODIGOS_SOLICITUD)
    suspend fun getCodigosSolicitud(): CodigoRefaccion

    @GET(Constants.API_SUBCENTROS)
    suspend fun getSubcentros(): SubCentrosEsp

    @GET(Constants.API_SALAS_REGION)
    suspend fun getSalasRegion(
        @Query("regionidx") regionidx :String
    ) : Response<SalasRegionM>

    @POST(Constants.API_CREAR_PEDIDO)
    suspend fun postCrearPedido(
        @Body PedidoMaterial: PedidoMaterial): Response<ResponsePedido>

    @POST(Constants.API_SOLICITAR_POR_QR)
    suspend fun solicitarPorQr(
        @Body Solicitud_QR: Solicitud_QR
    ): Response<ResponsePedidoQR>

    @POST(Constants.API_DEVOLUCION_MT)
    suspend fun postDevolucionPedido(
        @Body DevolucionMaterial: DevolucionMaterial): Response<Responsedevolucion>

    @POST(Constants.API_DESC_MATERIAL)
    suspend fun postDescuentoMat(
        @Body DescuentoMaterial: DescuentoMaterial): Response<DescuentoMaterialResp>

    @POST(Constants.API_VALIDA_QR)
    suspend fun getValidationQrMaterial(
        @Body ValidaQR : ValidaQR
    ) : Response<ResponseValida>

   @GET(Constants.API_INVENTARIO_VAN)
   suspend fun getInventariio(
       @Query("usuarioIDx") usuarioIDx : String,
       @Query("estatusx") estatusx : String
   ) : Response<InventarioRes>

   @GET(Constants.API_DESC_VAN)
   suspend fun postDecuentoMtVan(
       @Query("cantidad" ) cantidad  : String,
       @Query("codigo"   ) codigo    : String,
       @Query("idmaquina") idmaquina : String,
       @Query("n"        ) n         : String,
       @Query("usuarioid") usuarioid : String
   ) : Response<SurtidoInventario>

   @GET(Constants.API_VAN_REVISION)
   suspend fun agregaVanRevision(
       @Query("subcentro"  ) subcentro   : String,
       @Query("cantidad"   ) cantidad    : String,
       @Query("refaccionid") refaccionid : String,
       @Query("usuarioid"  ) usuarioid   : String
   ) : Response<Moviemiento>

   @POST(Constants.API_SOLICITA_PEDIDO_DEVOLUCION)
   suspend fun solPedidoDevolucion(
       @Body DevolucionMaterial : DevSolicitudMaterial
   ) : Response<ResponsePedidoQR>

   @POST(Constants.API_RECIBIR_MATERIAL)
   suspend fun recibirMaterial(
       @Body PostRecibirMaterial : PostRecibirMaterial
   ) : Response<ingresoMat>

   @GET(Constants.API_MAQUINAS_SALA)
   suspend fun getMaquinasSala(
       @Query("id") id : Int?
   ): ArrayList<MaquinasSala>
/*
   @GET(Constants.API_PRUEBA)
   suspend fun apiprueba () : Response<ArrayList<Prueba>>
*/
}


