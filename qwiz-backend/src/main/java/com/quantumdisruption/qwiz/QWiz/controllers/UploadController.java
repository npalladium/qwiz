package com.quantumdisruption.qwiz.QWiz.controllers;

import com.quantumdisruption.qwiz.QWiz.containers.EmitterContainer;
import com.quantumdisruption.qwiz.QWiz.repositories.QuizRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@Slf4j
@CrossOrigin
public class UploadController {


    @Value("${app.uploads.location}")
    private String uploadFolder;
    @Value("${app.outputs.location}")
    private String outputFolder;

    @Autowired
    EmitterContainer emitterContainer;

    @Autowired
    QuizRepository quizRepository;


    @PostMapping(value = {"/upload", "/upload/{name}"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<JSONObject> singleFileUpload(@RequestParam("file") MultipartFile file,
                                                       RedirectAttributes redirectAttributes,
                                                       @PathVariable Optional<String> name) {

        if (file.isEmpty()) {
            return new ResponseEntity(new JSONObject().put("message", "Failed!"), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!name.isPresent()) {
            handleUploadedFile(file, outputFolder);
        } else {
            String slidesOutputFolder = Paths.get(outputFolder, name.get()).toString();
            mkdirIfNotExists(slidesOutputFolder);
            handleUploadedFile(file, slidesOutputFolder);
        }
        return new ResponseEntity(new JSONObject().put("message", "Uploaded!"), HttpStatus.CREATED);
    }


    private void mkdirIfNotExists(String slidesOutputFolder) {
        File slidesOutputDir = new File(slidesOutputFolder);
        if (!slidesOutputDir.exists()) {
            slidesOutputDir.mkdir();
        }
    }


    private void handleUploadedFile(MultipartFile file, String slidesOutPutFolder) {
        try {

            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadFolder + file.getOriginalFilename());
            createFileIfNotExists(path);
            Files.write(path, bytes);
            new Thread(() -> {
                try {
                    generateImagesFromPDF(path.toFile(),
                            "jpg",
                            slidesOutPutFolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void generateImagesFromPDF(File file, String extension, String outputFolder) throws IOException {
        PDDocument document = PDDocument.load(file);
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        String imgPath;
        FileUtils.cleanDirectory(Paths.get(outputFolder).toFile());
        log.info("Generating images");
        for (int page = 0; page < document.getNumberOfPages(); ++page) {
            generateImage(extension, pdfRenderer, page, outputFolder);
        }
        document.close();
    }

    private static void generateImage(String extension, PDFRenderer pdfRenderer, int page, String outputFolder) throws IOException {
        String imgPath;
        BufferedImage bim = pdfRenderer.renderImageWithDPI(
                page,
                300,
                ImageType.RGB);
        imgPath = String.format("%s/pdf-%03d.%s", outputFolder, page + 1, extension);
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