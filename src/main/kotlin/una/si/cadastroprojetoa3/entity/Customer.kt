package una.si.cadastroprojetoa3.entity

import jakarta.persistence.*

@Entity
@Table(name = "Usuario")
data class Customer(
    @Column(nullable = false) var nome: String = "",
    @Column(nullable = false, unique = true) val cpf_cnpj: String = "",
    @Column(nullable = false, unique = true) var email: String = "",
    @Column(nullable = false) var senha: String = "",
    @Column(nullable = false) @Embedded var address: Address = Address(),
    @Column(nullable = false) var telefone: String = "",
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null
)