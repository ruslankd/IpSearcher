package ru.kabirov.searcherbyip.domain

import kotlinx.coroutines.flow.Flow
import ru.kabirov.data.api.IpAddressRepository
import ru.kabirov.data.model.RequestResult
import ru.kabirov.data.model.Subnet
import javax.inject.Inject

class GetSubnetUseCase @Inject constructor(
    private val ipAddressRepository: IpAddressRepository,
) {
    operator fun invoke(ipAddress: String): Flow<RequestResult<Subnet>> {
        return ipAddressRepository
            .getSubnetByIp(ipAddress)
    }
}