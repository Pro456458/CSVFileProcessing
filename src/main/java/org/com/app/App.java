package org.com.app;

/**
 write a java function to process an irregular csv file. Each row can have missing data, and
 number of columns may vary across rows. Replace missing value with :
 0 for integer.
 "_" for String.
 Returned the corrected CSV.
 <br><br>
 Run AppTest.java to test the code.
 **/
public class App 
{
    public static void main( String[] args )
    {
        String filePath=args[0];
        String modifiedFilePath=args[1];

        CsvProcessor csvProcessor = new CsvProcessor();
        csvProcessor.readAndProcessCsv(filePath,modifiedFilePath);

    }
}
