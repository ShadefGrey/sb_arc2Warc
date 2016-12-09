//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by l_ckha on 22-09-2016.
// */
//public class SplitWarcFile {
//
//    private int fileSplits;
//    private File pathOfWarc;
//    private String pathOfSplits;
//    //    private int content;
//    private String warcInfo;
//    private int warcFileLenght;
//    private File outputFile;
//    private int maxBytes;
//
//    public SplitWarcFile(String pathOfWarc, String pathOfSplits, int maxBytes) {
//        this.maxBytes = maxBytes;
//        this.pathOfWarc = new File(pathOfWarc);
//        this.pathOfSplits = pathOfSplits;
//        fileSplits = 1;
//        warcFileLenght = 0;
//        warcInfo = "WARC/1.0\n" +
//                "WARC-Type: warcinfo\n" +
//                "WARC-Record-ID: <urn:uuid:dd9618a3-8716-4f55-ae23-66736709c0aa>\n" +
//                "WARC-Date: 2016-08-17T08:16:15Z\n" +
//                "Content-Length: 0\n" +
//                "Content-Type: application/warc-fields\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "WARC/";
//    }
//
//
//    //splits a warc file into new files that are each ca 10 mb in size
//    public void splitIt() {
//        String s = pathOfSplits.substring(0, pathOfSplits.length() - 5) + fileSplits + pathOfSplits.substring(pathOfSplits.length() - 5);
//        outputFile = new File(s);
//        List<Byte> outputbytes = new ArrayList<>();
//
//
//        {
//            try (FileInputStream fInputStream = new FileInputStream(pathOfWarc)) {
//                if (fileSplits == 1) {
//                    warcFileLenght = fInputStream.available();
//                }
//
//                //make the file the outputstream should write to
//                FileOutputStream fOutputStream = new FileOutputStream(outputFile);
//                if (!outputFile.exists()) {
//                    outputFile.createNewFile();
//                }
//
//                int counter = 0;
//                boolean go = false;
//                boolean w = false;
//                boolean a = false;
//                boolean r = false;
//                boolean c = false;
//                boolean found = false;
//                int content = 0;
//
//
//                //Should be possible to make it more readable, but not right now
//                while ((content = fInputStream.read()) != -1 && !found) {
//
//                    //when the counter reaches 10.000.000 (which is slightly below a file size of 10 mb) it sets the boolean go to true.
//                    if (counter > maxBytes) {
//                        go = true;
//                    }
//
//                    //When the go is set to true, it starts looking for the beginning of the next warc record so it can stop at an appropriate time
//                    if ((byte) content == 'W' && go) {
//                        w = true;
//                    } else if ((byte) content == 'A' && w && go) {
//                        a = true;
//                    } else if ((byte) content == 'R' && w && a && go) {
//                        r = true;
//                    } else if ((byte) content == 'C' && w && a && r && go) {
//                        c = true;
//                    } else if ((byte) content == '/' && w && a && r && c && go) {
//                        found = true;
//                    }else {
//                        w = false;
//                        a = false;
//                        r = false;
//                        c = false;
//                    }
//                    counter++;
//                    outputbytes.add((byte) content);
//
//                    //When the beginning of the next warc record is found, write the output to a file
//                    if (found) {
//                        for (int i = 0; i < 5; i++) {
//                            outputbytes.remove(outputbytes.size() - 1);
//                        }
//
//                        byte[] output = new byte[outputbytes.size()];
//
//                        //add the content from the outputbytes arraylist to a byte array the outputstream can use
//                        for (int i = 0; i < outputbytes.size(); i++) {
//                            output[i] = outputbytes.get(i);
//                        }
//
//                        fOutputStream.write(output);
//                        fOutputStream.close();
//                        outputbytes.clear();
//                        System.out.println("File made");
//                    }
//
//                    //Writes the last file that will most likely be below 10.000.000 bytes
//                    if (warcFileLenght == counter){
//                        byte[] output = new byte[outputbytes.size()];
//
//                        //add the content from the outputbytes arraylist to a byte array the outputstream can use
//                        for (int i = 0; i < outputbytes.size(); i++) {
//                            output[i] = outputbytes.get(i);
//                        }
//
//                        fOutputStream.write(output);
//                        fOutputStream.close();
//                        System.out.println("Last file made");
//                    }
//
//                    //resets the booleans and make a new outputfile to write to, add a warc info to the start of the outputbyte array list, since it will be missing in all but the first of the split files
//                    //the warc info should match the one of the original warc file (also add WARC/ since it was deleted in the process of finding the next warc record)
//                    if (found) {
//                        fileSplits++;
//
//                        for (char ch : warcInfo.toCharArray()) {
//                            outputbytes.add((byte) ch);
//                        }
//
//                        //substract counter from warcFileLength so the last warc file will be added at the appropriate time
//                        warcFileLenght -= counter;
//                        counter = 0;
//                        go = false;
//                        w = false;
//                        a = false;
//                        r = false;
//                        c = false;
//                        found = false;
//                        outputFile = new File(pathOfSplits.substring(0, pathOfSplits.length() - 5) + fileSplits + pathOfSplits.substring(pathOfSplits.length() - 5));
//                        fOutputStream = new FileOutputStream(outputFile);
//                        if (!outputFile.exists()) {
//                            outputFile.createNewFile();
//                        }
//
//                    }
//                }
//
//                System.out.println("done");
//                fInputStream.close();
//                fOutputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//
//    }
//}
