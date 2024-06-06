package una.si.cadastroprojetoa3.exception

data class BusinessException(override val message: String?) : RuntimeException(message)
