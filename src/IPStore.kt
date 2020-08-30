@Suppress("EXPERIMENTAL_API_USAGE")
class IPStore {

    private val store = ULongArray(256*256*256*4)

    private val blockSize1Level = 256 * 256 * 4
    private val blockSize2Level = 256 * 4
    private val blockSize3Level = 4

    fun addIPAddress(address: UByteArray) {
        val ipBitPosArrayView = byteToLongArrayPosition(address[3].toInt())
        val pos = getIPBlockPosition(address)
        for (i in 0..3) {
            store[pos + i] = store[pos + i] or ipBitPosArrayView[i]
        }
    }

    @ExperimentalStdlibApi
    fun calculateCount(): Long {
        var count: Long = 0

        for (ipBitPosView in store) {
            count += ipBitPosView.countOneBits()
        }

        return count
    }

    private fun getIPBlockPosition(address: UByteArray): Int {

        val numBlock1Level = address[0].toInt()
        val numBlock2Level = address[1].toInt()
        val numBlock3Level = address[2].toInt()

        val pos: Int = blockSize1Level * numBlock1Level + blockSize2Level * numBlock2Level + blockSize3Level * numBlock3Level

        return pos
    }
}