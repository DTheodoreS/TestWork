@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS")

package tests

import BufferReader
import byteToLongArrayPosition
import ipByteArrayToString
import longArrayPositionToByte
import java.io.File
import java.io.FileInputStream

fun main() {
    convertByteToLongArrayTest()
    bufferReadFromStringTest()
    bufferReadFromFileTest()
    ipByteArrayToStringTest()
    fileReadTest()
}

fun convertByteToLongArrayTest() {
    print("convertByteToLongArrayTest ... ")
    var result = true
    for (i in 0..255) {
        val a =  byteToLongArrayPosition(i)
        val b = longArrayPositionToByte(a)

        if (b != i) {
            result = false
        }
    }
    if (result)
        println("Success")
    else
        println("Error")
}

fun ipByteArrayToStringTest() {
    print("ipByteArrayToStringTest ... ")
    val byteArray: UByteArray = ubyteArrayOf(145u, 34u, 6u, 124u)
    val ip = ipByteArrayToString(byteArray)
    if (ip == "145.34.6.124")
        println("Success")
    else
        println("Error")
}

fun bufferReadFromStringTest() {
    print("bufferReadFromStringTest ... ")

    if (!readStringUseBufferReader("145.67.23.4\r\n123.106.207.255\n", arrayOf("145.67.23.4","123.106.207.255"))) {
        println("Error")
        return
    }

    if (!readStringUseBufferReader("145\r.67.23.4\r\n123.106.207.255", arrayOf("123.106.207.255"))) {
        println("Error")
        return
    }

    if (!readStringUseBufferReader("14\r5.67.23.4\r\n123.106.207.", arrayOf("5.67.23.4"))) {
        println("Error")
        return
    }

    if (!readStringUseBufferReader("14\r5.67.23\r\n123.106.207.6", arrayOf("123.106.207.6"))) {
        println("Error")
        return
    }

    println("Success")
}

fun bufferReadFromFileTest() {
    print("bufferReadFromFileTest ... ")

    val path = "src/resources/BufferReaderTest.txt"

    val ipList = arrayOf(
            "145.67.23.4",
            "2.2.5.5",
            "70.99.0.0",
            "255.255.255.255",
            "13.106.27.2",
            "0.0.0.0"
    )

    if (!readFileUseBufferReader(path, ipList)) {
        println("Error")
        return
    }

    println("Success")
}

private fun readStringUseBufferReader(content: String, ipList: Array<String>): Boolean {
    val bufferReader = BufferReader()
    bufferReader.returnResolvedIPAddressAsArray = true

    val buffer = content.toByteArray()
    val bufferSize = buffer.size

    var addresses = bufferReader.readBuffer(
            buffer,
            bufferSize,
            ::noop)
    val lastAddress = bufferReader.getIpFromBuffer()
    if (lastAddress != null)
        addresses += lastAddress

    if (addresses.size != ipList.size) {
        return false
    }

    for (i in addresses.indices) {
        val address = addresses[i]
        val ip = ipByteArrayToString(address)
        if (ip != ipList[i]) {
            return false
        }
    }

    return true
}

@Suppress("SameParameterValue")
private fun readFileUseBufferReader(filePath: String, ipList: Array<String>): Boolean {
    val buffer = ByteArray(100)
    val bufferReader = BufferReader()
    bufferReader.returnResolvedIPAddressAsArray = true
    var addresses: Array<UByteArray> = emptyArray()

    val file = File(filePath)

    var stream: FileInputStream? = null
    try {
        stream = FileInputStream(file)

        var bufferSize = stream.read(buffer)
        while (bufferSize != -1) {
            addresses += bufferReader.readBuffer(
                    buffer,
                    bufferSize,
                    ::noop
            )

            bufferSize = stream.read(buffer)
        }

    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        stream?.close()
    }

    val lastAddress = bufferReader.getIpFromBuffer()
    if (lastAddress != null)
        addresses += lastAddress

    if (addresses.size != ipList.size) {
        return false
    }

    for (i in addresses.indices) {
        val address = addresses[i]
        val ip = ipByteArrayToString(address)
        if (ip != ipList[i]) {
            return false
        }
    }

    return true
}

private fun noop(address: UByteArray) { }