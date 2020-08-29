@file:Suppress("EXPERIMENTAL_API_USAGE")

package tests

import BufferReader
import ipByteArrayToString
import FileReader

fun main() {
    fileReadTest()
}

fun fileReadTest() {
    print("fileReadTest ... ")

    val path = "src/resources/BufferReaderTest.txt"

    val ipList = arrayOf(
            "145.67.23.4",
            "2.2.5.5",
            "70.99.0.0",
            "255.255.255.255",
            "13.106.27.2",
            "0.0.0.0"
    )

    val fileReaderTest = FileReaderTest()
    val ipAddresses = fileReaderTest.read(path)

    if (ipAddresses.size != ipList.size) {
        println("Error")
        return
    }

    for ((index, address) in ipAddresses.withIndex()) {
        if (address != ipList[index]) {
            println("Error")
            return
        }
    }

    println("Success")
}

@ExperimentalUnsignedTypes
class FileReaderTest {

    private val bufferReader = BufferReader()

    private var fileReader = FileReader("")

    private var addresses: Array<String> = emptyArray()

    fun read(filePath: String): Array<String> {

        addresses = emptyArray()

        fileReader = FileReader(filePath)
        fileReader.read(
                ::bufferReadCallback,
                ::percentsChangedCallback)

        return addresses
    }

    private fun percentsChangedCallback() {
    }

    private fun bufferReadCallback(buffer: ByteArray, bufferSize: Int) {
        bufferReader.readBuffer(
                buffer,
                bufferSize,
                ::ipReadCallback)
    }

    private fun ipReadCallback(address: UByteArray) {
        addresses += ipByteArrayToString(address)
    }
}