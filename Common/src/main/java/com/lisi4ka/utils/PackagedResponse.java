package com.lisi4ka.utils;

import java.io.Serializable;

public class PackagedResponse implements Serializable {
    private final String message;
    private CommandMap commandMap = null;
    private int packageCount;
    private int packageNumber;

    public PackagedResponse(String message, int packageCount, int packageNumber, ResponseStatus status){
        this.message = message;
        this.packageCount = packageCount;
        this.status = status;
        this.packageNumber = packageNumber;
    }
    public PackagedResponse(CommandMap commandMap, String message){
        this.message = message;
        this.status = ResponseStatus.Map;
        this.commandMap = commandMap;
    }
    public PackagedResponse(String message, ResponseStatus status){
        this.message = message;
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public CommandMap getCommandMap(){return commandMap;}

    public int getPackageCount() {
        return packageCount;
    }
    public int getPackageNumber(){return packageNumber;}
    public ResponseStatus status;
}

