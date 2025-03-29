package ru.mtuci.bvt_demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

@RestController
@RequestMapping("/files")
public class MyFileController {
    private final MyFileRepository fileRepository;

    public MyFileController(MyFileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> saveFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Empty file");
        }

        try {
            String fileName = file.getOriginalFilename();
            byte[] bytes = file.getBytes();

            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(fileName);
            fileEntity.setData(bytes);

            fileRepository.save(fileEntity);

            return ResponseEntity.ok().body("Файл успешно загружен: " + fileName);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping
    public void processFile(@RequestParam("file") MultipartFile file) throws IOException {
        //Сохраняем файл из запроса во временный бинарный файл
        Path tempfile = Files.createTempFile("upload-", ".bin");
        try (InputStream is = file.getInputStream()) {
            Files.copy(is, tempfile, StandardCopyOption.REPLACE_EXISTING);
        }
        //Читаем временный файл блоками
        try (RandomAccessFile raf = new RandomAccessFile(tempfile.toFile(), "r")) {
            final int windowSize = 8;
            //Размер блока для чтения в байтах
            final int chunkSize = 8192;
            byte[] chunk = new byte[chunkSize];
            //Текущая позиция в файле
            long globalPos = 0;
            int bytesRead;
            long rollingHash = 0;
            Queue<Byte> windowQueue = new LinkedList<>();

            //Читаем, пока не дойдём до конца файла
            while((bytesRead = raf.read(chunk)) != -1) {
                //Какая-то логика работы с файлом
                for (int i = 0; i < windowSize; i++) {
                    byte b = chunk[i];
                    rollingHash = increase_rollingHash(byte);
                    windowQueue.add(b);

                    if (windowQueue.size() == windowSize) {
                        //Выполняем поиск сигнатуры по словарю
                    }
                    byte oldestByte = windowQueue.remove();
                    long oldestVal = remove_byte_from_rolling_hash(oldestByte);
                    rollingHash = (rollingHash - oldestVal + MOD) % MOD;
                }
                globalPos++;
            }
        }
        //...
        Files.deleteIfExists(tempfile);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable(name = "id") Long id) {
        Optional<FileEntity> optionalFileEntity = fileRepository.findById(id);
        if (optionalFileEntity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        FileEntity fileEntity = optionalFileEntity.get();

        return new ResponseEntity<>(fileEntity.getData(), HttpStatus.OK);
    }
}
