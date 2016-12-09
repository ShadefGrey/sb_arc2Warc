//import org.jwat.tools.JWATTools;
//import org.jwat.warc.WarcFileWriter;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.lang.reflect.Array;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by l_ckha on 07-09-2016.
// */
//public class WarcFile {
//    private WarcRecord warcInfoRecord;
//    private ArrayList<WarcRecord> warcRecords;
//    private String warcFileString;
//    private ArrayList<Byte> warcFileBytes;
//    private String pageName;
//    private String dirPath;
//    private int contentLength; //TODO it would make more sense to have it in the warc record
//
//    public WarcFile(String dirPath) {
//        this.dirPath = dirPath;
//        pageName = "soeg.sol.dk"; //TODO should automatically set the right page name
//        warcFileBytes = new ArrayList<>();
//        warcInfoRecord = createWarcRecord(WarcRecordType.WARCINFO);
//        warcFileString = warcInfoRecord.warcInfoRecord();
//        for (int i = 0; i < warcFileString.length(); i++) {
//            warcFileBytes.add((byte) warcFileString.charAt(i));
//        }
//    }
//
//    public ArrayList<Byte> getWarcFileBytes() {
//        return new ArrayList<>(warcFileBytes);
//    }
//
//    public WarcRecord createWarcRecord(WarcRecordType wrt) {
//        return new WarcRecord(wrt);
//    }
//
//    public void writeWarcFile(String pathToSave) {
//        try {
//            File outputFile = new File(pathToSave);
//            responseRecordToFile(dirPath);
//            FileOutputStream fOutputStream = new FileOutputStream(outputFile);
//            if (!outputFile.exists()) {
//                outputFile.createNewFile();
//            }
//            byte[] output;
//
//            output = new byte[getWarcFileBytes().size()];
//            for (int i = 0; i < getWarcFileBytes().size(); i++) {
//                output[i] = getWarcFileBytes().get(i);
//            }
//            fOutputStream.write(output);
//            fOutputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public void responseRecordToFile(String gifToFix) {
//        File file = new File(gifToFix);
//        List<Byte> outputbytes = new ArrayList<>();
//
//        try (FileInputStream fInputStream = new FileInputStream(file)) {
//            contentLength = fInputStream.available();
//            System.out.println(contentLength);
//
//
//            //add the content from the inputstream to the outputbytes arraylist
//            int content;
//            while ((content = fInputStream.read()) != -1) {
//                outputbytes.add((byte) content);
//            }
//
//            //Finds out how many bytes to delete in the file for it to be a gif
//            //also adds 1 to the content lenght for each \n since I will insert an \r when that occurs
//            int linefeeds = 0;
//            int toDelete = 0;
//            for (byte b : outputbytes) {
//                if (linefeeds != 2) {
//                    if (b == '\n') {
//                        linefeeds++;
//                        toDelete++;
//                        contentLength++;
//                    } else {
//                        toDelete++;
//                        linefeeds = 0;
//                    }
//                }
//            }
//
//            String inputToAdjust = "";
//            for (int i = 0; i < toDelete; i++) {
//                inputToAdjust += (char) (byte) outputbytes.get(i);
//            }
//
//            String warcString =
//                    new WarcRecord(WarcRecordType.RESPONSE).warcResponseRecord(warcInfoRecord.getWarcinfoId(), gifToFix, contentLength, pageName);
//
//            //Change Status to http
//            inputToAdjust = inputToAdjust.replaceAll("Status: ", "HTTP/1.1 ");
//            warcString += inputToAdjust;
//            for (int i = 0; i < toDelete; i++) {
//                outputbytes.remove(0);
//            }
//
//            //change \n to \r\n
//            for (int i = 0; i < warcString.length(); i++) {
//                if (i > 0 && warcString.charAt(i) == '\n' && warcString.charAt(i - 1) != '\r') {
//                    warcString = new StringBuilder(warcString).insert(i, "\r").toString();
//                    i++;
//                }
//            }
//
//
//            //add the things from warcstring to the start of the outputbytes arraylist
//            for (int i = warcString.length() - 1; i >= 0; i--) {
//                outputbytes.add(0, (byte) warcString.charAt(i));
//            }
//
//            warcFileBytes.addAll(outputbytes);
//
//            //prints the warc file headers
////            warcFileString += warcString;
////            System.out.println(warcFileString);
//
//            fInputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//}
