@ExperimentalUnsignedTypes
class IPAddressReader {

    private val bufferReader = BufferReader()

    private var fileReader = FileReader("")

    fun read(filePath: String) {

        val a = ULongArray(256*256*256*4)

        fileReader = FileReader(filePath)
        fileReader.showStartMessage()
        fileReader.read(::bufferReadCallback, ::percentsChangedCallback)
        fileReader.showEndMessage()

    }

    private fun percentsChangedCallback() {
        fileReader.showProgress()
    }

    private fun bufferReadCallback(buffer: ByteArray, bufferSize: Int) {
        bufferReader.readBuffer(
                buffer,
                bufferSize,
                ::ipReadCallback
        )
    }

    private fun ipReadCallback(address: UByteArray) {
        println(ipByteArrayToString(address))
    }
}