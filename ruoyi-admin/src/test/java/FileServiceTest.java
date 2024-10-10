import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.IdUtil;
import com.example.filespringbootstarter.config.FileClientAutoConfiguration;
import com.example.filespringbootstarter.core.service.FileService;
import com.example.filespringbootstarter.enums.FileStorageEnum;
import com.ruoyi.web.core.config.EnvConfig;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = FileServiceTest.Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileServiceTest {
    @Autowired
    private FileService fileService;

    byte[] content = ResourceUtil.readBytes("file/erweima.jpg");

    String path = IdUtil.fastSimpleUUID() + ".jpg";

    @Test
    @Disabled
    @Order(1)
    void testCreateFile() throws Exception {
        String returnPath = fileService.createFile(FileStorageEnum.LOCAL, "test", "/home/" + path, content);
        System.out.println(returnPath);
    }

    @Test
    @Order(1)
    void testCreateWebFile() throws Exception {
        // 创建MockMultipartFile
        MultipartFile multipartFile = new MockMultipartFile(
                "file", // 表单字段名
                "test.txt", // 文件名
                "text/plain", // 文件类型
                "Hello, World!".getBytes() // 文件内容
        );
        String returnPath = fileService.createWebFile(FileStorageEnum.LOCAL, multipartFile);
        System.out.println(returnPath);
    }

    @Test
    @Order(2)
    @Disabled
    void testGetFile() throws Exception {
        byte[] result = fileService.getFileContent(FileStorageEnum.LOCAL, "/home/" + path);
        // 断言
        assertEquals(result.length, content.length);
    }

    @Test
    @Order(3)
    @Disabled
    void testDeleteFile() throws Exception {
        fileService.deleteFile(FileStorageEnum.LOCAL, "/home/" + path);
    }

    @Import({FileClientAutoConfiguration.class, EnvConfig.class})
    public static class Application {
    }
}
