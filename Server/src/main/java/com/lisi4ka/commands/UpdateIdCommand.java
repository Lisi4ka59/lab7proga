package com.lisi4ka.commands;

import com.lisi4ka.models.*;
import com.lisi4ka.utils.CityComparator;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static com.lisi4ka.utils.BdConnect.conn;
import static com.lisi4ka.utils.Checker.checkDate;

public class UpdateIdCommand implements Command {
    public String message = "";
    private final List<City> collection;

    public UpdateIdCommand(List<City> collection) {

        this.collection = collection;
    }

    private String updateArgs(String[] args, String user) {
        boolean update = false;
        int id = Integer.parseInt(args[0]);
        for (City city : collection) {
            if (city.getId() == id) {
                if (!user.equals(city.getUser())) {
                    return String.format("City %d does not belong to you\n", id);
                }
                if (isUpdate(id, "name", args[1])) {
                    city.setName(args[1]);
                }
                if (isUpdate(id, "coordinate_x", Double.parseDouble(args[2]))) {
                    double x = Double.parseDouble(args[2]);
                    Coordinates coordinates = city.getCoordinates();
                    coordinates.setX(x);
                    city.setCoordinates(coordinates);
                }
                if (isUpdate(id, "coordinate_y", Float.parseFloat(args[3]))) {
                    float y = Float.parseFloat(args[3]);
                    Coordinates coordinates = city.getCoordinates();
                    coordinates.setY(y);
                    city.setCoordinates(coordinates);
                }
                if (isUpdate(id, "area", Double.parseDouble(args[4]))) {
                    city.setArea(Double.parseDouble(args[4]));
                }
                if (isUpdate(id, "population", Long.parseLong(args[5]))) {
                    city.setPopulation(Long.parseLong(args[5]));
                }
                if (isUpdate(id, "meters_above_sea_level", Integer.parseInt(args[6]))) {
                    city.setMetersAboveSeaLevel(Integer.parseInt(args[6]));
                }
                if (isUpdate(id, "climate", Climate.fromInt(Integer.parseInt(args[7])).name())) {
                    city.setClimate(Climate.fromInt(Integer.parseInt(args[7])));
                }
                if ("null".equals(args[8])) {
                    if (isUpdate(id, "government")) {
                        city.setGovernment(null);
                    }
                } else {
                    if (isUpdate(id, "government", Government.fromInt(Integer.parseInt(args[8])).name())) {
                        city.setGovernment(Government.fromInt(Integer.parseInt(args[8])));
                    }
                }
                if ("null".equals(args[9])) {
                    if (isUpdate(id, "standard_of_living")) {
                        city.setStandardOfLiving(null);
                    }
                } else {
                    if (isUpdate(id, "standard_of_living", StandardOfLiving.fromInt(Integer.parseInt(args[9])).name())) {
                        city.setStandardOfLiving(StandardOfLiving.fromInt(Integer.parseInt(args[9])));
                    }
                }
                Date birthday;
                if ("null".equals(args[10]) || "null".equals(args[11])) {
                    if (isUpdate(id, "governor_age") || isUpdate(id, "governor_birthday")) {
                        city.setGovernor(null);
                    }
                } else {
                    if (isUpdate(id, "governor_age", Long.parseLong(args[10])) || isUpdate(id, "governor_birthday", new java.sql.Date(checkDate(args[11]).getTime()))) {
                        long age = Long.parseLong(args[10]);
                        birthday = checkDate(args[11]);
                        city.setGovernor(new Human(age, birthday));
                    }
                }
                update = true;
                break;
            }
        }
        if (update)
            return String.format("City %d updated\n%s", id, message);
        else
            return String.format("City %d doesn't exist\n", id);
    }

    public boolean isUpdate(int id, String cityField, String newField) {
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE city SET " + cityField + " = ? WHERE id = ?");
            statement.setString(1, newField);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            message += cityField + " was not updated!\n";
            return false;
        }
        return true;
    }

    public boolean isUpdate(int id, String cityField, long newField) {
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE city SET " + cityField + " = ? WHERE id = ?");
            statement.setLong(1, newField);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            message += cityField + " was not updated!\n";
            return false;
        }
        return true;
    }

    public boolean isUpdate(int id, String cityField, float newField) {
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE city SET " + cityField + " = ? WHERE id = ?");
            statement.setFloat(1, newField);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            message += cityField + " was not updated!\n";
            return false;
        }
        return true;
    }

    public boolean isUpdate(int id, String cityField, int newField) {
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE city SET " + cityField + " = ? WHERE id = ?");
            statement.setInt(1, newField);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            message += cityField + " was not updated!\n";
            return false;
        }
        return true;
    }

    public boolean isUpdate(int id, String cityField, Date newField) {
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE city SET " + cityField + " = ? WHERE id = ?");
            statement.setDate(1, new java.sql.Date(newField.getTime()));
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            message += cityField + " was not updated!\n";
            return false;
        }
        return true;
    }

    public boolean isUpdate(int id, String cityField, double newField) {
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE city SET " + cityField + " = ? WHERE id = ?");
            statement.setDouble(1, newField);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            message += cityField + " was not updated!\n";
            return false;
        }
        return true;
    }

    public boolean isUpdate(int id, String cityField) {
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE city SET " + cityField + " = ? WHERE id = ?");
            statement.setNull(1, 12);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            message += cityField + " was not updated!\n";
            return false;
        }
        return true;
    }

    @Override
    public String execute(String args, String user) {
        String[] cityArgs = args.split(",");
        String answer = updateArgs(cityArgs, user);
        collection.sort(new CityComparator());
        return answer;
    }
}
