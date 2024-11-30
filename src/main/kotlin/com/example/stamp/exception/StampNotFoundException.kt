package com.example.stamp.exception

class StampNotFoundException(origin: Long) : ResponseException(ExceptionCode.NOT_FOUND, origin.toString())
