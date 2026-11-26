package com.pogodizborovite

import org.schabi.newpipe.extractor.NewPipe
import org.schabi.newpipe.extractor.ServiceList
import org.schabi.newpipe.extractor.downloader.Downloader
import org.schabi.newpipe.extractor.downloader.Request
import org.schabi.newpipe.extractor.downloader.Response
import org.schabi.newpipe.extractor.exceptions.ReCaptchaException
import org.schabi.newpipe.extractor.stream.StreamInfo
import org.schabi.newpipe.extractor.stream.StreamInfoItem
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

data class ExtractedVideo(
    val title: String,
    val url: String,
    val thumbnail: String,
    val uploader: String
)

private class AppNewPipeDownloader : Downloader() {
    override fun execute(request: Request): Response {
        val connection = (URL(request.url()).openConnection() as HttpURLConnection).apply {
            requestMethod = request.httpMethod()
            connectTimeout = 20000
            readTimeout = 25000
            instanceFollowRedirects = true
            setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 11) AppleWebKit/537.36 Chrome/131.0.0.0 Mobile Safari/537.36")
            request.headers().forEach { (name, values) ->
                if (values.isNotEmpty()) setRequestProperty(name, values.joinToString(","))
            }
            request.dataToSend()?.let { body ->
                doOutput = true
                outputStream.use { it.write(body) }
            }
        }
        val code = connection.responseCode
        val stream = if (code in 200..399) connection.inputStream else connection.errorStream
        val body = stream?.bufferedReader(StandardCharsets.UTF_8)?.use { it.readText() } ?: ""
        val headers = connection.headerFields.filterKeys { it != null }
            .mapKeys { it.key!! }
            .mapValues { it.value ?: emptyList() }
        return Response(code, connection.responseMessage ?: "", headers, body, connection.url.toString())
    }
}

object NewPipeYoutube {
    @Volatile private var initialized = false

    @Synchronized
    private fun initialize() {
        if (!initialized) {
            NewPipe.init(AppNewPipeDownloader())
            initialized = true
        }
    }

    fun search(query: String): List<ExtractedVideo> {
        initialize()
        val service = ServiceList.YouTube
        val handler = service.searchQHFactory.fromQuery(query)
        val extractor = service.getSearchExtractor(handler)
        extractor.fetchPage()
        return extractor.initialPage.items
            .filterIsInstance<StreamInfoItem>()
            .take(30)
            .map { item ->
                ExtractedVideo(
                    title = item.name ?: "Vidéo sans titre",
                    url = item.url,
                    thumbnail = item.thumbnails.firstOrNull()?.url ?: "",
                    uploader = item.uploaderName ?: ""
                )
            }
    }

    fun getPlayableStream(videoUrl: String): String {
        initialize()
        val info = StreamInfo.getInfo(videoUrl)
        return info.videoStreams.firstOrNull { it.isUrl }?.content
            ?: info.videoStreams.firstOrNull()?.content
            ?: throw IOException("Aucun flux vidéo disponible pour cette vidéo.")
    }
}
