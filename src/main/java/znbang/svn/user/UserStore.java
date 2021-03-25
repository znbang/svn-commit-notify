package znbang.svn.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用檔案user.json儲存使用者資料。
 */
@Component
public class UserStore {
    private final File FILE = new File("user.json");
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 讀檔。
     * @return 使用者資料
     * @throws IOException 讀檔發生錯誤
     */
    private Map<String, User> load() throws IOException {
        Map<String, User> table = new HashMap<>();
        if (FILE.exists()) {
            for (User a : objectMapper.readValue(FILE, User[].class)) {
                table.put(a.getEmail(), a);
            }
        }
        return table;
    }

    /**
     * 存檔。
     * @param table 使用者資料
     * @throws IOException 存檔發生錯誤
     */
    private void save(Map<String, User> table) throws IOException {
        objectMapper.writeValue(FILE, table.values());
    }

    /**
     * 取得所有使用者
     * @return 使用者清單
     * @throws IOException 讀檔發生錯誤
     */
    public List<User> list() throws IOException {
        List<User> list = new ArrayList<>(load().values());
        list.sort((a, b) -> a.getEmail().compareToIgnoreCase(b.getEmail()));
        return list;
    }

    /**
     * 儲存使用者。
     * @param user 要儲存的使用者
     * @throws IOException 讀寫檔案發生錯誤
     */
    public void save(User user) throws IOException {
        Map<String, User> table = load();
        table.put(user.getEmail(), user);
        save(table);
    }

    /**
     * 刪除使用者。
     * @param user 要刪除的使用者
     * @throws IOException 讀寫檔案發生錯誤
     */
    public void delete(User user) throws IOException {
        Map<String, User> table = load();
        if (table.remove(user.getEmail()) != null) {
            save(table);
        }
    }
}
