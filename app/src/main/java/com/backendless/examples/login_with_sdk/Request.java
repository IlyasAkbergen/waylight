package com.backendless.examples.login_with_sdk;
import java.util.Date;

public class Request {

    private String request_url;
    private String pointa;
    private String pointb;
    private String ownerID;
    private Date departdate;
    private Date returndate;

    public String getRequest_url(){
        return request_url;
    }

    public void setRequest_url(String request_url){
        this.request_url = request_url;
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

    public String getOwnerID(){
        return ownerID;
    }

    public Date getDepartdate(){
        return departdate;
    }

    public Date getReturndate(){
        return returndate;
    }
}
