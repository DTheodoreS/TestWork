import java.io.File
import java.io.FileInputStream
import java.util.*

@Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS")
class FileReader(filePath: String) {

    private var buffer = ByteArray(10) // BUFFER_SIZE = 8 * 1024
    private var bufferSize: Int = 0

    private var startTime: Date = Date()
    private var endTime: Date = Date()

    private var file: File = File("")
    private var fileSize = 0L

    private var bytesRead: ULong = 0u
    private var percentsRead = 0.0

    private var percentsChanged: () -> Unit = { }

    init {
        val file = File(filePath)
        this.file = file
        this.fileSize = file.length()
    }

    fun read(bufferReadCallback: (buffer: ByteArray, bufferSize: Int) -> Unit,
             percentsChangedCallback: () -> Unit) {
        percentsChanged = percentsChangedCallback
        startTime = Date()

        var stream: FileInputStream? = null
        try {
            stream = FileInputStream(file)

            bufferSize = stream.read(buffer)
            while (bufferSize != -1 /*&& percentsRead < 1*/) {
                bufferReadCallback(buffer, bufferSize)
                calculateProgress()
                bufferSize = stream.read(buffer)
            }

            bufferReadCallback(ByteArray(1) { 10 }, 1)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            stream?.close()
        }

        endTime = Date()
    }

    fun showStartMessage() {
        println("File '${file.absolutePath}'")
        println("File size = $fileSize b")
        println("File reading started [${formatTime(startTime)}] ...")
    }

    fun showProgress() {
        println("${"%.2f".format(percentsRead)}%")
    }

    fun showEndMessage() {
        val totalMS = endTime.time - startTime.time
        val s = totalMS / 1000
        val diff = "${s / 60}h ${s / 60}m ${s % 60}s"
        println("... file reading completed [${formatTime(endTime)}] elapsed=$diff")
    }

    private fun calculateProgress() {
        bytesRead += bufferSize.toUInt()

        val newPercents = bytesRead.toDouble() / fileSize * 100
        if (newPercents - percentsRead > 0.01) {
            percentsRead = newPercents
            percentsChanged()
        }
    }
}