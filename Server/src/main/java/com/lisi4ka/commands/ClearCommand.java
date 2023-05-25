package com.lisi4ka.commands;

import com.lisi4ka.models.City;
import java.util.List;
import static com.lisi4ka.utils.DefaultSave.defaultSave;


public class ClearCommand implements Command{
    private final List<City> collection;
    public ClearCommand(List<City> collection) {
        this.collection = collection;
    }


    @Override
    public String execute() {
        collection.clear();
        return "\nCollection cleaned!\n" + defaultSave(collection);
    }
}
