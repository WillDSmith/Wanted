package com.xtremeware.wanted;

/**
 * Created by Shakey on 12/8/13.
 */
public class List {

    private long id;
    private String name;

    public List()
    {

    }

    public List(String name) {
        this.name = name;
    }

    public List(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {

        // This method is used for the add/edit spinner
        return name;
    }

}
