package matal.batch;

import matal.entity.Store;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class CsvReader {

    @Bean
    public FlatFileItemReader<Store> csvFileItemReader() {
        FlatFileItemReader<Store> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource("C:\\Users\\82102\\Downloads\\Naengmyeon.csv")); // CSV 파일 경로 설정

        // 라인 매퍼 설정
        DefaultLineMapper<Store> lineMapper = new DefaultLineMapper<>();

        // 토크나이저 설정
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("latitude", "longitude", "negative_ratio", "positive_ratio", "rating", "review_count",
                "address", "category", "keyword", "name", "naver_map_url", "near_station",
                "opening_hours", "phone_number", "positive_keywords");

        lineMapper.setLineTokenizer(tokenizer);

        // FieldSetMapper 설정
        BeanWrapperFieldSetMapper<Store> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Store.class);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        reader.setLineMapper(lineMapper);
        return reader;
    }
}
