package una.si.cadastroprojetoa3.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import una.si.cadastroprojetoa3.dto.CustomerDto
import una.si.cadastroprojetoa3.dto.CustomerUpdateDto
import una.si.cadastroprojetoa3.entity.Customer
import una.si.cadastroprojetoa3.repository.CustomerRepository
import java.util.Random

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CustomerResourceTest {
  @Autowired
  private lateinit var customerRepository: CustomerRepository

  @Autowired
  private lateinit var mockMvc: MockMvc

  @Autowired
  private lateinit var objectMapper: ObjectMapper

  companion object {
    const val URL: String = "/api/usuarios"
  }

  @BeforeEach
  fun setup() = customerRepository.deleteAll()

  @AfterEach
  fun tearDown() = customerRepository.deleteAll()

  @Test
  fun `should create a customer and return 201 status`() {
    //given
    val customerDto: CustomerDto = builderCustomerDto()
    val valueAsString: String = objectMapper.writeValueAsString(customerDto)
    //when
    //then
    mockMvc.perform(
      MockMvcRequestBuilders.post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(valueAsString)
    )
      .andExpect(MockMvcResultMatchers.status().isCreated)
      .andExpect(jsonPath("$.nome").value("Ana Luiza"))
      .andExpect(jsonPath("$.cpf_cnpj").value("014.712.490-50"))
      .andExpect(jsonPath("$.email").value("email@email.com"))
      .andExpect(jsonPath("$.telefone").value("991234567"))
      .andExpect(jsonPath("$.cep").value("38000-000"))
      .andExpect(jsonPath("$.rua").value("Rua Tal, 10"))
      .andExpect(jsonPath("$.bairro").value("Jaraguá"))
      .andExpect(jsonPath("$.cidade").value("Uberlândia"))
      .andExpect(jsonPath("$.estado").value("MG"))
      .andExpect(jsonPath("$.id").value(1))
      .andDo(MockMvcResultHandlers.print())
  }

  @Test
  fun `should not save a customer with same CPF and return 409 status`() {
    //given
    customerRepository.save(builderCustomerDto().toEntity())
    val customerDto: CustomerDto = builderCustomerDto()
    val valueAsString: String = objectMapper.writeValueAsString(customerDto)
    //when
    //then
    mockMvc.perform(
      MockMvcRequestBuilders.post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(valueAsString)
    )
      .andExpect(MockMvcResultMatchers.status().isConflict)
      .andExpect(jsonPath("$.title").value("Conflict! Consult the documentation"))
      .andExpect(jsonPath("$.timestamp").exists())
      .andExpect(jsonPath("$.status").value(409))
      .andExpect(
        jsonPath("$.exception")
          .value("class org.springframework.dao.DataIntegrityViolationException")
      )
      .andExpect(jsonPath("$.details[*]").isNotEmpty)
      .andDo(MockMvcResultHandlers.print())
  }

  @Test
  fun `should not save a customer with empty nome and return 400 status`() {
    //given
    val customerDto: CustomerDto = builderCustomerDto(nome = "")
    val valueAsString: String = objectMapper.writeValueAsString(customerDto)
    //when
    //then
    mockMvc.perform(
      MockMvcRequestBuilders.post(URL)
        .content(valueAsString)
        .contentType(MediaType.APPLICATION_JSON)
    )
      .andExpect(MockMvcResultMatchers.status().isBadRequest)
      .andExpect(jsonPath("$.title").value("Bad Request! Consult the documentation"))
      .andExpect(jsonPath("$.timestamp").exists())
      .andExpect(jsonPath("$.status").value(400))
      .andExpect(
        jsonPath("$.exception")
          .value("class org.springframework.web.bind.MethodArgumentNotValidException")
      )
      .andExpect(jsonPath("$.details[*]").isNotEmpty)
      .andDo(MockMvcResultHandlers.print())
  }

  @Test
  fun `should find customer by id and return 200 status`() {
    //given
    val customer: Customer = customerRepository.save(builderCustomerDto().toEntity())
    //when
    //then
    mockMvc.perform(
      MockMvcRequestBuilders.get("$URL/${customer.id}")
        .accept(MediaType.APPLICATION_JSON)
    )
      .andExpect(MockMvcResultMatchers.status().isOk)
      .andExpect(jsonPath("$.nome").value("Ana Luiza"))
      .andExpect(jsonPath("$.cpf_cnpj").value("014.712.490-50"))
      .andExpect(jsonPath("$.email").value("email@email.com"))
      .andExpect(jsonPath("$.telefone").value("991234567"))
      .andExpect(jsonPath("$.cep").value("38000-000"))
      .andExpect(jsonPath("$.rua").value("Rua Tal, 10"))
      .andExpect(jsonPath("$.bairro").value("Jaraguá"))
      .andExpect(jsonPath("$.cidade").value("Uberlândia"))
      .andExpect(jsonPath("$.estado").value("MG"))
      //.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
      .andDo(MockMvcResultHandlers.print())
  }

  @Test
  fun `should not find customer with invalid id and return 400 status`() {
    //given
    val invalidId: Long = 2L
    //when
    //then
    mockMvc.perform(
      MockMvcRequestBuilders.get("$URL/$invalidId")
        .accept(MediaType.APPLICATION_JSON)
    )
      .andExpect(MockMvcResultMatchers.status().isBadRequest)
      .andExpect(jsonPath("$.title").value("Bad Request! Consult the documentation"))
      .andExpect(jsonPath("$.timestamp").exists())
      .andExpect(jsonPath("$.status").value(400))
      .andExpect(
        jsonPath("$.exception")
          .value("class una.si.cadastroprojetoa3.exception.BusinessException")
      )
      .andExpect(jsonPath("$.details[*]").isNotEmpty)
      .andDo(MockMvcResultHandlers.print())
  }

  @Test
  fun `should delete customer by id and return 204 status`() {
    //given
    val customer: Customer = customerRepository.save(builderCustomerDto().toEntity())
    //when
    //then
    mockMvc.perform(
      MockMvcRequestBuilders.delete("$URL/${customer.id}")
        .accept(MediaType.APPLICATION_JSON)
    )
      .andExpect(MockMvcResultMatchers.status().isNoContent)
      .andDo(MockMvcResultHandlers.print())
  }

  @Test
  fun `should not delete customer by id and return 400 status`() {
    //given
    val invalidId: Long = Random().nextLong()
    //when
    //then
    mockMvc.perform(
      MockMvcRequestBuilders.delete("$URL/${invalidId}")
        .accept(MediaType.APPLICATION_JSON)
    )
      .andExpect(MockMvcResultMatchers.status().isBadRequest)
      .andExpect(jsonPath("$.title").value("Bad Request! Consult the documentation"))
      .andExpect(jsonPath("$.timestamp").exists())
      .andExpect(jsonPath("$.status").value(400))
      .andExpect(
        jsonPath("$.exception")
          .value("class una.si.cadastroprojetoa3.exception.BusinessException")
      )
      .andExpect(jsonPath("$.details[*]").isNotEmpty)
      .andDo(MockMvcResultHandlers.print())
  }

  @Test
  fun `should update a customer and return 200 status`() {
    //given
    val customer: Customer = customerRepository.save(builderCustomerDto().toEntity())
    val customerUpdateDto: CustomerUpdateDto = builderCustomerUpdateDto()
    val valueAsString: String = objectMapper.writeValueAsString(customerUpdateDto)
    //when
    //then
    mockMvc.perform(
      MockMvcRequestBuilders.patch("$URL?usuarioId=${customer.id}")
        .contentType(MediaType.APPLICATION_JSON)
        .content(valueAsString)
    )
      .andExpect(MockMvcResultMatchers.status().isOk)
      .andExpect(jsonPath("$.nome").value("Ana Luiza Gomes"))
      .andExpect(jsonPath("$.cpf_cnpj").value("014.712.490-50"))
      .andExpect(jsonPath("$.email").value("email@email.com"))
      .andExpect(jsonPath("$.telefone").value("991234567"))
      .andExpect(jsonPath("$.cep").value("38000-000"))
      .andExpect(jsonPath("$.rua").value("Rua Fulana, 100"))
      .andExpect(jsonPath("$.bairro").value("Canaa"))
      .andExpect(jsonPath("$.cidade").value("Uberlândia"))
      .andExpect(jsonPath("$.estado").value("MG"))
      //.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
      .andDo(MockMvcResultHandlers.print())
  }

  @Test
  fun `should not update a customer with invalid id and return 400 status`() {
    //given
    val invalidId: Long = Random().nextLong()
    val customerUpdateDto: CustomerUpdateDto = builderCustomerUpdateDto()
    val valueAsString: String = objectMapper.writeValueAsString(customerUpdateDto)
    //when
    //then
    mockMvc.perform(
      MockMvcRequestBuilders.patch("$URL?usuarioId=$invalidId")
        .contentType(MediaType.APPLICATION_JSON)
        .content(valueAsString)
    )
      .andExpect(MockMvcResultMatchers.status().isBadRequest)
      .andExpect(jsonPath("$.title").value("Bad Request! Consult the documentation"))
      .andExpect(jsonPath("$.timestamp").exists())
      .andExpect(jsonPath("$.status").value(400))
      .andExpect(
        jsonPath("$.exception")
          .value("class una.si.cadastroprojetoa3.exception.BusinessException")
      )
      .andExpect(jsonPath("$.details[*]").isNotEmpty)
      .andDo(MockMvcResultHandlers.print())
  }


  private fun builderCustomerDto(
    nome: String = "Ana Luiza",
    cpf_cnpj: String = "014.712.490-50",
    email: String = "email@email.com",
    senha: String = "12345678",
    telefone: String = "991234567",
    cep: String = "38000-000",
    rua: String = "Rua Tal, 10",
    bairro: String = "Jaraguá",
    cidade: String = "Uberlândia",
    estado: String = "MG",
  ) = CustomerDto(
    nome = nome,
    cpf_cnpj = cpf_cnpj,
    email = email,
    senha = senha,
    telefone = telefone,
    cep = cep,
    rua = rua,
    bairro = bairro,
    cidade = cidade,
    estado = estado
  )

  private fun builderCustomerUpdateDto(
    nome: String = "Ana Luiza Gomes",
    telefone: String = "991234567",
    cep: String = "38000-000",
    rua: String = "Rua Fulana, 100",
    bairro: String = "Canaa",
    cidade: String = "Uberlândia",
    estado: String = "MG"
  ): CustomerUpdateDto = CustomerUpdateDto(
    nome = nome,
    telefone = telefone,
    cep = cep,
    rua = rua,
    bairro = bairro,
    cidade = cidade,
    estado = estado
  )
}