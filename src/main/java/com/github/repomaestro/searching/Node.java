/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.repomaestro.searching;

import java.util.Arrays;
import java.io.*;

/**
 * This class represents a node of a tree.
 * A node has a State, problem-dependent state of a search tree node.
 * 
 * <p>And several implicit fields such as:</p>
 * <ul>
 * <li>Parent, Node that represents the parent of this node in a search tree.</li>
 * <li>Move, the move that resulted in this Node being the current state of a search.</li>
 * <li>Cost, the cost in order to reach this node.</li>
 * <li>Depth, depth of the this node.</li>
 * </ul>
 * 
 * <p>
 * User of this class (classes outside this package) can only use, but not create an instance of this class.
 * Since this class is immutable as well, the user can only query the information within an instance
 * with its accesssor methods.
 * </p>
 * 
 * @param <T> type of the state, must be a sub-type of {@link State}
 * @author repomaestro
 */
public final class Node<T extends State> implements Comparable<Node<T>> {
    private final Node<T> parent;
    private final Move<T> move;
    private final int cost;
    private final int depth;
    
    private T state;
    
    Node(T state) {
        this(state, null, null, 0, 0);
    }
    
    Node(T state, Node parent, Move<T> move, int cost, int depth) {
        this.state = state;
        this.parent = parent;
        this.move = move;
        this.cost = cost;
        this.depth = depth;
    }
    
    /**
     * Returns the parent Node of this Node.
     * @return parent Node of this Node in the search problem
     */
    public Node<T> getParent() {
        return this.parent;
    }
    
    /**
     * @return the Move that resulted this Node by applying it to the parent Node
     */
    public Move<T> getMove() {
        return this.move;
    }
    
    /**
     * Returns the total cost from root Node to this Node.<br>
     * Note that this is cost also includes any heuristics applied.<br>
     * That is, for each Move m from parent Node to this Node, the sum of all costs of
     * m is lesser equal to total cost.
     * @return total cost from root Node to this Node 
     */
    public int getCost() {
        return this.cost;
    }
    
    /**
     * Returns the depth (number of nodes from this to root) of this node.
     * @return the depth of this Node in the Search Tree
     */
    public int getDepth() {
        return this.depth;
    }
    
    /**
     * @return the state of this Node
     */
    public T getState() {
        return this.state;
    }
    
    @Override
    public int compareTo(Node<T> another) {
        return this.cost - another.cost;
    } 
    
    @Override
    public String toString() {
        return state.toString();
    }
}
