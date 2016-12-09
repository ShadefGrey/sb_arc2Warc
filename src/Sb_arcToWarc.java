import org.jwat.warc.*;
import org.jwat.warc.WarcRecord;

import java.io.*;
import java.util.*;

/**
 * Created by l_ckha on 26-09-2016.
 */
public class Sb_arcToWarc {
    private File srcFile;
    private File destDir;
    private String pageName;


    private String dstFname;
    private String tmpFname;
    private File tmpDstFile;
    private File dstFile;

    private Service service;
    private BufferedOutputStream file_out;
    private WarcWriter writer;
    private org.jwat.warc.WarcRecord record;
    private Long contentLength;
    private String contentType;
    private int recordCount;
    private UUID warcinfoUuid = null;
    //                UUID filedescUuid = null;
    private UUID recordUuid = null;

    private int stillGoing = 0;


    public Sb_arcToWarc(File srcFile, File destDir, String pageName) {
        try {
            this.srcFile = srcFile;
            this.destDir = destDir;
            //TODO since pageName is set in the constructor it can only make a warc file from a single site at a time, as it is now
            this.pageName = pageName;
            recordCount = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void convert(String currentPath) {
        try {
                        /*
                         *  Generate filedesc uuid if the arc record is a version block record.
						 */
//                    if (arcRecord.recordType == ArcRecordBase.RT_VERSION_BLOCK) {
//                filedescUuid = UUID.randomUUID();
//                    }
                        /*
                         * Is the first record a version block record?
						 */
//                    if (recordCount == 0 && arcRecord.recordType != ArcRecordBase.RT_VERSION_BLOCK) {
//                        // TODO Warning, missing filedesc as first record in ARC file.
//                    }


            //TODO must do something to get a non arcrecord managed payload?
//                        managedPayload.manageArcRecord(arcRecord, true);

//                        httpHeader = managedPayload.httpHeader;
//                        if (httpHeader == null || !httpHeader.isValid()) {
//                            //savePayload(managedPayload);
//
//                            // Optional
//								/*
//								int number = getNextPayloadErrorNumber();
//								savePayloadErrorArc(arcRecord, managedPayload, number);
//								*/
//
//                            managedPayload = repairPayload.repairPayload(managedPayload, arcRecord.header.contentTypeStr, arcRecord.header.archiveDate);
//                        }


            //TODO most likely doesnt get a header
//                httpHeader = managedPayload.httpHeader;
//                if (httpHeader != null && httpHeader.isValid()) {
//                    if (httpHeader.headerType == HttpHeader.HT_RESPONSE) {
//                        contentType = "application/http; msgtype=response";
//                    } else if (httpHeader.headerType == HttpHeader.HT_REQUEST) {
//                        contentType = "application/http; msgtype=request";
//                    } else {
//                        throw new IllegalStateException("Unknown header type!");
//                    }
//                } else {

            //TODO temporary, only works if everything is response
            contentType = "application/http; msgtype=response";

            ArrayList<Byte> httpHeaderBytes;
            if (contentType.equals("application/http; msgtype=response")) {
                httpHeaderBytes = service.getHtppHeader(new File(currentPath));
            } else httpHeaderBytes = new ArrayList<>();
            ArrayList<Byte> tmpContentFile = service.getPayload(new File(currentPath));


            //TODO trying to get block digest to work (when i use this for the warcBlockDigest it tells me the 'digestValue' is null
//                ArrayList<Byte> blockToDigest = httpHeaderBytes;
//                blockToDigest.addAll(tmpContentFile);
//                byte[] blockBytes = service.arrayListToArray(blockToDigest);


            //TODO managedPayload is null
//                warcBlockDigest = WarcDigest.createWarcDigest("SHA1", blockBytes, "base32", Base32.encodeArray(managedPayload.blockDigestBytes));
//                warcPayloadDigest = WarcDigest.createWarcDigest("SHA1", managedPayload.payloadDigestBytes, "base32", Base32.encodeArray(managedPayload.payloadDigestBytes));


            String warcTURI = service.makeUriFromPath(currentPath, pageName);
            String warcDate = service.toWarcDateFormat(currentPath, pageName);


            recordUuid = UUID.randomUUID();
            record = WarcRecord.createRecord(writer);
            record.header.addHeader(WarcConstants.FN_WARC_TYPE, WarcConstants.RT_RESPONSE);
            record.header.addHeader(WarcConstants.FN_WARC_TARGET_URI, warcTURI);
            record.header.addHeader(WarcConstants.FN_WARC_DATE, warcDate);
            record.header.addHeader(WarcConstants.FN_WARC_RECORD_ID, "<urn:uuid:" + recordUuid + ">");
            record.header.addHeader(WarcConstants.FN_WARC_IP_ADDRESS, "0.0.0.0");
            record.header.addHeader(WarcConstants.FN_WARC_WARCINFO_ID, "<urn:uuid:" + warcinfoUuid + ">");
            contentLength = (long) (httpHeaderBytes.size() + tmpContentFile.size());
//                if (contentLength > 0) { //TODO for now it will have no warc og payload digest, should be okay since the field is optional
//                    record.header.addHeader(WarcConstants.FN_WARC_BLOCK_DIGEST, warcBlockDigest, null);
//                    if (managedPayload.httpHeaderLength > 0 && managedPayload.payloadLength > 0) {
//                        record.header.addHeader(WarcConstants.FN_WARC_PAYLOAD_DIGEST, warcPayloadDigest, null);
//                    }
//                }

            record.header.addHeader(WarcConstants.FN_CONTENT_LENGTH, contentLength, null);
            if (contentType != null) {
                record.header.addHeader(WarcConstants.FN_CONTENT_TYPE, contentType);
            }

            writer.writeHeader(record);

            //TODO i should be able to use writer.streampayload if i get my own inputstream for it (split my inputstream into a http and a main body part)
            //this should be the header stream
            if (httpHeaderBytes != null) {
                byte[] output = service.arrayListToArray(httpHeaderBytes);
                writer.writePayload(output);
            }

            //this should be the block stream
            if (tmpContentFile != null) {
                byte[] output = service.arrayListToArray(tmpContentFile);
                writer.writePayload(output);
            }
            writer.closeRecord();
            ++recordCount;
//                }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void convertAll() {
        service = new Service();

        file_out = null;
        writer = null;
//        ManagedPayload managedPayload = null;
//        HttpHeader httpHeader;
//        WarcDigest warcBlockDigest;
//        WarcDigest warcPayloadDigest;

        try {

            dstFname = "fromsb." + pageName;
            dstFname = dstFname.substring(0, dstFname.lastIndexOf(".")) + ".warc";

            tmpFname = dstFname + ".open";

            tmpDstFile = new File(destDir, tmpFname);
            dstFile = new File(destDir, dstFname);


            if (dstFile.exists() && !dstFile.isFile()) {
                throw new IOException("Destination file is a directory: '" + dstFile.getPath() + "'");
            }

//            if (!dstFile.exists()) { //TODO right now it overwrites, maybe it should not.
            if (tmpDstFile.exists()) {
                if (!tmpDstFile.isFile()) {
                    throw new IOException("Temporary destination file is a directory: '" + tmpDstFile.getPath() + "'");
                }
                if (!tmpDstFile.delete()) {
                    throw new IOException("Could not delete file: '" + tmpDstFile.getPath() + "'");
                }
            }
            if (!tmpDstFile.exists()) {
                tmpDstFile.createNewFile();
            }
            file_out = new BufferedOutputStream(new FileOutputStream(tmpDstFile), 8192);
            writer = WarcWriterFactory.getWriter(file_out, 8192, false);


                        /*
                         *  Write a warcinfo record if is the first record or if it is a version block record.
						 */
            if (recordCount == 0) {
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTimeZone(TimeZone.getTimeZone("UTC"));
                cal.setTimeInMillis(System.currentTimeMillis());
                warcinfoUuid = UUID.randomUUID();
                record = WarcRecord.createRecord(writer);
                record.header.addHeader(WarcConstants.FN_WARC_TYPE, WarcConstants.RT_WARCINFO);
                record.header.addHeader(WarcConstants.FN_WARC_DATE, cal.getTime(), null);
                record.header.addHeader(WarcConstants.FN_WARC_FILENAME, dstFname);
                record.header.addHeader(WarcConstants.FN_WARC_RECORD_ID, "<urn:uuid:" + warcinfoUuid + ">");
                record.header.addHeader(WarcConstants.FN_CONTENT_TYPE, "application/warc-fields");
                record.header.addHeader(WarcConstants.FN_CONTENT_LENGTH, "0");
                // Standard says no.
                //record.header.addHeader(WarcConstants.FN_WARC_CONCURRENT_TO, "<urn:uuid:" + filedescUuid + ">");
                writer.writeHeader(record);
                writer.closeRecord();
                ++recordCount;
            }

            //TODO the method to convert the files from sb_arc to a single warc file

//            convert(srcFile.getAbsolutePath());
            recursiveConvert(srcFile.getAbsolutePath());


            file_out.close();
            if (!tmpDstFile.renameTo(dstFile)) {
                throw new IOException("Could not rename '" + tmpDstFile.getPath() + "' to '" + dstFile.getPath() + "'");
            }
//            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void recursiveConvert(String dirPath) {
        File f = new File(dirPath);
        File[] files = f.listFiles();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String affix = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1);
                if (file.isFile() && !affix.equals("orig")) {
                    convert(file.getAbsolutePath());
                    stillGoing++;
                    //stillGoing is just a counter that tells me it is still going for every 1000 files made.
                    if (stillGoing % 1000 == 0) {
                        System.out.println("Still going " + stillGoing);
                    }
                }

                if (file.isDirectory()) {
                    recursiveConvert(file.getAbsolutePath());
                }
            }
        }
    }
}
