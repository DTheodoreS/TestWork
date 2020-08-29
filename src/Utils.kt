@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS")

import java.text.SimpleDateFormat
import java.util.*

fun byteToLongArrayPosition(byte: Int): ULongArray {
    val a = ULongArray(4)
    val l: ULong
    if (byte < 128)
        if (byte < 64)
        {
            l = byteToLongPosition(byte)
            a[0] = l
        }
        else
        {
            l = byteToLongPosition(byte - 64)
            a[1] = l
        }
    else
        if (byte < 192)
        {
            l = byteToLongPosition(byte - 128)
            a[2] = l
        }
        else
        {
            l = byteToLongPosition(byte - 192)
            a[3] = l
        }
    return a
}

fun longArrayPositionToByte(array: ULongArray): Int {
    var b = 0
    for (i in 0..3) {
        val b1 = longPositionToByte(array[i])
        if (b1 > 0)
        {
            b = b1 + 64 * i
        }
    }
    b--
    return b
}

val ul1: ULong = 1.toULong()

fun byteToLongPosition(byte: Int): ULong {
    return ul1 shl byte
}

fun longPositionToByte(long: ULong): Int {
    // exclude for performance
    if (long < 1u) {
        return 0
    }

    var l = long

    var position = 0
    while (l > 0u) {
        position++
        l = l shr 1
    }

    return position
}

fun ipByteArrayToString(ip: UByteArray): String {
    return "${ip[0]}.${ip[1]}.${ip[2]}.${ip[3]}"
}

fun cloneUByteArray(uByteArray: UByteArray): UByteArray {
    val newArray = UByteArray(4)
    for (i in 0..3) {
        newArray[i] = uByteArray[i]
    }

    return newArray
}

fun formatTime(date: Date): String {
    val pattern = "HH:mm:ss"
    val simpleDateFormat = SimpleDateFormat(pattern)
    return simpleDateFormat.format(date)
}