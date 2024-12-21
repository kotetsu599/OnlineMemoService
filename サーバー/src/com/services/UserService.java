package com.services;

import com.utils.DataManager;
import com.utils.Validator;

public class UserService{
    public static boolean RegisterUser(String UserName,String Password){
        if(Validator.IsValidateRegister(UserName,Password)){
            DataManager.SaveUserInfo(UserName,Password);
            return true;
        }else{
            return false;
        }
    }

    public static boolean LoginUser(String UserName,String Password){
        
        if (Validator.IsValidateLogin(UserName, Password)){
            return true;
        }else{
            return false;
        }

    }


}