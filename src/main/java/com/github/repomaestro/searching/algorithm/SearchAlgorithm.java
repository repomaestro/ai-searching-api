/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.repomaestro.searching.algorithm;

/**
 *
 * Constant instances of this enum class represents the UNINFORMED search algorithm.
 * @author repomaestro
 */
public enum SearchAlgorithm implements Algorithm {
    //The Depth First Search algorithm
    DFS, 
    //The Breadth First Search algorithm
    BFS,
    //The Incremental Depth Search algorithm
    IDS,
    //The Uniform Cost algorithm
    UCS;
}
