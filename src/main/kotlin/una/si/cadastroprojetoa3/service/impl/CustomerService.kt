package una.si.cadastroprojetoa3.service.impl

import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.stereotype.Service
import una.si.cadastroprojetoa3.entity.Customer
import una.si.cadastroprojetoa3.exception.BusinessException
import una.si.cadastroprojetoa3.repository.CustomerRepository
import una.si.cadastroprojetoa3.service.ICustomerService

@Service
class CustomerService(
    private val customerRepository: CustomerRepository
) : ICustomerService {
    override fun save(customer: Customer): Customer =
        this.customerRepository.save(customer)


    override fun findById(id: Long): Customer =
        this.customerRepository.findById(id).orElseThrow {
            throw BusinessException("ID $id não encontrado")

        }

    override fun delete(id: Long) {
        val customer: Customer = this.findById(id)
        this.customerRepository.delete(customer)
    }
}