package com.flipbox.skyline.procase.Database;
/**
 * Created by Hasby on 10/06/2015.
 */
public class Client {
    private int _id;
    private String _name;
    private String _logo;

    public Client(){}
    public Client(String name, String logo){
        this._name = name;
        this._logo = logo;
    }
    public Client(int id, String name, String logo){
        this._id = id;
        this._name = name;
        this._logo = logo;
    }

    public int getID(){
        return this._id;
    }
    public void setID(int id){
        this._id = id;
    }
    public String getName(

    ){
        return this._name;
    }
    public void setName(String name){
        this._name = name;
    }
    public String getLogo(){
        return this._logo;
    }
    public void setLogo(String logo){
        this._logo = logo;
    }
}