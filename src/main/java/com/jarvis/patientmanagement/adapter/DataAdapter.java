package com.jarvis.patientmanagement.adapter;

public class DataAdapter {
    public String Hospital_Image_Url, Hospital_Name;
    int Id;
    public String Hospital_Address, Hospital_Phone, Hospital_Location;

    public String getHospitalImageUrl() {
        return Hospital_Image_Url;
    }

    public void setHospitalImageUrl(String imageServerUrl) {
        this.Hospital_Image_Url = imageServerUrl;
    }

    public String getHospitalName() {
        return Hospital_Name;
    }

    public void setHospitalName(String Hospitalname) {
        this.Hospital_Name = Hospitalname;
    }

    public String getHospitalAddress() {
        return Hospital_Address;
    }

    public void setHospitalAddress(String address) {
        this.Hospital_Address = address;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id1) {
        this.Id = Id1;
    }

    public String getHospitalPhone() {
        return Hospital_Phone;
    }

    public void setHospitalPhone(String phone_number1) {
        this.Hospital_Phone = phone_number1;
    }

    public String getHospitalLocation() {
        return Hospital_Location;
    }

    public void setHospitalLocation(String subject1) {
        this.Hospital_Location = subject1;
    }
}