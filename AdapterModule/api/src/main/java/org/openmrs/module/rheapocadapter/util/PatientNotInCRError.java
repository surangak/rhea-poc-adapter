package org.openmrs.module.rheapocadapter.util;

/**
 * Created with IntelliJ IDEA.
 * User: ng1
 * Date: 4/10/13
 * Time: 11:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class PatientNotInCRError extends Exception{
    String msg = "";

    public PatientNotInCRError() {
    }
    public PatientNotInCRError(String str){
//        super(str);
        msg=str;
    }
    public String toString(){
        return msg;
    }
}
