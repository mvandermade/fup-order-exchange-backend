package com.example.stamp.exception

import org.springframework.http.HttpStatus

class StampNotFoundException(origin: Long) : ResponseException(HttpStatus.NOT_FOUND, origin.toString())
