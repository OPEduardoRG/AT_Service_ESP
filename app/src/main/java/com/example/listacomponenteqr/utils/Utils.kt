package com.example.listacomponenteqr.utils

import android.content.Context

class Utils (val context : Context?) {
    fun getValidationSerie(string: String): Boolean{
        val component = "[0-9/-]{1,30}"
        return component.toRegex().matches(string)
    }
    fun getValidationComponent(string: String): Boolean{
        val component = "[A-Za-z0-9/-]{1,35}[\\s]{1}[A-Za-z0-9]{1,30}"
        return component.toRegex().matches(string)
    }
    fun getValidationComponent2(string: String): Boolean {
        val component = "[A-Za-z0-9]{1,30}[-]{1}[A-Za-z0-9]{1,30}[-]{1}[A-Za-z0-9]{1,30}[ ]{1}[A-Za-z0-9]{1,30}"
        return component.toRegex().matches(string)
    }
    fun getValidation(string: String): Boolean {
        val formattDLAC = "[0-9]{1,30}[;]{1}[a-zA-Z0-9:_/-]{1,30}[;]{1}[a-zA-Z0-9: _/-]{0,20}"
        return formattDLAC.toRegex().matches(string)
    }
    fun getValidation2(string: String): Boolean {
        val formattDLAC = "[0-9]{1,30}[;]{1}[a-zA-Z0-9:=._/-]{1,30}[;]{1}[a-zA-Z0-9:._/-]{0,30}[;]{1}[a-zA-Z0-9:._ /-]{0,30}"
        return formattDLAC.toRegex().matches(string)
    }
    fun getValidationName(string: String): Boolean{
        val component = "[A-Z a-z0-9:._/\"-]{3,30}"
        return component.toRegex().matches(string)
    }
    fun getValidationRefaccion(string: String): Boolean{
        val component = "[A-Za-z0-9]{1,15}[-]{1}[A-Za-z0-9]{1,30}[-]{1}[A-Za-z0-9]{1,30}"
        return component.toRegex().matches(string)
    }
    fun getValidationRefaccion2(string: String): Boolean{
        val component = "[A-Za-z0-9]{1,15}[-]{1}[A-Za-z0-9]{1,30}"
        return component.toRegex().matches(string)
    }
    fun getValidationInt(string: String): Boolean{
        val component = "[0-9]{1,30}"
        return component.toRegex().matches(string)
    }
}
