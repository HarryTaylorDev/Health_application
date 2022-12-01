import MVC.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTests {

    @Test
    public void generatedSaltsShouldBeDifferent(){
        assertNotEquals(User.generateSalt(), User.generateSalt());
    }

    @Test
    public void hashedDataShouldBeDifferent(){
        String inputData = "test data";
        assertNotEquals(User.hashDataWithSalt(inputData, User.generateSalt()), User.hashDataWithSalt(inputData, User.generateSalt()));
    }
}
