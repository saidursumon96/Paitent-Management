package com.jarvis.patientmanagement.config;

public class IP_Config {

    //*******************Main IP Config***************//
    //public static String URL_MAIN = "http://192.168.43.180/patientmanage/";
    //public static String URL_MAIN = "https://sumon12.000webhostapp.com/";
    //public static String URL_MAIN = "http://172.150.10.102/patientmanage/";
    public static String URL_MAIN = "http://192.168.88.253:8080/patientmanage/";

    //*******************Patient**********************//
    public static String URL_LOGIN_P = URL_MAIN + "patient/Login.php";
    public static String URL_REGISTER_P = URL_MAIN + "patient/Signup.php";

    //*******************Doctor***********************//
    public static String URL_LOGIN_D = URL_MAIN + "doctors/Login.php";
    public static String URL_REGISTER_D = URL_MAIN + "doctors/Signup.php";

    //********************Hospital List****************//
    public static String URL_H_L = URL_MAIN + "hospital/data.php";
    public static String URL_H_JSON_L = URL_MAIN + "hospital/ImageJsonData.php";

    //*******************Patient List*****************//
    public static String URL_P_L = URL_MAIN + "patientlist/StudentDetails.php";
    public static String URL_P_LIST = URL_MAIN + "patientlist/data.php";

    //*******************Doctor List******************//
    public static String URL_D_L = URL_MAIN + "doctorlist/StudentDetails.php";
    public static String URL_D_LIST = URL_MAIN + "doctorlist/data.php";

    //*******************DiseasesList*****************//
    public static String URL_DI_L = URL_MAIN + "diseaseslist/StudentDetails.php";
    public static String URL_DI_LIST = URL_MAIN + "diseaseslist/data.php";

    //*******************Patient Profile View*********//
    public static String URL_PP_V = URL_MAIN + "patient/data.php";
    public static String URL_PP_U = URL_MAIN + "patient/update.php";

    //*******************Doctor Profile View**********//
    public static String URL_DP_V = URL_MAIN + "doctors/data.php";
    public static String URL_DP_U = URL_MAIN + "doctors/update.php";

    //*******************Appoinment List***************//
    public static String URL_APP_LIST = URL_MAIN + "appoinmentlist/StudentDetails.php";
    public static String URL_APP_SET = URL_MAIN + "appoinmentlist/SetAppoinment.php";
}