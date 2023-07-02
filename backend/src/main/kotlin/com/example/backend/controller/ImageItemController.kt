package com.example.backend.controller

import com.example.backend.model.ImageItem
import com.example.backend.service.ImageItemService
import com.example.backend.utils.Response
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@RestController
@RequestMapping("/image")
class ImageItemController(
    val sdf: SimpleDateFormat = SimpleDateFormat("yyyyMMdd")
) {
    @Autowired
    lateinit var imageItemService: ImageItemService
    @Value("\${file.storage.url}")
    lateinit var imagefolderPath: String


    @PostMapping("/upload")
    fun uploadImage(@RequestParam("file") image: MultipartFile): Response<ImageItem> {
        val filename = image.originalFilename ?: "untitled"
        val filepath = "${imagefolderPath}/${UUID.randomUUID()}_${sdf.format(Date())}_${filename.replace(" ", "")}"
        val imageitem = imageItemService.insertOne(filename, filepath)

        image.transferTo(File(filepath))
        return Response.Ok("upload ok", imageitem)
    }

    @GetMapping("/download/{id}")
    fun findOne(@PathVariable id: Long, response: HttpServletResponse) {
        val imageitem = imageItemService.findOne(id)
        if (imageitem != null) {
            val file = File(imageitem.path)
            if (file.exists()) {
                response.reset()
                val buffer = ByteArray(1024)
                val fis = FileInputStream(file)
                val bis = BufferedInputStream(fis)
                val os = response.outputStream
                response.setContentLengthLong(file.length())

                var result = bis.read(buffer)
                while (result != -1) {
                    os.write(buffer, 0, result)
                    result = bis.read(buffer)
                }

                os.flush()
                fis.close()
                bis.close()
                os.close()
            }
        }
    }
}