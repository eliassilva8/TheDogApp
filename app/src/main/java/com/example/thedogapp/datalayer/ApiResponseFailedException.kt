package com.example.thedogapp.datalayer

class ApiResponseFailedException(message: String): Exception(message)
class ApiEmptyResponseException(message: String): Exception(message)