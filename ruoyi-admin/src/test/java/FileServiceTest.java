import com.example.filespringbootstarter.core.service.FileService;
import com.example.filespringbootstarter.enums.FileStorageEnum;
import com.ruoyi.RuoYiApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = RuoYiApplication.class)
public class FileServiceTest {
    @Autowired
    private FileService fileService;

    @Test
    public void testFileService() throws Exception {
        fileService.createFile(FileStorageEnum.LOCAL, "test", "/home", null);
    }
}
