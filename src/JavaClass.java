
/**
 * Created by l_ckha on 25-08-2016.
 */
//package org.archive.format.warc;
//import org.archive.format.ArchiveFileConstants;

import java.io.*;


public class JavaClass {


    public static void main(String[] args) throws IOException {


        //TODO splitting a warc (10.000.000 makes a file of ca 10 mb)
//        String pathOfWarc = "C:\\Users\\ckha\\Desktop\\bad.www.jp.dk20040220131901.warc";
//
//        //split the file at 10.000.000 bytes
//        String pathOfSplits = "C:\\Users\\ckha\\Desktop\\jpwarcsplit\\bad.www.jp.fullTest.warc";
//        SplitWarcFile s = new SplitWarcFile(pathOfWarc, pathOfSplits, 10000000);
//
//        //split the file at 1.000.000
//        String pathOfSmallerSplits ="C:\\Users\\ckha\\Desktop\\jpwarcsplitsmaller\\bad.www.jp.fullTest.warc";
//        SplitWarcFile s = new SplitWarcFile(pathOfWarc, pathOfSmallerSplits, 1000000);
//
//        //splits the file with a problem at 10.000
//        String pathOfWarc ="C:\\Users\\ckha\\Desktop\\jpwarcsplitsmaller\\bad.www.jp.fullTest3(this_one).warc";
//        String pathOfSmallestSplits ="C:\\Users\\ckha\\Desktop\\jpwarcsmallest\\bad.www.jp.fullTest.warc";
//        SplitWarcFile s = new SplitWarcFile(pathOfWarc, pathOfSmallestSplits, 10000);
//
//        s.splitIt();


//        File srcDir = new File("C:\\Users\\ckha\\Desktop\\sb_pack\\noget\\unpackedsoeg.sol\\soeg.sol.dk\\2001-12-03.1601\\img"); //a few gif images
//        File srcFile = new File("C:\\Users\\ckha\\Desktop\\sb_pack\\noget\\unpackedsoeg.sol\\soeg.sol.dk\\2001-12-03.1601\\index.html"); //just 1 index page (the method now only works on dirs)

        //TODO convert a sb_arc file to warc
//        File srcDir = new File("C:\\Users\\ckha\\Desktop\\sb_pack\\noget\\unpackedsoeg.sol\\soeg.sol.dk\\2001-11-23.1601"); //the current warc file in sbArc2WarcTest
//        File destDir = new File("C:\\Users\\ckha\\Desktop\\sbArc2WarcTest");


//        File srcDir = new File("C:\\Users\\ckha\\Desktop\\sb_pack\\noget\\unpackedsoeg.sol\\soeg.sol.dk\\2001-11-13.1601"); //warcify everything 2001-11-13, current file in sbArc2WarcTest2
//        File destDir = new File("C:\\Users\\ckha\\Desktop\\sbArc2WarcTest2");

//        File srcDir = new File("C:\\Users\\ckha\\Desktop\\sb_pack\\noget\\unpackedsoeg.sol\\soeg.sol.dk\\2001-12-03.1601"); //warcify everything 2001-12-03, current file in sbArc2WarcTest (on E:)
//        File srcDir = new File("E:\\sb_pack\\soeg.sol.dk\\2001-11-25.1601");
//        File destDir = new File("E:\\sbArc2WarcTest");
//        Sb_arcToWarc sba2w = new Sb_arcToWarc(srcDir, destDir, "soeg.sol.dk");


        //TODO den kan ikke vise en af filerne fra extra posten, en anden virker og både style og alle billeder lader til at være der, men links fungerer ikke
        //TODO fejlen lader til at være pga af hvordan sys fungerer i python 2.7, har prøvet at lave et foreslået fix, men lader ikke til at virke.
        //TODO ved ikke om det er pga. fikset ikke virker, eller fordi den har lavet fejl filen da den blev pakket ind med Python programmet i sin tid.
//        File srcDir = new File("E:\\sb_pack\\www.extra-posten.dk\\2001-11-02.1419");
        File srcDir = new File("E:\\sb_pack\\www.extra-posten.dk\\2001-11-28.1601");
        File destDir = new File("E:\\sbArc2WarcTest");
        Sb_arcToWarc sba2w = new Sb_arcToWarc(srcDir, destDir, "www.extra-posten.dk");


        //TODO viser filerne fra radio2.dk, lader til at virke, dog nogle applikationer java security blokerer for
//        File srcDir = new File("E:\\sb_pack\\www.radio2.dk\\2001-12-03.1601");
//        File destDir = new File("E:\\sbArc2WarcTest");
//        Sb_arcToWarc sba2w = new Sb_arcToWarc(srcDir, destDir, "www.radio2.dk");


        //TODO kan vise siden, er ikke helt sikker på hvordan man navigerer rundt. Nogle links der lader til ikke at virke, som jeg ikke forstår
//        File srcDir = new File("E:\\sb_pack\\www.frikirke.dk\\2001-12-03.1601");
//        File destDir = new File("E:\\sbArc2WarcTest");
//        Sb_arcToWarc sba2w = new Sb_arcToWarc(srcDir, destDir, "www.frikirke.dk");

        sba2w.convertAll();

    }
}
