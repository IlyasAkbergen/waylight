package com.backendless.examples.login_with_sdk;

public class Ticket {
    public String pointa, pointb, flightClass, departdate, returndate, request_url;
    public int price;

    public Ticket(){ }

    public Ticket(String pointa, String pointb, String flightClass, String departdate, String returndate, int price){
        this.pointa = pointa;
        this.pointb = pointb;
        if(flightClass.equals(""))
            this.flightClass = null;
        else
            this.flightClass = flightClass;
        if(departdate.equals(""))
            this.departdate = null;
        else
            this.departdate = departdate;
        if(returndate.equals(""))
            this.returndate = null;
        else
            this.returndate = returndate;
    }

    public String getPointa(){
        return pointa;
    }

    public void setPointa(String pointa){
        this.pointa = pointa;
    }

    public String getPointb(){
        return pointb;
    }

    public void setPointb(String pointb){
        this.pointb = pointb;
    }

    public String getFlightClass(){
        return flightClass;
    }

    public void setFlightClass(String flightClass){
        this.flightClass = flightClass;
    }

    public String getDepartdate(){
        return departdate;
    }

    public void setDepartdate(String departdate){
        this.departdate = departdate;
    }

    public  String getReturndate(){
        return returndate;
    }
    public void setReturndate(String returndate){
        this.returndate = returndate;
    }

    public int getPrice(){ return price;}
    public void setPrice(int price){ this.price = price;}

}
