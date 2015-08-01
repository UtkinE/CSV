package net.vazem.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SourceData {

    private File source;

    public SourceData(File file){
        this.source = file;
    }

    public String[] getAllColumnNames(boolean isHeader, String separator){
        try {
            BufferedReader bf = new BufferedReader(new FileReader(source));
            if(isHeader){
                return bf.readLine().split(separator);
            } else {
                Integer countColumn = bf.readLine().split(separator).length;
                String[] nameColumn = new String[countColumn];
                for(int i=0;i<countColumn;i++){
                    nameColumn[i] = "COLUMN" + i;
                }
                return nameColumn;
            }

        } catch (IOException e){

        }
        return null;

    }

}