
package dictionary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP
 */
public class dictionary_EV {
    //khởi tạo mảng lưu trữ danh sách từ và nghĩa
    TreeMap<String,String> Words;
    private Integer num = new Integer(0);

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }


    
    /**
     * khai báo constructor
     * mục đích: cung cấp bộ nhớ cho mảng và đọc danh sách từ vào mảng bằng phương thức insertFormFile()
     */
    public dictionary_EV(){
        Words = new TreeMap();
        insertFromFile();
    }
    /**
     * đọc file E_V.txt vào mảng treemap bằng BufferedReader
     * cách làm: dạng từ trong file E_V.txt: "Word_target<html>...Word_explain...<html>"
     *  dùng hàm subcribe cắt từ "<html>" đầu tiên để chia thành 2 String là từ và nghĩa
     * try_catch: bắt ngoại lệ nếu ko mở đc file
     */
    
    private void insertFromFile(){
    try{
        FileReader fr = new FileReader("E_V.txt");
        BufferedReader file;
        file = new BufferedReader(fr);
        while(file.ready()){
            String Line = file.readLine();
            String Word_target = Line.substring(0,Line.indexOf("<html>"));
            String Word_explain = Line.substring(Line.indexOf("<html>"));
            Words.put(Word_target, Word_explain);
            num++;
        }
        file.close();
    }catch(IOException e){
        System.out.println(e);
    }
    }
    
    /**
     * mục đích: dịch nghĩa
     * @param word_target từ cần tìm kiếm ngĩa
     * cách làm: dùng hàm get() trong treemap(hàm get() dùng duyệt nhị phân)
     * @return 
     */
    public String translateWordExplain(String word_target){
        if(Words.get(word_target)==null) return "don't have this work!";
        else return Words.get(word_target);
    }
    
    /**
     * mục đích: thêm từ và nghĩa mới vào từ điển(chỉ vào mảng, ko thay đổi file E_V.txt)
     * @param Word_insert_key từ mới
     * @param Word_insert_value nghĩa
     * cách làm: dùng hàm put() trong treemap(treemap tự động sắp xếp các key để duyệt nhị phân)
     */
    public void insertWord(String Word_insert_key, String Word_insert_value){
        String word_insert_value = "<html><ul><li><font color='#cc0000'><b>" + Word_insert_value + "</b></font></li></ul></html>";
        Words.put(Word_insert_key, word_insert_value);
    }
    
    /**
     * mục đích: xóa 1 từ và nghĩa của nó(chỉ thoa tác trên mảng, ko làm thay đổi file E_V.txt)
     * @param Word từ cần xóa
     * cách làm: dung hàm remove() trong treemap
     */
    public void removeWord(String Word){
        Words.remove(Word);
    }
    
    /**
     * mục đích: thay từ hoặc thay nghĩa của từ bằng từ mới
     * @param Word: từ cần thay cách viết hoặc thay nghĩa của nó
     * @param new_Word: từ mới
     * @param new_explain: nghĩa mới
     * cách làm: chia 2 trường hợp, thay từ bằng một từ khác hoặc thay nghĩa của từ bằng 1 nghĩa mới
     */
    public void replaceWord(String Word, String new_Word, String new_explain){
        String New_explain = "<html><ul><li><font color='#cc0000'><b>" + new_explain + "</b></font></li></ul></html>";
        if("".equals(new_Word)){
            removeWord(Word);
            insertWord(Word,New_explain);
        }
        else if("".equals(new_explain)){
            String temp = Words.get(Word);
            removeWord(Word);
            insertWord(new_Word,temp);
        }
    }
    
    public void makeVoice(String Word){
        System.setProperty("mbrola.base", "src\\source_voice\\mbrola");
        VoiceManager voice_manager = VoiceManager.getInstance();
        Voice voice = voice_manager.getVoice("mbrola_us1");
        voice.allocate();
        voice.speak(Word);
        voice.deallocate();
    }
    
    public void overideSource(){
        try {
            try (FileWriter fr = new FileWriter("E_V.txt")) {
                BufferedWriter file = new BufferedWriter(fr);
                Set<String> key_set = Words.keySet();
                key_set.forEach((String key) -> {
                    try {
                        String Line = key + Words.get(key)+"\n";
                        file.write(Line);
                    } catch (IOException ex) {
                        Logger.getLogger(dictionary_EV.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                fr.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(dictionary_EV.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
