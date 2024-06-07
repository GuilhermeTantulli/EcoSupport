package com.thinkplancode.ecosupport

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class SignUpActivity : AppCompatActivity() {

    private lateinit var spinnerType: Spinner
    private lateinit var layoutIndividual: ViewGroup
    private lateinit var layoutInstitution: ViewGroup
    private lateinit var layoutServiceProvider: ViewGroup
    private lateinit var editTextCPF: EditText
    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmPassword: EditText

    private lateinit var editTextDocumentInstitution: EditText
    private lateinit var editTextRazaoSocialInstitution: EditText
    private lateinit var editTextEmailInstitution: EditText
    private lateinit var editTextPasswordInstitution: EditText
    private lateinit var editTextConfirmPasswordInstitution: EditText

    private lateinit var editTextDocumentServiceProvider: EditText
    private lateinit var editTextRazaoSocialServiceProvider: EditText
    private lateinit var editTextEmailServiceProvider: EditText
    private lateinit var editTextPasswordServiceProvider: EditText
    private lateinit var editTextConfirmPasswordServiceProvider: EditText

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        spinnerType = findViewById(R.id.spinnerType)
        layoutIndividual = findViewById(R.id.layoutIndividual)
        layoutInstitution = findViewById(R.id.layoutInstitution)
        layoutServiceProvider = findViewById(R.id.layoutServiceProvider)

        editTextCPF = findViewById(R.id.editTextCPF)
        editTextName = findViewById(R.id.editTextName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword)

        editTextDocumentInstitution = findViewById(R.id.editTextDocumentInstitution)
        editTextRazaoSocialInstitution = findViewById(R.id.editTextRazaoSocialInstitution)
        editTextEmailInstitution = findViewById(R.id.editTextEmailInstitution)
        editTextPasswordInstitution = findViewById(R.id.editTextPasswordInstitution)
        editTextConfirmPasswordInstitution = findViewById(R.id.editTextConfirmPasswordInstitution)

        editTextDocumentServiceProvider = findViewById(R.id.editTextDocumentServiceProvider)
        editTextRazaoSocialServiceProvider = findViewById(R.id.editTextRazaoSocialServiceProvider)
        editTextEmailServiceProvider = findViewById(R.id.editTextEmailServiceProvider)
        editTextPasswordServiceProvider = findViewById(R.id.editTextPasswordServiceProvider)
        editTextConfirmPasswordServiceProvider = findViewById(R.id.editTextConfirmPasswordServiceProvider)

        // Criação do Spinner para seleção do tipo de cadastro
        spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> showIndividualLayout()
                    1 -> showInstitutionLayout()
                    2 -> showServiceProviderLayout()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        spinnerType = findViewById(R.id.spinnerType)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.type_array,
            R.layout.spinner_item
        )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        spinnerType.adapter = adapter

        // Configurar a máscara para CPF
        setupDocumentMask(editTextCPF)

        // Configurar a máscara para CNPJ da Instituição
        setupDocumentMask(editTextDocumentInstitution)

        // Configurar a máscara para CNPJ do Prestador de Serviço
        setupDocumentMask(editTextDocumentServiceProvider)

        // Cadastro de cada tipo atribuído ao seu botão específico
        findViewById<Button>(R.id.buttonCadastrarIndividual).setOnClickListener {
            cadastrarIndividual()
        }

        findViewById<Button>(R.id.buttonCadastrarInstitution).setOnClickListener {
            cadastrarInstituicao()
        }

        findViewById<Button>(R.id.buttonCadastrarServiceProvider).setOnClickListener {
            cadastrarPrestadorServico()
        }

        supportActionBar?.hide()
    }

    private fun showIndividualLayout() {
        layoutIndividual.visibility = View.VISIBLE
        layoutInstitution.visibility = View.GONE
        layoutServiceProvider.visibility = View.GONE
    }

    private fun showInstitutionLayout() {
        layoutIndividual.visibility = View.GONE
        layoutInstitution.visibility = View.VISIBLE
        layoutServiceProvider.visibility = View.GONE
    }

    private fun showServiceProviderLayout() {
        layoutIndividual.visibility = View.GONE
        layoutInstitution.visibility = View.GONE
        layoutServiceProvider.visibility = View.VISIBLE
    }

    private fun cadastrarIndividual() {
        val cpf = editTextCPF.text.toString()
        val nome = editTextName.text.toString()
        val email = editTextEmail.text.toString()
        val senha = editTextPassword.text.toString()
        val confirmarSenha = editTextConfirmPassword.text.toString()

        if (cpf.isEmpty() || nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

//        if (!isValidCPF(cpf)) {
//            Toast.makeText(this, "CPF inválido", Toast.LENGTH_SHORT).show()
//            return
//        }

        if (senha != confirmarSenha) {
            Toast.makeText(this, "As senhas não coincidem.", Toast.LENGTH_SHORT).show()
            return
        }

        // Implemente a lógica de validação e comunicação com a API aqui...
        val endpoint = "https://ecosupport-production.up.railway.app/usuarios"
        val jsonBody = JSONObject().apply {
            put("nome", nome)
            put("email", email)
            put("senha", senha)
            put("tipo", "pf")
            put("empresa", JSONObject.NULL)
            put("instituicao", JSONObject.NULL)
            put("pessoaFisica", JSONObject().apply {
                put("id", 1)
            })
        }
        fazerRequisicao(endpoint, jsonBody)
        Log.d("SignUpActivity", "CPF: $cpf")
        Log.d("SignUpActivity", "Nome: $nome")
        Log.d("SignUpActivity", "Email: $email")
        Log.d("SignUpActivity", "Senha: $senha")
        Log.d("SignUpActivity", "Confirmar Senha: $confirmarSenha")

    }

    private fun cadastrarInstituicao() {
        val cnpj = editTextDocumentInstitution.text.toString()
        val razaoSocial = editTextRazaoSocialInstitution.text.toString()
        val email = editTextEmailInstitution.text.toString()
        val senha = editTextPasswordInstitution.text.toString()
        val confirmarSenha = editTextConfirmPasswordInstitution.text.toString()

        if (cnpj.isEmpty() || razaoSocial.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

//        if (!isValidCNPJ(cnpj)) {
//            Toast.makeText(this, "CNPJ inválido", Toast.LENGTH_SHORT).show()
//            return
//        }

        if (senha != confirmarSenha) {
            Toast.makeText(this, "As senhas não coincidem.", Toast.LENGTH_SHORT).show()
            return
        }

        // Implemente a lógica de validação e comunicação com a API aqui...
        val endpoint = "https://ecosupport-production.up.railway.app/usuarios"
        val jsonBody = JSONObject().apply {
            put("nome", razaoSocial)
            put("email", email)
            put("senha", senha)
            put("tipo", "instituicao")
            put("empresa", JSONObject.NULL)
            put("instituicao", JSONObject().apply {
                put("id", 1)
            })
            put("pessoaFisica", JSONObject.NULL)
        }
        fazerRequisicao(endpoint, jsonBody)
    }

    private fun cadastrarPrestadorServico() {
        val cnpj = editTextDocumentServiceProvider.text.toString()
        val razaoSocial = editTextRazaoSocialServiceProvider.text.toString()
        val email = editTextEmailServiceProvider.text.toString()
        val senha = editTextPasswordServiceProvider.text.toString()
        val confirmarSenha = editTextConfirmPasswordServiceProvider.text.toString()

        if (cnpj.isEmpty() || razaoSocial.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

//        if (!isValidCNPJ(cnpj)) {
//            Toast.makeText(this, "CNPJ inválido", Toast.LENGTH_SHORT).show()
//            return
//        }

        if (senha != confirmarSenha) {
            Toast.makeText(this, "As senhas não coincidem.", Toast.LENGTH_SHORT).show()
            return
        }

        // Implemente a lógica de validação e comunicação com a API aqui...
        val endpoint = "https://ecosupport-production.up.railway.app/usuarios"
        val jsonBody = JSONObject().apply {
            put("nome", razaoSocial)
            put("email", email)
            put("senha", senha)
            put("tipo", "empresa")
            put("empresa", JSONObject().apply {
                put("id", 1)
            })
            put("instituicao", JSONObject.NULL)
            put("pessoaFisica", JSONObject.NULL)
        }
        fazerRequisicao(endpoint, jsonBody)
    }


    private fun fazerRequisicao(endpoint: String, jsonBody: JSONObject) {
        val request = Request.Builder()
            .url(endpoint)
            .post(jsonBody.toString().toRequestBody("application/json".toMediaTypeOrNull()))
            .build()


        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Erro ao cadastrar. Verifique sua conexão com a internet e tente novamente.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        val intentError = Intent(this@SignUpActivity, ErrorSignUpActivity::class.java)
                        startActivity(intentError)
                        throw IOException("Unexpected code $response")
                    } else if (response.isSuccessful) {
                        runOnUiThread {
                            Toast.makeText(this@SignUpActivity, "Cadastro bem-sucedido!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    val responseData = response.body?.string()
                }
            }
        })
    }

    private fun setupDocumentMask(editText: EditText) {
        editText.filters = arrayOf(InputFilter.LengthFilter(18))

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Nada a ser feito aqui
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nada a ser feito aqui
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let { it ->
                    val cleaned = it.filter { it.isDigit() } // Remove tudo exceto os dígitos
                    val formatted = if (cleaned.length <= 11) {
                        formatCpf(cleaned)
                    } else {
                        formatCnpj(cleaned)
                    }
                    editText.removeTextChangedListener(this)
                    editText.setText(formatted)
                    editText.setSelection(formatted.length)
                    editText.addTextChangedListener(this)
                }
            }
        })
    }

    // Função para aplicar a máscara de CPF
    private fun formatCpf(input: CharSequence): String {
        val formatted = StringBuilder(input)

        if (input.length >= 3) {
            formatted.insert(3, ".")
        }
        if (input.length >= 7) {
            formatted.insert(7, ".")
        }
        if (input.length >= 11) {
            formatted.insert(11, "-")
        }

        return formatted.toString()
    }

    // Função para aplicar a máscara de CNPJ
    private fun formatCnpj(input: CharSequence): String {
        val formatted = StringBuilder(input)

        if (input.length >= 2) {
            formatted.insert(2, ".")
        }
        if (input.length >= 6) {
            formatted.insert(6, ".")
        }
        if (input.length >= 10) {
            formatted.insert(10, "/")
        }
        if (input.length >= 12) {
            formatted.insert(15, "-")
        }

        return formatted.toString()
    }
}
    // Tratativas de validacao de CPF e CNPJ - removidas para aumentar capacidade de testes
//    private fun isValidCPF(cpf: String): Boolean {
//        if (cpf.length != 11 || cpf.any { !it.isDigit() }) {
//            return false
//        }
//
//        val cpfNumbers = cpf.substring(0, 9).map { it.toString().toInt() }
//        val firstVerifierDigit = cpf.substring(9, 10).toInt()
//        val secondVerifierDigit = cpf.substring(10, 11).toInt()
//
//        var sum = 0
//        var multiplier = 10
//        for (i in 0 until 9) {
//            sum += cpfNumbers[i] * multiplier
//            multiplier--
//        }
//
//        var remainder = sum % 11
//        var digit = if (remainder < 2) 0 else 11 - remainder
//        if (digit != firstVerifierDigit) {
//            return false
//        }
//
//        sum = 0
//        multiplier = 11
//        for (i in 0 until 10) {
//            sum += cpfNumbers[i] * multiplier
//            multiplier--
//        }
//
//        remainder = sum % 11
//        digit = if (remainder < 2) 0 else 11 - remainder
//        return digit == secondVerifierDigit
//    }
//
//    private fun isValidCNPJ(cnpj: String): Boolean {
//        if (cnpj.length != 14 || cnpj.any { !it.isDigit() }) {
//            return false
//        }
//
//        val cnpjNumbers = cnpj.substring(0, 12).map { it.toString().toInt() }
//        val firstVerifierDigit = cnpj.substring(12, 13).toInt()
//        val secondVerifierDigit = cnpj.substring(13, 14).toInt()
//
//        var sum = 0
//        var multiplier = 5
//        for (i in 0 until 12) {
//            sum += cnpjNumbers[i] * multiplier
//            multiplier--
//            if (multiplier < 2) {
//                multiplier = 9
//            }
//        }
//
//        var remainder = sum % 11
//        var digit = if (remainder < 2) 0 else 11 - remainder
//        if (digit != firstVerifierDigit) {
//            return false
//        }
//
//        sum = 0
//        multiplier = 6
//        for (i in 0 until 13) {
//            sum += cnpjNumbers[i] * multiplier
//            multiplier--
//            if (multiplier < 2) {
//                multiplier = 9
//            }
//        }
//
//        remainder = sum % 11
//        digit = if (remainder < 2) 0 else 11 - remainder
//        return digit == secondVerifierDigit
//    }


