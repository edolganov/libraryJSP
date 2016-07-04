import com.examples.libraryJSP.model.DataManager;
import org.junit.Test;

import java.sql.SQLException;

/**
 * Created by Аяз on 21.06.2016.
 */
public class TestDB {

    public static void startProcessCheck() {

    }

    public static void createDB() {
        DataManager dataManager = new DataManager();
        try {
            dataManager.createDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testExample1Process() throws Exception {
        // Тестовый пример 1
        // startProcessCheck(new LogHandler(), "/testExample1.log", "/testExample1_answer.log");
        createDB();
    }
}
