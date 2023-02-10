import kotlinx.browser.document
import org.w3c.dom.url.URL

fun main() {
    val url = URL(document.URL)
    val calc = url.searchParams.get("i")
    console.log(calc)
    if (!calc.isNullOrEmpty()) {
        document.getElementById("calc-input").asDynamic().value = calc
        processInput(calc)
    }
}