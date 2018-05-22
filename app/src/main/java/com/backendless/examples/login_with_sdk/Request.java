package com.backendless.examples.login_with_sdk;
import java.util.Date;

public class Request {

    private String request_url;
    private String pointa;
    private String pointb;
    private String ownerID;
    private Date departdate;
    private Date returndate;
    public String objectId;

//    public String getObjectId()
//    {
//        return objectId;
//    }

//    public void setObjectId( String objectId )
//    {
//        this.objectId = objectId;
//    }

    public Date getDepartdate(){ return departdate; }

    public void setDepartdate(Date departdate){ this.departdate = departdate; }

    public Date getReturndate(){
        return returndate;
    }

    public void setReturndate(Date returndate){ this.returndate = returndate; }

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
}
