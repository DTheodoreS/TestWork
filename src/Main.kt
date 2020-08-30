@ExperimentalStdlibApi
@ExperimentalUnsignedTypes
fun main() {
    try {
        val path = "src/resources/hosts.txt"
        val counter = CounterIPAddresses(path)
        counter.parse()
    } catch (e: OutOfMemoryError) {
        println("Not enough memory: ${e.localizedMessage}")
        println("Please increase the maximum size of allocated memory. Recommended: -Xmx800M")
        e.printStackTrace()
    }
}
