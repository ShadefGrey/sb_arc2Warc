//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
///**
// * Created by l_ckha on 08-09-2016.
// */
//public class ViewableGif {
//    private UUID warcInfoId;
//    private int contentLength;
//    private WarcRecord warcInfoRecord = new WarcRecord(WarcRecordType.WARCINFO);
//    private String pageName;
//    private Service service;
//
//    public ViewableGif() {
//        warcInfoId = UUID.randomUUID();
//        pageName = "soeg.sol.dk"; //TODO should automatically set the right page name
//        contentLength = 0;
//        service = new Service();
//    }
//
//    public void fixGif(String gifToFix, String fixedGifName) {
//        File file = new File(gifToFix);
//        File outputFile = new File(fixedGifName);
//        List<Byte> outputbytes = new ArrayList<>();
////        String outputtest = ""; //TEST
//        try (FileInputStream fInputStream = new FileInputStream(file)) {
//            System.out.println("Total file size to read (in bytes) : " + fInputStream.available());
//
//
//            FileOutputStream fOutputStream = new FileOutputStream(outputFile);
//            if (!outputFile.exists()) {
//                outputFile.createNewFile();
//            }
//            int content;
//            while ((content = fInputStream.read()) != -1) {
//                // convert to char and display it
//                outputbytes.add((byte) content);
////                outputtest += (char) content; //TEST
//            }
//
//            int linefeeds = 0;
//            int toDelete = 0;
//            for (byte b : outputbytes) { //a while loop would be better
//                if (linefeeds != 2) {
//                    if ((char) b == '\n') {
//                        linefeeds++;
//                        toDelete++;
//                    } else {
//                        toDelete++;
//                        linefeeds = 0;
//                    }
//                }
//            }
//
////            for (int i = 0; i < outputtest.length(); i++) { //TEST
////                if (linefeeds != 2) {
////                    if (outputtest.charAt(i) == '\n') {
////                        linefeeds++;
////                        toDelete++;
////                    } else {
////                        toDelete++;;
////                        linefeeds = 0;
////                    }
////                }
////
////            }
//
//            //print out the deleted part
////            String deletedPart = "";
////            for (int i = 0; i < toDelete; i++) {
////                deletedPart +=(char)(byte)outputbytes.get(i);
////            }
////            System.out.println(deletedPart);
//
//            for (int i = 0; i < toDelete; i++) {
//                outputbytes.remove(0);
//            }
//
//            byte[] output = new byte[outputbytes.size()];
//
//            for (int i = 0; i < outputbytes.size(); i++) {
//                output[i] = outputbytes.get(i);
//
//            }
//            fOutputStream.write(output);
//            fOutputStream.close();
//            fInputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void WarcifyGif(String gifToFix, String fixedWarcName) {
//        File file = new File(gifToFix);
//        File outputFile = new File(fixedWarcName);
//        List<Byte> outputbytes = new ArrayList<>();
//
//        try (FileInputStream fInputStream = new FileInputStream(file)) {
//            System.out.println("Total file size to read (in bytes) : " + fInputStream.available());
//            contentLength = fInputStream.available();
//            System.out.println(contentLength);
//
//            //make the file the outputstream should write to
//            FileOutputStream fOutputStream = new FileOutputStream(outputFile);
//            if (!outputFile.exists()) {
//                outputFile.createNewFile();
//            }
//            //add the content from the inputstream to the outputbytes arraylist
//            int content;
//            while ((content = fInputStream.read()) != -1) {
//                outputbytes.add((byte) content);
//            }
//
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
//            String inputToAdjust = "";
//            for (int i = 0; i < toDelete; i++) {
//                inputToAdjust += (char) (byte) outputbytes.get(i);
//            }
//
//            String warcString =
//                    warcInfoRecord.warcInfoRecord() +
//                            new WarcRecord(WarcRecordType.RESPONSE).warcResponseRecord(warcInfoId, gifToFix, contentLength, pageName);
////                            +
////                            "WARC/1.0\n" +
////                            "WARC-Type: " + "response\n" +
////                            "WARC-Target-URI: " + "http://www.soeg.sol.dk/img/kv_logo.gif\n" +
////                            "WARC-Warcinfo-ID: <urn:uuid:" + warcInfoRecord.getWarcinfoId() + ">\n" +
////                            "WARC-Date: " + dateP.toWarcDateFormat(gifToFix) + "\n" +
////                            "WARC-IP-Address: " + "0.0.0.0\n" +
////                            "WARC-Record-ID: " + "<urn:uuid:" + UUID.randomUUID() + ">\n" +
////                            "Content-Type: " + "application/http;msgtype=response\n" +
////                            "WARC-Identified-Payload-Type: " + "image/gif\n" +
////                            "Content-Length: " + contentLength + "\n" + //TODO der er \r\n\r\n før content imellem header og content block der ikke bliver talt med, ikke sikker på hvor den skal tælle fra, den siger det passer hvis jeg adder 1 til content lenght men kan ikke se hvorfor
////                            "\n";
//
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
//            //add the things from warcstring to the start of the outputbytes arraylist
//            for (int i = warcString.length() - 1; i >= 0; i--) {
//                outputbytes.add(0, (byte) warcString.charAt(i));
//            }
//
//            //change \n to \r\n in the whole file (seems to be the wrong choice, shouldn't do that in the actual content only the headers)
////            for (int i = 0; i < outputbytes.size(); i++) {
////                if (outputbytes.get(i) == '\n' && outputbytes.get(i - 1) != '\r') {
////                    outputbytes.add(i,(byte)'\r');
////                    i++;
////                }
////            }
//            byte[] output = new byte[outputbytes.size()];
//
//            //add the content from the outputbytes arraylist to a byte array the outputstream can use
//            for (int i = 0; i < outputbytes.size(); i++) {
//                output[i] = outputbytes.get(i);
//            }
//
//            fOutputStream.write(output);
//            fOutputStream.close();
//            fInputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public static void main(String[] args) {
//        ViewableGif v = new ViewableGif();
////        v.fixGif("C:\\Users\\ckha\\Desktop\\sb_pack\\noget\\unpackedsoeg.sol\\soeg.sol.dk\\2001-12-02.1601\\img\\solsog.gif", "C:\\Users\\ckha\\Desktop\\testoutput.gif");
//
////        v.responseRecordToFile("C:\\Users\\ckha\\Desktop\\sb_pack\\noget\\unpackedsoeg.sol\\soeg.sol.dk\\2001-12-03.1601\\img\\kv_logo.gif", "C:\\Users\\ckha\\Desktop\\warcifiedoutput2.warc");
//
//        v.WarcifyGif("C:\\Users\\ckha\\Desktop\\sb_pack\\noget\\unpackedsoeg.sol\\soeg.sol.dk\\2001-12-02.1601\\img\\solsog.gif", "C:\\Users\\ckha\\Desktop\\warcifiedoutput.warc");
//    }
//}
