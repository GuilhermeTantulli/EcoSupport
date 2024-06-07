package com.thinkplancode.ecosupport

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class IndividualDashboardActivity : AppCompatActivity() {

    private lateinit var textViewSaldoTotal: TextView
    private lateinit var textViewContratoStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_individual_dashboard)

        textViewSaldoTotal = findViewById(R.id.textViewSaldoTotal)

        supportActionBar?.hide()

        // Suponha que esses dados venham de uma fonte de dados, como uma API ou banco de dados
        fetchData()
    }

    private fun fetchData() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getContratos()
                val contratoAtivo = response._embedded.contratoList.firstOrNull { it.status == "Em Progresso" }

                if (contratoAtivo != null) {
                    textViewSaldoTotal.text = "R$ ${contratoAtivo.valor}"
                } else {
                    textViewSaldoTotal.text = "R$ 0.0"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle the error appropriately
                textViewContratoStatus.text = "Erro ao carregar dados."
            }
        }
    }

    interface ApiService {
        @GET("/contratos")
        suspend fun getContratos(): ContratoResponse
    }

    object RetrofitClient {
        private const val BASE_URL = "https://ecosupport-production.up.railway.app/"

        val apiService: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }

    data class Empresa(
        val id: Int,
        val nome: String,
        val cnpj: String,
        val email: String,
        val telefone: String,
        val endereco: String
    )

    data class Contrato(
        val id: Int,
        val empresa: Empresa,
        val tipoContrato: String,
        val dataInicio: String,
        val dataFim: String,
        val valor: Double,
        val status: String,
        val assinaturaPendente: String
    )

    data class ContratoResponse(
        val _embedded: Embedded
    )

    data class Embedded(val contratoList: List<Contrato>)
}
