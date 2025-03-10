package io.github.devcrocod.example.playground.services

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.JsonProperty
import io.github.devcrocod.example.playground.data.BookingStatus
import io.github.devcrocod.example.playground.services.BookingTools.BookingDetails
import io.github.devcrocod.example.playground.services.BookingTools.BookingDetailsRequest
import io.github.devcrocod.example.playground.services.BookingTools.Companion.logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Description
import org.springframework.core.NestedExceptionUtils
import java.time.LocalDate
import java.util.function.Function

@Configuration
class BookingTools {
    companion object {
        private val logger = LoggerFactory.getLogger(BookingTools::class.java)
    }

    @Autowired
    private lateinit var flightBookingService: FlightBookingService

    data class BookingDetailsRequest constructor(
        val bookingNumber: String,
        val firstName: String,
        val lastName: String
    )

    data class ChangeBookingDatesRequest constructor(
        val bookingNumber: String,
        val firstName: String,
        val lastName: String,
        val date: String,
        val from: String,
        val to: String
    )

    data class CancelBookingRequest constructor(
        val bookingNumber: String,
        val firstName: String,
        val lastName: String
    )

    data class BookingDetails constructor(
        val bookingNumber: String,
        val firstName: String,
        val lastName: String,
        val date: LocalDate?,
        val bookingStatus: BookingStatus?,
        val from: String?,
        val to: String?,
        val bookingClass: String?
    )

    @Bean
    @Description("Get booking details")
    fun getBookingDetails(): Function<BookingDetailsRequest, BookingDetails> {
        return Function { request ->
            try {
                flightBookingService.getBookingDetails(request.bookingNumber, request.firstName, request.lastName)
            } catch (e: Exception) {
                logger.warn("Booking details: {}", NestedExceptionUtils.getMostSpecificCause(e).message)
                BookingDetails(
                    request.bookingNumber,
                    request.firstName,
                    request.lastName,
                    null, null, null, null, null
                )
            }
        }
    }

    @Bean
    @Description("Change booking dates")
    fun changeBooking(): Function<ChangeBookingDatesRequest, String> {
        return Function { request ->
            flightBookingService.changeBooking(
                request.bookingNumber,
                request.firstName,
                request.lastName,
                request.date,
                request.from,
                request.to
            )
            ""
        }
    }

    @Bean
    @Description("Cancel booking")
    fun cancelBooking(): Function<CancelBookingRequest, String> {
        return Function { request ->
            flightBookingService.cancelBooking(request.bookingNumber, request.firstName, request.lastName)
            ""
        }
    }
}