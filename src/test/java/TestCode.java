import com.examples.libraryJSP.beans.permissions.Roles;
import org.junit.Test;

import java.util.HashMap;
import java.util.Hashtable;

import static com.examples.libraryJSP.beans.encoding.Encoder.md5Apache;

/**
 * Created by Аяз on 27.06.2016.
 */
public class TestCode {

    HashMap<String, String> categories = new HashMap<>();
    Hashtable<String, String> categoriesT = new Hashtable<>();

    public void test() {
        categories.keySet();
        categoriesT.keys();
        categoriesT.keySet();

        for (Roles role:
        Roles.values()) {

        }
    }

    public void md5Generate() {
        System.out.println("User_1: " + md5Apache("User_1"));
        System.out.println("User_2: " + md5Apache("User_2"));
        System.out.println("pass: " + md5Apache("pass"));
    }
    @Test
    public void testExample1Process() throws Exception {
        // Тестовый пример 1 - генерация паролей
        md5Generate();
    }

}
