/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.repomaestro.searching;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * <p>
 * This interface is the super-interface of a state in a searching problem.<br>
 * The class that represent a state in a searching problem MUST implement this interface
 * in order to use the API. The convention one can use for choosing the class name of the
 * state can be a searching problem name followed by "State". For example:<br>
 * GameState.
 * </p>
 * 
 * This is a marker interface hence has no fields/methods.
 * @author repomaestro
 */
public class State implements Serializable {
    /**
     * Checks the equality between this object and another.
     * 
     * <p>
     * This method is provided by this class does a deep checking. That is,
     * two objects are equal if they represent identical structures in memory.
     * </p>
     * @param another object to check equality
     * @return true if objects are equal
     */
    @Override
    public boolean equals(Object another) {
            if (!(another instanceof State))
                return false;
            
            
            byte[] thisData = serialize(this);
            byte[] otherData = serialize(another);
            
            if (thisData == null || otherData == null)
                return false;
            else {
                return Arrays.equals(thisData, otherData);
            }
    }
    
    
    @Override
    public int hashCode() {
        byte[] thisData = serialize(this);
        int hash = 1;
        for (int i = 0; i < thisData.length; i++) {
            if (thisData[i] == 0)
                hash += 7 * i;
            else 
                hash *= thisData[i];
        }
        return hash;
    }
    
    private byte[] serialize(Object o) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);
            objectOut.writeObject(o);
            return byteOut.toByteArray();
        } catch (IOException ignore) {} 
        
        return null;
    }
}
