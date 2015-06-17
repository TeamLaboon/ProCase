package com.flipbox.skyline.procase.Database;

/**
 * Created by putradiikaa on 15/06/2015.
 */
public class User {
    private int _id;
    private String _name;
    private String _type;
    private String _logo;

    public User(){}
    public User(String name, String type, String logo){
        this._name = name;
        this._type = type;
        this._logo = logo;
    }
    public User(int id, String name, String type, String logo){
        this._id = id;
        this._name = name;
        this._type = type;
        this._logo = logo;
    }

    public int getID(){ return this._id; }
    public void setID(int id){ this._id = id; }
    public String getName(){ return this._name; }
    public void setName(String name){ this._name = name; }
    public String getType(){ return this._type; }
    public void setType(String type){ this._type = type; }
    public String getLogo(){
        return this._logo;
    }
    public void setLogo(String logo){
        this._logo = logo;
    }
}
