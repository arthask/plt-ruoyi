import com.alibaba.excel.EasyExcel;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ReadTest {

    private final String WRITE_FILE_PREFIX = "src/export/";

    private final String READ_FILE_PREFIX = "src/word/";

    private final String name = "word-phrase";

    private final String readFileName = READ_FILE_PREFIX + name;

    private final int size = 4;

    @Test
    public void readTxtFile() throws IOException {

        List<WordData> wordDataList = getWordData(readFileName, size);
        wordDataList.forEach(System.out::println);
    }

    @Test
    public void simpleWrite() {
        // 注意 simpleWrite在数据量不大的情况下可以使用（5000以内，具体也要看实际情况），数据量大参照 重复多次写入
        // 写法1 JDK8+
        // since: 3.0.0-beta1
        String fileName = WRITE_FILE_PREFIX + name + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName, WordData.class)
                .sheet("程序员常用单词")
                .doWrite(() -> {
                    // 分页查询数据
                    try {
                        return getWordData(readFileName, size);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private Integer getMapIndex(int row, Set<Integer> keySet, int size) {
        for (Integer key : keySet) {
            if (row >= key && row < key + size) {
                return key;
            }
        }
        return null;
    }

    private List<WordData> getWordData(String fileName, int size) throws IOException {
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        int i = 1;
        String line;
        Map<Integer, WordData> dataMap = new HashMap<>();
        List<WordData> wordDataList = new ArrayList<>();
        long fileLength = file.length();
        LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(file));
        lineNumberReader.skip(fileLength);
        long rowNum = lineNumberReader.getLineNumber();
        for (int row = 1; row <= rowNum; ) {
            WordData wordData = new WordData();
            wordData.setTag("程序员英语词汇");
            wordDataList.add(wordData);
            dataMap.put(row, wordData);
            row += size;
        }
        System.out.print(dataMap.size());

        while ((line = br.readLine()) != null) {
            WordData wordData = dataMap.get(getMapIndex(i, dataMap.keySet(), size));
            if (i % size == 1) {
                wordData.setWord(line);
            }
            if (i % size == 2) {
                wordData.setTranslation(line);
            }
            if (i % size == 3) {
                wordData.setPos(line);
            }
            if (size == 4 && i % size == 0) {
                wordData.setPhonetic(line);
            } else {
                if (i % size == 4) {
                    wordData.setPhonetic(line);
                }
            }

            i++;
        }
        br.close();
        return wordDataList;
    }
}
