import java.io.File

fun main() {

    val path = "src/resources/hosts.txt"
    val file = File(path)

    file.bufferedReader().forEachLine { it ->
        println("{$it}")
    }
}




