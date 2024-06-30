import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.quoteofthebay.quote
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.io.OutputStreamWriter

class MainViewModel(val context: Context) : ViewModel() {
    private var quoteList: MutableList<quote> = mutableListOf()
    private var index = 0

    init {
        loadQuotesFromAssets()
        index = quoteList.indices.random()
    }

    private fun loadQuotesFromAssets() {
        try {
            context.assets.open("quotes.json").use { inputStream ->
                val json = inputStream.bufferedReader().use {
                    it.readText()
                }
                val listType = object : TypeToken<List<quote>>() {}.type
                quoteList = Gson().fromJson(json, listType)
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }

    fun saveQuotesToFile() {
        try {
            context.openFileOutput("quotes.json", Context.MODE_PRIVATE).use { outputStream ->
                val gson = Gson()
                val json = gson.toJson(quoteList)
                OutputStreamWriter(outputStream).use { writer ->
                    writer.write(json)
                }
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }

    fun toggleLikedStatus(id: Int) {
        val quote = quoteList.find { it.id == id }
        quote?.isLiked = if (quote?.isLiked == 1) 0 else 1
        val index = quoteList.indexOf(quote)
        if (index!= -1) {
            quoteList[index] = quote!!
        }
        saveQuotesToFile()
    }

    fun getQuote() = quoteList[index]

    fun nextQuote(): quote {
        index = (index + 1) % quoteList.size
        return quoteList[index]
    }

    fun prevQuote(): quote {
        index = (index - 1 + quoteList.size) % quoteList.size
        return quoteList[index]
    }
}
