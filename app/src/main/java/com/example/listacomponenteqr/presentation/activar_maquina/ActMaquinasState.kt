package com.example.listacomponenteqr.presentation.activar_maquina

import com.example.listacomponenteqr.domain.model.ActivateMaquina

data class ActMaquinasState(
    val isLoading: Boolean = false,
    val actmaquinas: ActivateMaquina = ActivateMaquina(null, null),
    val error: String = ""

)


