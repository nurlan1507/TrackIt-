package com.nurlan1507.trackit.helpers

abstract class ApiResult {}

class ApiSuccess():ApiResult(){

}
class ApiFailure(var e:Exception):ApiResult(){

}