package ru.kabirov.iporganisationselector

import java.util.regex.Pattern

class IpAddressValidator {

    companion object {
        private val IPV4_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
        )
    }

    fun isValidIpAddress(ipAddress: String): Boolean {
        val matcher = IPV4_PATTERN.matcher(ipAddress)
        return matcher.matches()
    }
}