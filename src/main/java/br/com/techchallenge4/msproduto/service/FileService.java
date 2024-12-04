package br.com.techchallenge4.msproduto.service;

import br.com.techchallenge4.msproduto.config.BatchConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileService {

    @Value("${carga.input-path}")
    private String uploadDir;


    private final BatchConfiguration batchConfiguration;

    public FileService(BatchConfiguration batchConfiguration) {
        this.batchConfiguration = batchConfiguration;
    }

    // Salva o arquivo CSV no diretório resources como carga.csv
    public String saveFile(MultipartFile file) throws IOException {
        // Verifica se o diretório resources existe, se não, cria
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs(); // Cria a pasta de recursos caso não exista
        }

        // Define o caminho e nome do arquivo para sempre ser "carga.csv"
        String filePath = dir.getAbsolutePath() + "/carga.csv";
        File destFile = new File(filePath);

        // Salva o arquivo no diretório resources com o nome carga.csv
        file.transferTo(destFile);

        return filePath;
    }
}
