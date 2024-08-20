package matal.batch;

import matal.entity.Store;
import matal.repository.StoreRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class CsvWriter implements ItemWriter<Store> {

    private final StoreRepository storeRepository;

    public CsvWriter(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public void write(Chunk<? extends Store> chunk) throws Exception {
        storeRepository.saveAll(chunk.getItems());
    }
}
