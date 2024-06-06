package una.si.cadastroprojetoa3.repository

import una.si.cadastroprojetoa3.entity.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository: JpaRepository<Customer, Long> {
}