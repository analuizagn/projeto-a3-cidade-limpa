package una.si.cadastroprojetoa3.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import una.si.cadastroprojetoa3.entity.Address
import una.si.cadastroprojetoa3.entity.Customer

class CustomerDto (
    @field:NotEmpty(message = "Nome inválido") val nome: String,
    @field:NotEmpty @field:Size(min = 11, max = 18,message = "CPF/CNPJ inválido") val cpf_cnpj: String,
    @field:NotEmpty @field:Email(message = "E-mail inválido") val email: String,
    @field:NotEmpty(message = "Senha inválida") val senha: String,
    @field:NotEmpty(message = "Telefone inválido") val telefone: String,
    @field:NotEmpty(message = "CEP inválido") val cep: String,
    @field:NotEmpty(message = "Rua inválida") val rua: String,
    @field:NotEmpty(message = "Bairro inválido") val bairro: String,
    @field:NotEmpty(message = "Cidade inválida") val cidade: String,
    @field:NotEmpty(message = "Estado inválido") val estado: String
) {
    fun toEntity(): Customer = Customer(
        nome = this.nome,
        cpf_cnpj = this.cpf_cnpj,
        email = this.email,
        senha = this.senha,
        telefone = this.telefone,
        address = Address(
            cep = this.cep,
            rua = this.rua,
            bairro = this.bairro,
            cidade = this.cidade,
            estado = this.estado
        )
    )
}
