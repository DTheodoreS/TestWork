@ExperimentalUnsignedTypes
class CounterIPAddresses(filePath: String) {

    private val bufferReader = BufferReader()

    private var fileReader = FileReader("")

    private var ipStore: IPStore? = null

    private var filePath: String = ""

    init {
        this.filePath  = filePath
    }

    @ExperimentalStdlibApi
    fun parse() {

        ipStore = IPStore()

        fileReader = FileReader(filePath)
        fileReader.showStartMessage()
        fileReader.read(::bufferReadCallback, ::percentsChangedCallback)
        fileReader.showEndMessage()

        println()
        println("Calculate unique ip-addresses ...")
        val count = ipStore?.calculateCount()
        println("Found $count unique ip-addresses")
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
        ipStore?.addIPAddress(address)
    }
}