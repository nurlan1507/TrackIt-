package com.nurlan1507.trackit.utils

class NotificationMessage {
    val message = "{" +
            "  \"to\": \"/topics/%s\"," +
            "  \"notification\": {" +
            "       \"body\":\"%s\"," +
            "       \"title\":\"%s\"" +
            "   }" +
            "}"
}