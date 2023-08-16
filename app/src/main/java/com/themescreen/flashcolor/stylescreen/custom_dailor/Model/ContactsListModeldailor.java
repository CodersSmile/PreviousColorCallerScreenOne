package com.themescreen.flashcolor.stylescreen.custom_dailor.Model;

import java.util.Comparator;

public class ContactsListModeldailor {
    public static Comparator<ContactsListModeldailor> contactsname = new Comparator<ContactsListModeldailor>() {

        public int compare(ContactsListModeldailor contactsListModeldailor, ContactsListModeldailor contactsListModeldailor2) {
            return contactsListModeldailor.getName().compareTo(contactsListModeldailor2.getName());
        }
    };
    String id;
    String name;
    String number;
    String photourl;

    public ContactsListModeldailor(String str, String str2, String str3, String str4) {
        this.id = str;
        this.name = str2;
        this.number = str3;
        this.photourl = str4;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String str) {
        this.number = str;
    }

    public String getPhotourl() {
        return this.photourl;
    }

    public void setPhotourl(String str) {
        this.photourl = str;
    }
}
