package com.example.listacomponenteqr.navigation

sealed class Destination(val route: String){
    object SplashScreen : Destination(route = "splash_screen")
    object LoginScreen : Destination(route = "login_screen")
    object HomeScreen : Destination(route = "home_screen")
    object MaquinasListScreen : Destination(route = "maquinas_list_screen")
    object MaquinasActivate : Destination(route = "maquinas_activate_screen")
    object MaquinasPiezasScreen : Destination(route = "maquinas_piezas_screen")
    object SolicitudPedidoScreen : Destination(route = "solicitud_pedido_screen")
    object DevolucionMaterialScreen : Destination(route = "devolucion_material_screen")
    object DescuentoMaterialScreen : Destination(route = "descuento_material_dcreen" )
    object InventarioScreen : Destination(route = "inventario_screen")
    object RecibirMaterialScreen :Destination(route = "recibir_material_screen")
    object MaquinaSalaScreen : Destination(route = "maquinas_sala_screen")

    companion object {
        fun getStartDestination() = SplashScreen.route
    }
}