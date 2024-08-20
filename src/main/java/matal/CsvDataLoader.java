package matal;

import matal.Service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class CsvDataLoader implements CommandLineRunner {

    @Autowired
    private StoreService storeService;

    @Override
    public void run(String... args) throws Exception {
        Path csvFilePath = Path.of("C:/Users/82102/Downloads/Naengmyeon.csv");
        storeService.saveStoresFromCsv(csvFilePath);
    }
}