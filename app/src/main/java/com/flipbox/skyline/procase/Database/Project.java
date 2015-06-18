package com.flipbox.skyline.procase.Database;

/**
 * Created by Hasby on 11/06/2015.
 */
public class Project {
    private int _id;
    private int _company_id;
    private String _name;
    private String _type;
    private String _logo;
    private String _description;
    private String _prototype;

    public Project(){}
    public Project(String name, String type, String logo, String description, String prototype){
        this._name = name;
        this._type = type;
        this._logo = logo;
        this._description = description;
        this._prototype = prototype;
    }
    public Project(int id, int company_id, String name, String type, String logo, String description, String prototype){
        this._id = id;
        this._company_id = company_id;
        this._name = name;
        this._type = type;
        this._logo = logo;
        this._description = description;
        this._prototype = prototype;
    }

    public int getID() { return this._id; }
    public void setID(int id){ this._id = id; }

    public int getCompanyID() { return this._company_id; }
    public void setCompanyID(int companyID){ this._company_id = companyID; }

    public String getName(){return this._name;}
    public void setName(String name){this._name = name;}

    public String getType(){ return this._type; }
    public void setType(String type){ this._type = type; }

    public String getLogo(){ return this._logo; }
    public void setLogo(String logo){ this._logo = logo; }

    public String getDescription(){ return this._description; }
    public void setDescription(String description){ this._description = description; }

    public String getPrototype(){ return this._prototype; }
    public void setPrototype(String prototype){ this._prototype = prototype; }
}