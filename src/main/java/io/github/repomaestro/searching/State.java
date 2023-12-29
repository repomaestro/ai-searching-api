/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.repomaestro.searching;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * <p>
 * This class is the super-class of a state in a searching problem.<br>
 * The class that represent a state in a searching problem MUST extend this class
 * in order to use the API. The convention one can use for choosing the class name of the
 * state can be a searching problem name followed by "State". For example:<br>
 * GameState.
 * </p>
 * 
 * <p>
 * This class overrides some important {@code Object} methods which other classes
 * of this API use effectively.<br>
 * Sub-class (class that represents the state of a searching problem), can, but is
 * not required to override these methods. Even though doing so would improve the
 * performance
 * </p>
 * 
 * <p>
 * <i>
 * <b>Important</b>: Fields/attributes of sub-classes of this class (that is, class
 * that represents the state) MUST be of a type that implements {@link java.io.Serializable}, or
 * more accurately, MUST be serializable. This shouldn't be a concern however, as most of the 
 * Java supplied types that would be typically used as a field/attribute of a state are 
 * declared as being Serializable.
 * </i>
 * </p>
 * @author repomaestro
 */
public class State implements Serializable {
    /**
     * Checks the equality between this object and another.
     * 
     * <p>
     * This method is provided by this class does a deep checking. That is,
     * two objects are equal if they represent identical structures in heap/memory.
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

        return Arrays.equals(thisData, otherData);
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
        } catch (NotSerializableException e) {
            throw new RuntimeException(String.format(
                    "%s is not serializable (make sure its fields are serializable).", 
                    this.getClass()),
                    e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
