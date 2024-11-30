package com.example.stamp.exception

class OrderNotFoundException(origin: Long) : ResponseException(ExceptionCode.NOT_FOUND, origin.toString())
