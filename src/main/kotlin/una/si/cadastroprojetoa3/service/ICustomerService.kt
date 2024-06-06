package una.si.cadastroprojetoa3.service;

import una.si.cadastroprojetoa3.entity.Customer

interface ICustomerService {
    fun save(customer: Customer): Customer
    fun findById(id: Long): Customer
    fun delete(id: Long)
}