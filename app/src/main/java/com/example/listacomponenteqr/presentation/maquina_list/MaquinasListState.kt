package com.example.listacomponenteqr.presentation.maquina_list

import com.example.listacomponenteqr.domain.model.Maquina

data class MaquinasListState (
    val isLoading: Boolean = false,
    val maquinas: List<Maquina> = emptyList(),
    val error : String = ""
)