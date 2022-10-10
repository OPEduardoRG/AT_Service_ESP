package com.example.listacomponenteqr.utils
/**
 * Created by Brian Fernando Mtz on 05/03/2022.
 */
object Constants {
    /**BaseURL-España*/
    const val BASE_URL = "http://10.110.76.16"
    /**PRUEBA**/
    //const val BASE_URL = "https://jsonplaceholder.typicode.com"
    /**BaseURL-Pruebas-RedInterna**/
    //const val BASE_URL = "http://10.10.0.252:8082"
    /**BaseURL-Pruebas-RedExterna*/
    //const val BASE_URL = "http://http://pruebasisco.ddns.net:8082"
    /**BaseURL-Productivo-RedExterna**/
    //const val BASE_URL = "http://189.254.111.195:8082"
    /**API_LOGIN*/
    const val API_LOGIN = "Android/codigoQREspana/login.php"
    /**API_LISTA_COMPONENTES_MAQUINAS*/
    const val API_LIST_MAQUINA = "Android/codigoQREspana/listaComponentes.php?"
    /**API ACTIVACION MAQUINA**/
    const val API_ACTIVACION_MAQUINA = "Android/codigoQREspana/validaMaquinaActivacion.php?"
    /**API ACTIVACION MAQUINAS COMPONENTES**/
    const val API_ACTIV_MAQUINA_COMPONENTE = "Android/codigoQREspana/activacionMaquinas.php"
    /**API REMPLAZO COMPONENTES**/
    const val API_REMPLAZO_COMP = "Android/codigoQREspana/ReemplazarComponente.php"
    /**Lista Codigos**/
    const val API_CODIGOS_SOLICITUD = "Android/listaCodigosRef.php"
    /**CreaPedido**/
    const val API_CREAR_PEDIDO = "Android/creaPedidoMaterial.php"
    /**Lista de subcentros**/
    const val API_SUBCENTROS = "Android/codigoQREspana/listaSubcentro.php"
    /**Crea la devolucion del material**/
    const val API_DEVOLUCION_MT = "Android/codigoQREspana/creaDevolucion.php"
    /**Salas por region**/
    const val API_SALAS_REGION = "Android/listaSalasXRegion.php"
    /**Valida Qr Devolucion**/
    const val API_VALIDA_QR = "Android/codigoQREspana/ValidarQRDev.php"
    /**Descuento de material**/
    const val API_DESC_MATERIAL = "Android/codigoQREspana/descuentoMaterialVan.php"
    /**Inventario Van Tecnico**/
    const val API_INVENTARIO_VAN = "Android/codigoQREspana/inventarioVan.php"
    /**Descuento Material Van**/
    const val API_DESC_VAN = "Android/codigoQREspana/descuentoMatVan.php"
    /**Agrega Material Van Revision**/
    const val API_VAN_REVISION = "Android/codigoQREspana/colocarMatVanRevision.php"
    /**Solicita el material por QR**/
    const val API_SOLICITAR_POR_QR = "Android/codigoQREspana/creaPedidoPorQR.php"
    /**Solicitar pedido de devolucion**/
    const val API_SOLICITA_PEDIDO_DEVOLUCION = "Android/codigoQREspana/creaPedidoPorDevolucion.php"
    /**Recibir material van**/
    const val API_RECIBIR_MATERIAL = "Android/codigoQREspana/recibirMatTecnico.php"
    /**Maqiuinas en la sala componentes**/
    const val API_MAQUINAS_SALA = "Android/codigoQREspana/maquinasSala.php"

    /**PRUEBA**/
    const val API_PRUEBA = "users"
    /**bebe bailando**/
    const val b = false
}

