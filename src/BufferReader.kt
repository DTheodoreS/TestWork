@ExperimentalUnsignedTypes
class BufferReader {
    private var buffer: ByteArray = ByteArray(0)
    private var bufferSize: Int = 0

    private var addresses: Array<UByteArray> = emptyArray()

    private var ipAccumulator: UByteArray = UByteArray(4)
    private var ipAccumulatorIndex: Int = 0

    private var byteAccumulator: UByteArray = UByteArray(3)
    private var byteAccumulatorIndex: Int = 0

    private val zero: Byte = 48
    private val dot: Byte = 46
    private val retCur: Byte = 13
    private val newLine: Byte = 10

    private var parseIPCallback: (UByteArray) -> Unit = { }

    var returnResolvedIPAddressAsArray: Boolean = false

    fun readBuffer(buffer: ByteArray, bufferSize: Int, callback: (address: UByteArray) -> Unit) : Array<UByteArray> {

        parseIPCallback = callback

        this.buffer = buffer
        this.bufferSize = bufferSize
        addresses = emptyArray()

        for (i in 0 until bufferSize) {
            analysis(buffer[i])
        }

        return addresses
    }

    fun getIpFromBuffer(): UByteArray? {
        dotCommit()

        if (ipAccumulatorIndex != 4) {
            return null
        }

        val ip = cloneUByteArray(ipAccumulator)
        resetIp()
        return ip
    }

    private fun analysis(byte: Byte) {
        if (byte in 48..57) {
            digitCommit(byte)
            return
        }

        if (byte == dot) {
            dotCommit()
            return
        }


        if (byte == retCur || byte == newLine) {
            saveIp()
            return
        }

        error()
    }

    private fun digitCommit(byte: Byte) {
        if (byteAccumulatorIndex == 3) {
            error()
            return
        }

        byteAccumulator[byteAccumulatorIndex] = (byte - zero).toUByte()
        byteAccumulatorIndex++
    }

    private fun dotCommit() {
        if (byteAccumulatorIndex == 0) {
            error()
            return
        }

        val byte: UByte = when(byteAccumulatorIndex){
            3 -> (byteAccumulator[0] * 100u + byteAccumulator[1] * 10u + byteAccumulator[2]).toUByte()
            2 -> (byteAccumulator[0] * 10u + byteAccumulator[1]).toUByte()
            1 -> byteAccumulator[0]
            else -> 0u
        }

        if (ipAccumulatorIndex == 4) {
            error()
            return
        }

        ipAccumulator[ipAccumulatorIndex] = byte
        ipAccumulatorIndex++

        resetByteAccumulator()
    }

    private fun saveIp() {
        dotCommit()
        if (ipAccumulatorIndex != 4) {
            error()
            return
        }

        parseIPCallback(ipAccumulator)
        if (returnResolvedIPAddressAsArray)
            addresses += cloneUByteArray(ipAccumulator)

        resetIp()
    }

    private fun resetIp() {
        resetByteAccumulator()

        ipAccumulatorIndex = 0
        for (i in 0..3) {
            ipAccumulator[i] = 0u
        }
    }

    private fun resetByteAccumulator() {
        byteAccumulatorIndex = 0
        for (i in 0..2) {
            byteAccumulator[i] = 0u
        }
    }

    private fun error() {
        resetIp()
    }
}