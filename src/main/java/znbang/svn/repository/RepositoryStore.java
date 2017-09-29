package znbang.svn.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用檔案repository.json儲存repository資料。
 */
@Component
public class RepositoryStore {
    private final File FILE = new File("repository.json");
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 讀檔。
     * @return repository資料
     * @throws IOException 讀檔發生錯誤
     */
    private Map<String, Repository> load() throws IOException {
        Map<String, Repository> table = new HashMap<>();
        if (FILE.exists()) {
            for (Repository a : objectMapper.readValue(FILE, Repository[].class)) {
                table.put(a.getUrl(), a);
            }
        }
        return table;
    }

    /**
     * 存檔。
     * @param table repository資料
     * @throws IOException 存檔發生錯誤
     */
    private void save(Map<String, Repository> table) throws IOException {
        objectMapper.writeValue(FILE, table.values());
    }

    /**
     * 取得所有repository。
     * @return repository清單
     * @throws IOException 讀檔發生錯誤
     */
    public List<Repository> list() throws IOException {
        List<Repository> list = new ArrayList<>(load().values());
        list.sort((a, b) -> a.getUrl().compareToIgnoreCase(b.getUrl()));
        return list;
    }

    /**
     * 儲存repository。
     * @param repository  要儲存的repository
     * @throws IOException 讀寫檔案發生錯誤
     */
    public void save(Repository repository) throws IOException {
        Map<String, Repository> table = load();
        table.put(repository.getUrl(), repository);
        save(table);
    }

    /**
     * 刪除repository。
     * @param repository 要刪除的repository
     * @throws IOException 讀寫檔案發生錯誤
     */
    public void delete(Repository repository) throws IOException {
        Map<String, Repository> table = load();
        if (table.remove(repository.getUrl()) != null) {
            save(table);
        }
    }
}
