package com.example.thedogapp.datalayer.sources

class ApiResponseFailedException(message: String): Exception(message)
class ApiEmptyResponseException(message: String): Exception(message)