package site.ogobi.ogobi.boundedContext.file.repository;

import org.springframework.stereotype.Repository;
import site.ogobi.ogobi.boundedContext.file.entity.File;

import java.util.HashMap;
import java.util.Map;

@Repository
public class FileRepository {

    private final Map<Long, File> store = new HashMap<>();
    private long sequence = 0L;

    public File save(File item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public File findById(Long id) {
        return store.get(id);
    }


}
