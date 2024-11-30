package com.example.stamp.exception

import org.springframework.http.HttpStatus

class WaitingForStampException(origin: Long) : ResponseException(HttpStatus.PROCESSING, origin.toString())
