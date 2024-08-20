package matal.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matal.entity.Store;
import matal.batch.CsvReader;
import matal.batch.CsvWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FileItemReaderJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final CsvReader csvReader;
    private final CsvWriter csvWriter;

    private static final int chunkSize = 1000;

    @Bean
    public Job csvFileItemReaderJob() {
        return new JobBuilder("csvFileItemReaderJob", jobRepository)
                .start(csvFileItemReaderStep())
                .build();
    }

    @Bean
    public Step csvFileItemReaderStep() {
        return new StepBuilder("csvFileItemReaderStep", jobRepository)
                .<Store, Store>chunk(chunkSize, transactionManager)
                .reader(csvReader.csvFileItemReader())
                .writer(csvWriter)
                .build();
    }
}
