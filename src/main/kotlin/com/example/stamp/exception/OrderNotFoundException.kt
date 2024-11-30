package com.example.stamp.exception

import org.springframework.http.HttpStatus

class OrderNotFoundException(origin: Long) : ResponseException(HttpStatus.NOT_FOUND, origin.toString())
