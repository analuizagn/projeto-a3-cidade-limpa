package una.si.cadastroprojetoa3.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class Address (
    @Column(nullable = false) var cep: String = "",
    @Column(nullable = false) var rua: String = "",
    @Column(nullable = false) var bairro: String = "",
    @Column(nullable = false) var cidade: String = "",
    @Column(nullable = false) var estado: String = ""
)
