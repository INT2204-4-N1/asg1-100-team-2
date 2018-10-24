/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


/**
 *
 * @author Hoang Dao
 */
abstract class Base_dictionary {
    
    protected TreeMap<String,String> Words;               //khởi tạo mảng lưu trữ danh sách từ và nghĩa
    protected ArrayList<String> keys = new ArrayList();   //khởi tạo danh sách key
    protected String path;                                //tên nguồn file
    
    /**
     * đọc file E_V.txt vào mảng treemap bằng BufferedReader
     * cách làm: dạng từ trong file E_V.txt: "Word_target<html>...Word_explain...<html>"
     *  dùng hàm subcribe cắt từ "<html>" đầu tiên để chia thành 2 String là từ và nghĩa
     * try_catch: bắt ngoại lệ nếu ko mở đc file
     */
    protected void insertFromFile(String path){
    try{
        FileInputStream fileInput = new FileInputStream(path);
        ZipInputStream zipstream = new ZipInputStream(fileInput);   
        ZipEntry entry = zipstream.getNextEntry();
        try (BufferedReader file = new BufferedReader(new InputStreamReader(zipstream,"utf-8"))) {
            while(file.ready()){
                String Line = file.readLine();
                String Word_target = Line.substring(0,Line.indexOf("<html>"));
                String Word_explain = Line.substring(Line.indexOf("<html>"));
                keys.add(Word_target);
                Words.put(Word_target, Word_explain);
            }
        }
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
        if(Words.get(word_target)==null) return "Don't have this work. \n Please search onlline! ";
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
    
    public abstract void makeVoicebyAPI(String Word);
    
    public abstract String TranslatebyAPI(String Word);
    
    public abstract void makeVoice(String Word);
    
    public void overideSource(){
        FileOutputStream fileOutput = null;
        try {
            fileOutput = new FileOutputStream(path+".zip");
            ZipOutputStream zipStream2 = new ZipOutputStream(fileOutput);
            try {
                BufferedWriter file = new BufferedWriter(new OutputStreamWriter(zipStream2,"utf-8"));
                try {
                    zipStream2.putNextEntry(new ZipEntry(path+".txt"));
                    Words.forEach((word,explain) -> {
                        try {
                            String line = word + explain +"\n";
                            file.write(line);
                        } catch (IOException ex) {
                            Logger.getLogger(dictionary_EV.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                    file.close();
                } catch (IOException ex) {
                    Logger.getLogger(dictionary_EV.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(dictionary_EV.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(dictionary_EV.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileOutput.close();
            } catch (IOException ex) {
                Logger.getLogger(dictionary_EV.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
     * tìm kiếm gợi ý
     * @param s từ gợi ý
     * @return 
     */
    public ArrayList searchKey(String s){
        ArrayList list = new ArrayList();
        int len = s.length();
        if(len != 0){
        for(int i = 0; i < keys.size(); i++)
            if(keys.get(i).toString().length() >= len)
                if(keys.get(i).toString().substring(0,len).equalsIgnoreCase(s))
                    list.add(keys.get(i));
        }
        return list;
    }   
    
}
