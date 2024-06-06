package una.si.cadastroprojetoa3.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import una.si.cadastroprojetoa3.entity.Address
import una.si.cadastroprojetoa3.entity.Customer
import una.si.cadastroprojetoa3.exception.BusinessException
import una.si.cadastroprojetoa3.repository.CustomerRepository
import una.si.cadastroprojetoa3.service.impl.CustomerService
import org.assertj.core.api.Assertions
import org.assertj.core.api.AtomicReferenceFieldUpdaterAssert
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.util.*

//@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CustomerServiceTest {
  @MockK lateinit var customerRepository: CustomerRepository
  @InjectMockKs lateinit var customerService: CustomerService

  @Test
  fun `should create customer`(){
    //given
    val fakeCustomer: Customer = buildCustomer()
    every { customerRepository.save(any()) } returns fakeCustomer
    //when
    val actual: Customer = customerService.save(fakeCustomer)
    //then
    Assertions.assertThat(actual).isNotNull
    Assertions.assertThat(actual).isSameAs(fakeCustomer)
    verify(exactly = 1) { customerRepository.save(fakeCustomer) }
  }

  @Test
  fun `should find customer by id`() {
    //given
    val fakeId: Long = Random().nextLong()
    val fakeCustomer: Customer = buildCustomer(id = fakeId)
    every { customerRepository.findById(fakeId) } returns Optional.of(fakeCustomer)
    //when
    val actual: Customer = customerService.findById(fakeId)
    //then
    Assertions.assertThat(actual).isNotNull
    Assertions.assertThat(actual).isExactlyInstanceOf(Customer::class.java)
    Assertions.assertThat(actual).isSameAs(fakeCustomer)
    verify(exactly = 1) { customerRepository.findById(fakeId) }
  }

  @Test
  fun `should not find customer by invalid id and throw BusinessException`() {
    //given
    val fakeId: Long = Random().nextLong()
    every { customerRepository.findById(fakeId) } returns Optional.empty()
    //when
    //then
    Assertions.assertThatExceptionOfType(BusinessException::class.java)
      .isThrownBy { customerService.findById(fakeId) }
      .withMessage("ID $fakeId não encontrado")
    verify(exactly = 1) { customerRepository.findById(fakeId) }
  }

  @Test
  fun `should delete customer by id`() {
    //given
    val fakeId: Long = Random().nextLong()
    val fakeCustomer: Customer = buildCustomer(id = fakeId)
    every { customerRepository.findById(fakeId) } returns Optional.of(fakeCustomer)
    every { customerRepository.delete(fakeCustomer) } just runs
    //when
    customerService.delete(fakeId)
    //then
    verify(exactly = 1) { customerRepository.findById(fakeId) }
    verify(exactly = 1) { customerRepository.delete(fakeCustomer) }
  }


  companion object {
    fun buildCustomer(
      nome: String = "Ana Luiza",
      cpf_cnpj: String = "014.712.490-50",
      email: String = "analuiza@gmail.com",
      senha: String = "12345678",
      telefone: String = "991234567",
      cep: String = "38000-000",
      rua: String = "Rua Tal, 10",
      bairro: String = "Jaraguá",
      cidade: String = "Uberlândia",
      estado: String = "MG",
      id: Long = 1L
    ) = Customer(
      nome = nome,
      cpf_cnpj = cpf_cnpj,
      email = email,
      senha = senha,
      telefone = telefone,
      address = Address(
        cep = cep,
        rua = rua,
        bairro = bairro,
        cidade = cidade,
        estado = estado,
      ),
      id = id
    )
  }
}