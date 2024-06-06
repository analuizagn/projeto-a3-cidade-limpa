package una.si.cadastroprojetoa3.dto

import jakarta.validation.constraints.NotEmpty
import una.si.cadastroprojetoa3.entity.Customer

data class CustomerUpdateDto(
    @field:NotEmpty(message = "Nome inválido") val nome: String,
    @field:NotEmpty(message = "Telefone inválido") val telefone: String,
    @field:NotEmpty(message = "CEP inválido") val cep: String,
    @field:NotEmpty(message = "Rua inválida") val rua: String,
    @field:NotEmpty(message = "Bairro inválido") val bairro: String,
    @field:NotEmpty(message = "Cidade inválida") val cidade: String,
    @field:NotEmpty(message = "Estado inválido") val estado: String
) {
    fun toEntity(customer: Customer): Customer {
        customer.nome = this.nome
        customer.telefone = this.telefone
        customer.address.cep = this.cep
        customer.address.rua = this.rua
        customer.address.bairro = this.bairro
        customer.address.cidade = this.cidade
        customer.address.estado = this.estado
        return customer
    }
}
