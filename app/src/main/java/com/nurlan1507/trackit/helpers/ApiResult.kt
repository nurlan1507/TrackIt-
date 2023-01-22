package com.nurlan1507.trackit.helpers


abstract class ApiResult(){}
class ApiFailure(var e: Exception):ApiResult()
class ApiSuccess(var list: Any? = null):ApiResult()
