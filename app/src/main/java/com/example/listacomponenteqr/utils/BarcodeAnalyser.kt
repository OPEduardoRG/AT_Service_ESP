package com.example.listacomponenteqr.utils

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.TimeUnit


@SuppressLint("UnsafeOptInUsageError")
class BarcodeAnalyser(
    private val onBarcodesDetected: (barcodes: List<Barcode>) -> Unit,
): ImageAnalysis.Analyzer{
    private var lastAnalyzedTimestamp = 0L
    override fun analyze(image: ImageProxy){
        val currentTimestamp = System.currentTimeMillis()
        if (currentTimestamp - lastAnalyzedTimestamp >= TimeUnit.MILLISECONDS.toMillis(1000)){
            image.image?.let { imageToAnalize ->
                val options = BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                    .build()
                val barcodeScanner = BarcodeScanning.getClient(options)
                val imageToProcess = InputImage.fromMediaImage(imageToAnalize, image.imageInfo.rotationDegrees)

                barcodeScanner.process(imageToProcess)
                    .addOnSuccessListener{ barcodes ->
                        if (barcodes.isNotEmpty()){
                            onBarcodesDetected(barcodes)
                        } else {
                            Log.d("TAG", "No se detecta QR")
                        }
                    }
                    .addOnFailureListener{ exception->
                        Log.d("TAG","El analizador de codigo tiene esta excepcion:$exception")
                    }
                    .addOnCompleteListener{
                        image.close()
                    }
            }
            lastAnalyzedTimestamp = currentTimestamp
        }else{
            image.close()
        }
    }
}

