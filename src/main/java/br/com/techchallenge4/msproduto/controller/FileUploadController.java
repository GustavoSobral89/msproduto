package br.com.techchallenge4.msproduto.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.batch.core.repository.JobRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/carga")
public class FileUploadController {

    private final JobLauncher jobLauncher; // JobLauncher para disparar o job
    private final Job job; // O job que processa o arquivo

    @Autowired
    private JobRepository jobRepository;

    private final String TEMP_STORAGE = "C:/Users/SYS/Documents/fiap/temp/";

    public FileUploadController(JobLauncher jobLauncher,  Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @Operation(summary = "Faz o upload de um arquivo CSV", description = "Este endpoint recebe um arquivo CSV enviado e o processa para salvar no banco de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arquivo processado com sucesso e dados salvos no banco de dados"),
            @ApiResponse(responseCode = "400", description = "Erro no envio do arquivo ou formato inválido"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao processar o arquivo")
    })
    @PostMapping(path = "/upload")
    public void uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        String originalFileName = multipartFile.getOriginalFilename();
        File fileToImport = new File(TEMP_STORAGE + originalFileName);
        multipartFile.transferTo(fileToImport);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("fullPathFileName", TEMP_STORAGE + originalFileName)
                .addLong("startAt", System.currentTimeMillis()).toJobParameters();

        JobExecution execution = jobLauncher.run(job, jobParameters);
    }
}
