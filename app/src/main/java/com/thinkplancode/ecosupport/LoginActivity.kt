package com.thinkplancode.ecosupport

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextDocument: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)

        buttonLogin.setOnClickListener {
            login()
        }
        supportActionBar?.hide()
    }

    private fun login() {
        val email = editTextEmail.text.toString()
        val senha = editTextPassword.text.toString()

        // Verifica se os campos de email e senha estão vazios
        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

        // Verifica se o formato do email é válido
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Por favor, insira um email válido.", Toast.LENGTH_SHORT).show()
            return
        }

        // Agora, faz a requisição de login apenas se os campos estiverem preenchidos corretamente
        val endpoint = "https://ecosupport-production.up.railway.app/usuarios"
        fazerRequisicaoLogin(endpoint, email, senha)
    }

    private fun fazerRequisicaoLogin(endpoint: String, email: String, senha: String) {
        val request = Request.Builder()
            .url(endpoint)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val intentError = Intent(this@LoginActivity, ErrorLoginActivity::class.java)
                startActivity(intentError)
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        val intentError = Intent(this@LoginActivity, ErrorLoginActivity::class.java)
                        startActivity(intentError)
                        throw IOException("Unexpected code $response")
                    }

                    // Trate a resposta bem-sucedida aqui...
                    val responseData = response.body?.string()
                    val jsonObject = JSONObject(responseData)

                    // Obtém a lista de usuários
                    val embeddedObject = jsonObject.optJSONObject("_embedded")
                    val usuarioList = embeddedObject?.optJSONArray("usuarioList")

                    // Verifica se a lista de usuários não é nula e se há elementos na lista
                    if (usuarioList != null && usuarioList.length() > 0) {
                        // Itera sobre a lista de usuários
                        for (i in 0 until usuarioList.length()) {
                            val usuario = usuarioList.optJSONObject(i)
                            val usuarioEmail = usuario.optString("email")
                            val usuarioSenha = usuario.optString("senha")

                            // Verifica se o email e a senha coincidem
                            if (email == usuarioEmail && senha == usuarioSenha) {
                                // Redireciona para a atividade apropriada com base no tipo de cadastro
                                val tipo = usuario.optString("tipo")
                                val intent: Intent? = when (tipo) {
                                    "pf" -> {
                                        val pessoaFisica = usuario.optJSONObject("pessoaFisica")
                                        if (pessoaFisica != null) {
                                            Intent(this@LoginActivity, IndividualDashboardActivity::class.java)
                                        } else {
                                            null
                                        }
                                    }
                                    "empresa" -> {
                                        val empresa = usuario.optJSONObject("empresa")
                                        if (empresa != null) {
                                            Intent(this@LoginActivity, DashboardActivity::class.java)
                                        } else {
                                            null
                                        }
                                    }
                                    "instituicao" -> {
                                        val instituicao = usuario.optJSONObject("instituicao")
                                        if (instituicao != null) {
                                            Intent(this@LoginActivity, ServiceDashboardActivity::class.java)
                                        } else {
                                            null
                                        }
                                    }
                                    else -> null
                                }

                                if (intent != null) {
                                    runOnUiThread {
                                        Toast.makeText(this@LoginActivity, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                                        startActivity(intent)
                                    }
                                } else {
                                    runOnUiThread {
                                        Toast.makeText(this@LoginActivity, "Tipo de usuário desconhecido.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                return
                            }
                        }
                    }

                    // Se o loop terminar sem encontrar um usuário correspondente, exiba uma mensagem de erro
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Email ou senha incorretos.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

}
