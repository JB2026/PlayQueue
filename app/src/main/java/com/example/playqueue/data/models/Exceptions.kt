package com.example.playqueue.data.models

/**
 * An exception to handle an error from the BGG API.
 */
open class BggException(message: String, cause: Throwable? = null): Exception(message, cause)

/**
 * An exception to handle an error around the network like timeout or no connection.
 */
class BggNetworkException(message: String, cause: Throwable? = null): BggException(message, cause)

/**
 * An exception to handle an error around the auth token sent to the API.
 */
class BggAuthException(message: String): BggException(message)

/**
 * An exception to handle an error around the XML parsing with an API response.
 */
class BggParsingException(message: String, cause: Throwable? = null): BggException(message, cause)