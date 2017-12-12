package utils;

import au.com.bytecode.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;

public class Output {

    private static CSVWriter csvWriter = null;
    private static String[] headers = {"ID","NODES","EDGES","TOT_MOVES","LEADER_MOVES","SHADOWS"};

    public static void CSVWriteOneLine( String[] content, String filePath){
        try{
            Initial(filePath);
            csvWriter = new CSVWriter(new FileWriter(filePath,true));
            csvWriter.writeNext( content );
            csvWriter.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void Initial(String filePath){
        try{
            File f = new File(filePath);
            if( !f.exists() ){
                csvWriter = new CSVWriter(new FileWriter(filePath,false));
                csvWriter.writeNext(headers);
                csvWriter.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
