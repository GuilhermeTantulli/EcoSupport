package com.thinkplancode.ecosupport

import android.os.Bundle
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class ServiceDashboardActivity : AppCompatActivity() {

    private lateinit var textViewContratoStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_dashboard)

        textViewContratoStatus = findViewById(R.id.textViewContratoStatus)

        // Suponha que esses dados venham de uma fonte de dados, como uma API ou banco de dados

        val contratoAtivo = isContratoAtivo()

        if (contratoAtivo) {
            textViewContratoStatus.text = "Você tem um contrato ativo."
        } else {
            textViewContratoStatus.text = "Você não tem um contrato ativo."
        }

        supportActionBar?.hide()
    }

    private fun isContratoAtivo(): Boolean {
        // Aqui você faria a lógica para verificar se há um contrato ativo
        return true // Valor exemplo
    }

}