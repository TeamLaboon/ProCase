package com.flipbox.skyline.procase.Database;
/**
 * Created by Hasby on 10/06/2015.
 */
public class Company {
    private int _id;
    private String _name;
    private String _token;
    private String _address;

    public Company(){}
    public Company(String name, String address, String token){
        this._name = name;
        this._token = token;
        this._address = address;
    }
    public Company(int id, String name, String address, String token){
        this._id = id;
        this._name = name;
        this._token = token;
        this._address = address;
    }

    public int getID(){ return this._id; }
    public void setID(int id){ this._id = id; }
	
    public String getName(){ return this._name; }
    public void setName(String name){ this._name = name; }
	
    public String getToken(){ return this._token; }
    public void setToken(String token){ this._token = token; }

    public String getAddress(){ return this._address; }
    public void setAddress(String address){ this._address = address; }
}