//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
///**
// * Created by l_ckha on 23-09-2016.
// */
//public class FixUTF8 {
//    private File file;
//    private int count;
//
//    public FixUTF8(String fileName) {
//        file = new File(fileName);
//        count = 0;
//    }
//
//    public void fixFile() {
//        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                // process the line.
//                if (line.toLowerCase().contains("æ") || line.toLowerCase().contains("ø") || line.toLowerCase().contains("å")) {
//                    count++;
//                }
//            }
//            System.out.println(count + " æøå found");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
