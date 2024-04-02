package com.vinayakgupta3112.movieapp.movieList.domain.usecase

class MinutesToReadableTime(
    private val minutes: Int
) {
    operator fun invoke(): String {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        return String.format("%02d hr %02d min", hours, remainingMinutes)
    }
}