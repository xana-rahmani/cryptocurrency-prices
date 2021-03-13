package com.example.cryptho.data;

public class MyMessage {
    // Notification Text
    final private String[] NotifText = {
            "No internet connection  :(",  // arg = 0
            "Cant get new coins data   :(",  // arg = 1
    };


    // Messages Type
    public int SHOW_NOTIFICATION = 0;
    public int UPDATE_COINS_DATA_LIST = 1;

    public String getNotifText(int arg1){
        return this.NotifText[arg1];
    }
}
