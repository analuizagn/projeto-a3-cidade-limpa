package una.si.cadastroprojetoa3.dto

import una.si.cadastroprojetoa3.entity.Customer

data class CustomerView(
    val nome: String,
    val cpf_cnpj: String,
    val email: String,
    val telefone: String,
    val cep: String,
    val rua: String,
    val bairro: String,
    val cidade: String,
    val estado: String,
    val id: Long?
) {
    constructor(customer: Customer) : this(
        nome = customer.nome,
        cpf_cnpj = customer.cpf_cnpj,
        email = customer.email,
        telefone = customer.telefone,
        cep = customer.address.cep,
        rua = customer.address.rua,
        bairro = customer.address.bairro,
        cidade = customer.address.cidade,
        estado = customer.address.estado,
        id = customer.id,
    )
}
