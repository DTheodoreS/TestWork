@ExperimentalUnsignedTypes
fun main() {
    var path = "src/resources/hosts.txt"
    path = "F:\\ip_addresses"
    //path = "F:\\ips.txt"
    path = "src/resources/BufferReaderTest.txt"
    val ipAddressReader = IPAddressReader()
    ipAddressReader.read(path)
}
