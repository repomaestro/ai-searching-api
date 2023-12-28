/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.repomaestro.searching;

import java.util.function.*;

/**
 *
 * <p>
 * This record represents a Move which is objective function of a search problem.
 * </p> 
 * A move is characterized by:
 * <ul>
 * <li>String, move name,</li> 
 * <li>UnaryOperator, the objective function,</li>
 * <li>int, cost of the move.</li>
 * </ul>
 * 
 * 
 * @param <T> type of state, must be a sub-type of {@link State}
 * @author repomaestro
 */
public record Move<T extends State>(String moveName, UnaryOperator<T> objectiveFunction, int cost) {}
