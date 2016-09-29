package com.watchdog;

/**
 * Created by jmullen on 9/25/16.
 */

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//import org.myorg.SystemUnavailableException;

public final class PasswordService {
    private static PasswordService instance;

    public PasswordService() {
    }

    public static synchronized PasswordService getInstance() //step 1
    {
        if (instance == null) {
            instance = new PasswordService();
        }
        return instance;
    }

    public synchronized String encrypt(String plaintext) throws Exception {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA"); //step 2
        } catch (NoSuchAlgorithmException e) {
            throw new Exception(e.getMessage());
        }
        try {
            md.update(plaintext.getBytes("UTF-8")); //step 3
        } catch (UnsupportedEncodingException e) {
            throw new Exception(e.getMessage());
        }

        byte raw[] = md.digest(); //step 4
        String hash = (new BASE64Encoder()).encode(raw); //step 5
        return hash; //step 6
    }
}