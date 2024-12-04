package br.com.techchallenge4.msproduto.config;

import br.com.techchallenge4.msproduto.model.Produto;
import br.com.techchallenge4.msproduto.model.ProdutoProcessor;
import lombok.*;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;

import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.batch.core.Job;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.File;

@Configuration
@RequiredArgsConstructor
public class BatchConfiguration {

    @Getter
    @Setter
    private String filePathAux;

    private final String SQL = "INSERT INTO produto (nome, descricao, preco, quantidadeestoque, createdatetime) " +
            "VALUES (:nome, :descricao, :preco, :quantidadeestoque, :createdatetime)";

    @Bean
    @StepScope
    public FlatFileItemReader<Produto> itemReader(@Value("#{jobParameters[fullPathFileName]}") String pathToFIle) {
        FlatFileItemReader<Produto> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new FileSystemResource(new File(pathToFIle)));
        flatFileItemReader.setName("CSV-Reader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }

    private LineMapper<Produto> lineMapper() {
        DefaultLineMapper<Produto> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("nome", "descricao", "preco", "quantidadeestoque");
        //lineTokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob");

        BeanWrapperFieldSetMapper<Produto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Produto.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    @Bean
    public Step step1(JobRepository jobRepository, ItemWriter<Produto> writer, PlatformTransactionManager transactionManager) {
        return new StepBuilder("slaveStep", jobRepository).<Produto, Produto>chunk(10, transactionManager)
                .reader(itemReader(filePathAux))
                .processor(itemProcessor())
                .writer(writer)
                .faultTolerant()
                //.taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Job runJob(JobRepository jobRepository, ItemWriter<Produto> writer, PlatformTransactionManager transactionManager) {
        return new JobBuilder("importarProdutos", jobRepository)
                .flow(step1(jobRepository, writer, transactionManager)).end().build();
    }

    @Bean
    public ItemWriter<Produto> itemWriter(DataSource datasource) {
        return new JdbcBatchItemWriterBuilder<Produto>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .dataSource(datasource)
                .sql(SQL)
                .build();
    }

    @Bean
    public ItemProcessor<Produto, Produto> itemProcessor() {
        return new ProdutoProcessor();
    }


    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }
}
