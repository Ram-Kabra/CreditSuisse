package com.code.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class Main {
	public static void main(String[] args) throws Exception {
        String filePath = JSONFileRetriever.getTheJsonPath();
        JSONFileRetriever myJsonFile = new JSONFileRetriever(filePath);
        if (!myJsonFile.isJSONFileExists() || !myJsonFile.isJSONFile()) {
            throw new FileNotFoundException("The JSON file has not been found in the following path: "
                    + filePath);
        }
        File myFile = myJsonFile.getFile();
        LogsCollectionBuilder listBuilder = new LogsCollectionBuilder();
        List <ServerLog> arr = listBuilder.parseFile(myFile);
        listBuilder.setTheAlertFlagsForDelayedEvents(arr);
        HSQLDatabase.exportToDatabase(arr);
        if (LogsCollectionBuilder.isPreviewLogApproved()) {
            listBuilder.getTheEventList(arr, 3);
        }
        System.out.println("Process has been successfully finished!");
    }
}
