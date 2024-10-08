import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.IdUtil;
import com.example.filespringbootstarter.config.FileClientAutoConfiguration;
import com.example.filespringbootstarter.core.service.FileService;
import com.example.filespringbootstarter.enums.FileStorageEnum;
import com.ruoyi.web.core.config.EnvConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import({FileClientAutoConfiguration.class, EnvConfig.class})
@SpringBootTest(classes = FileServiceTest.Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class FileServiceTest {
    @Autowired
    private FileService fileService;

    @Test
    public void testCreateFile() throws Exception {
        byte[] content = ResourceUtil.readBytes("file/erweima.jpg");
        String path = IdUtil.fastSimpleUUID() + ".jpg";
        String returnPath = fileService.createFile(FileStorageEnum.LOCAL, "test", "/home/" + path, content);
        System.out.println(returnPath);
    }

    @Test
    public void testDeleteFile() throws Exception {
        byte[] content = ResourceUtil.readBytes("file/erweima.jpg");
        String path = IdUtil.fastSimpleUUID() + ".jpg";
        String returnPath = fileService.createFile(FileStorageEnum.LOCAL, "test", "/home/" + path, content);
        System.out.println(returnPath);
        fileService.deleteFile(FileStorageEnum.LOCAL, "/home/" + path);
    }

    public static class Application {
    }
}
