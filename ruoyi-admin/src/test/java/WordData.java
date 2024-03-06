import com.alibaba.excel.annotation.ExcelProperty;

public class WordData {
    /**
     * 单词
     */
    @ExcelProperty("单词")
    private String word;

    /**
     * 翻译
     */
    @ExcelProperty("翻译")
    private String translation;

    /**
     * 发音
     */
    @ExcelProperty("音标")
    private String phonetic;

    /**
     * 词性
     */
    @ExcelProperty("词性")
    private String pos;

    /**
     * tag
     */
    @ExcelProperty("标签")
    private String tag;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "WordData{" +
                "word='" + word + '\'' +
                ", translation='" + translation + '\'' +
                ", phonetic='" + phonetic + '\'' +
                ", pos='" + pos + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
