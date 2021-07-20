package com.quantumdisruption.qwiz.QWiz.controllers;

import com.quantumdisruption.qwiz.QWiz.containers.EmitterContainer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@Slf4j
@CrossOrigin
public class UploadController {

    private final static String UPLOADED_FOLDER = "/uploads/";

    @Autowired
    EmitterContainer emitterContainer;


    @PostMapping("/upload")
    public ResponseEntity<JSONObject> singleFileUpload(@RequestParam("file") MultipartFile file,
                                           RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            return new ResponseEntity(new JSONObject().put("message", "Failed!"), HttpStatus.NOT_ACCEPTABLE);
        }

        try {

            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            createFileIfNotExists(path);
            Files.write(path, bytes);
            new Thread(() -> {
                try {
                    generateImagesFromPDF(path.toFile(), "jpg");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(new JSONObject().put("message", "Uploaded!"), HttpStatus.CREATED);
    }


    private static void generateImagesFromPDF(File file, String extension) throws IOException {
        PDDocument document = PDDocument.load(file);
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        String imgPath;
        FileUtils.cleanDirectory(Paths.get(UPLOADED_FOLDER, "output").toFile());
        log.info("Generating images");
        for (int page = 0; page < document.getNumberOfPages(); ++page) {
            generateImage(extension, pdfRenderer, page);
        }
        document.close();
    }

    private static void generateImage(String extension, PDFRenderer pdfRenderer, int page) throws IOException {
        String imgPath;
        BufferedImage bim = pdfRenderer.renderImageWithDPI(
                page,
                300,
                ImageType.RGB);
        imgPath = String.format("%s/output/pdf-%03d.%s", UPLOADED_FOLDER, page + 1, extension);
        createFileIfNotExists(Paths.get(imgPath));
        ImageIOUtil.writeImage(
                bim,
                imgPath,
                300);
    }

    private static void createFileIfNotExists(Path path) {
        try {
            log.debug("Creating file");
            Files.createFile(path);
        } catch (FileAlreadyExistsException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
