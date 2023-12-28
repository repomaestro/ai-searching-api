/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.repomaestro.searching;

import io.github.repomaestro.searching.algorithm.*;
import java.util.*;
import java.util.function.*;

/**
 * This class represents a TreeEngine whose instances are capable of solving
 * search algorithms.
 * 
 * <p>
 * A TreeEngine is initialized with a {@code State}, the initial State and a list of {@code Move}'s<br>
 * Here, this move list basically represents the successor function(s) of the problem.
 * </p>
 * 
 * <p>
 * After initialization, instance method {@code TreeEngine.pathTo} can be called, which
 * returns an ordered list of {@code Node}'s.
 * </p>
 * 
 * @param <T> type of state, must be a sub-type of {@link State}
 * @author repomaestro
 */
public class TreeEngine<T extends State> {
    /**
     * Fringe class represents a "search fringe", pretty much a data structure
     * whose internal definition changes based on the search algorithm.
     * 
     * As per the definition of the Fringe in searching, the behavior of the instances
     * of this class also changes based on the supplied Algorithm enum constant A.
     * 
     * This class is private to the {@code TreeEngine} class and used only within the operations
     * defined in the class.
     */
    private static class Fringe<T extends State> {
        private final Algorithm A;
        private Stack<Node<T>> stack;
        private Queue<Node<T>> queue;
        
        public Fringe(Algorithm A) {
            this.A = A;
            
            if (A == SearchAlgorithm.DFS || A == SearchAlgorithm.IDS)
                stack = new Stack<>();
            else if (A == SearchAlgorithm.BFS)
                queue = new ArrayDeque<>();
            else if (A == SearchAlgorithm.UCS || A == HeuristicAlgorithm.A_STAR || A == HeuristicAlgorithm.GS)
                queue = new PriorityQueue<>();
        }
        
        public void push(Node<T> node) {
            if (A == SearchAlgorithm.DFS || A == SearchAlgorithm.IDS)
                stack.push(node);
            else if (A == SearchAlgorithm.UCS || A == SearchAlgorithm.BFS || A == HeuristicAlgorithm.A_STAR || A == HeuristicAlgorithm.GS)
                queue.add(node);
        }
        
        public Node<T> pop() {
            if (A == SearchAlgorithm.DFS || A == SearchAlgorithm.IDS)
                return stack.pop();
            else if (A == SearchAlgorithm.UCS || A == SearchAlgorithm.BFS || A == HeuristicAlgorithm.A_STAR || A == HeuristicAlgorithm.GS)
                return queue.remove();
            else
                return null;
        }
        
        public boolean isEmpty() {
            if (A == SearchAlgorithm.DFS || A == SearchAlgorithm.IDS)
                return stack.isEmpty();
            else if (A == SearchAlgorithm.UCS || A == SearchAlgorithm.BFS || A == HeuristicAlgorithm.A_STAR || A == HeuristicAlgorithm.GS)
                return queue.isEmpty();
            else
                return false;
        }
    }
    
    //Initial node of the tree.
    private Node<T> initialNode;
    
    //Move List of the problem.
    private List<Move<T>> moveList;
    
    /**
     * Constructs this TreeEngine with an initial state and list of Moves
     * @param initialState the state which is the start/initial state of a search problem,
     * must be an instance of State sub-type
     * @param moveList a list of Moves, which are the objective functions
     */
    public TreeEngine(T initialState, List<Move<T>> moveList) {
        this.initialNode = new Node<>(initialState);
        this.moveList = new ArrayList<>(moveList);
    }
    
    /**
     * Reset the start state to <i>initialState</i>
     * @param initialState start state to reset this TreeEngine
     * @return this TreeEngine
     */
    public TreeEngine initial(T initialState) {
        this.initialNode = new Node(initialState);
        return this;
    }
    
    /**
     * Returns the start state.
     * @return start state of the search problem
     */
    public T initial() {
        return initialNode.getState();
    }
    
    /**
     * Resets the Move list with a new one.
     * After this method returns, previous moves are discarded and replaced with
     * the new ones.
     * @param newMoves a new list of Moves
     * @return this TreeEngine
     */
    public TreeEngine moves(List<Move<T>> newMoves) {
        this.moveList = new ArrayList<>(newMoves);
        return this;
    }
    
    /**
     * Returns current list of Moves
     * @return list of Moves
     */
    public List<Move<T>> moves() {
        return moveList;
    }
    
    /**
     * Tries to find a path from initial node (initial state) to the goal state as specified by the Predicate parameter, 
     * which is basically some kind of evaluator for the goal state (goal condition). Resulting path is returned
     * as an immutable List of Node's. If no solution exists (or no solution is found within given depth), then this
     * method returns empty list which is again, immutable.
     * 
     * <p>
     * There is only one Predicate passed, thus if more than one condition has to be met to be the goal
     * state, it must be specified in the evaluator itself (the implemented method of the sub-type).
     * </p>
     * The path finding algorithm is dependent on the Algorithm enum constant passed.<br>
     * Algorithm specified by A is run to the depth of at most maxDepth parameter.<br>
     * <p>
     * Optional heuristic function (as a ToIntFunction instance) can be passed to apply heuristic to the
     * move list functions. If the heuristic is passed even though the enum constant A is not type of HeuristicAlgorithm, 
     * then it is not considered and no heuristic will be applied.
     * </p>
     * This method takes heuristics as varargs argument (meaning you can pass arbitrary number of
     * such references) however, checking is also done here to ensure that only one is passed.
     * @param evaluator the predicate whose Predicate.test(State) will be called to check if the state is the goal state
     * @param A the search algorithm
     * @param maxDepth maximum depth to search for the solution
     * @param heuristics varargs heuristics whose length must be (n)one
     * @return immutable List of Nodes which is the path to the solution, empty List if no solution is found with given maximum depth
     */
    public List<Node<T>> pathTo(Predicate<T> evaluator, Algorithm A, int maxDepth, ToIntFunction<T>... heuristics) {
        if (!(A instanceof SearchAlgorithm || A instanceof HeuristicAlgorithm))
            throw new IllegalArgumentException(String.format("Algorithm \"%s\" is not applicable", A));
        
        if (A == SearchAlgorithm.IDS) {
            int depth = 1;
            List<Node<T>> path = Collections.EMPTY_LIST;
            while (path.equals(Collections.EMPTY_LIST) && depth <= maxDepth) {
                path = pathTo(evaluator, SearchAlgorithm.DFS, depth, heuristics);
                depth++;
            }
            
            return path;
        }
        
        if (heuristics.length > 1)
            throw new IllegalArgumentException("Number of heuristic functions (objects) that are passed cannot be larger than one!");
        
        //Initialize the heuristic function to be either none (if heuristics array is of zero-length) and first element if it is non zero-length (one as checked...).
        ToIntFunction<T> heuristic = (heuristics.length == 1 ? heuristics[0] : n -> 0);
                
        //The Fringe is of type Fringe which is an algorithm dependent data structure.
        Fringe<T> fringe = new Fringe(A);
        
        //Set for duplication prevention.
        Set<T> dupSet = new HashSet<>(); 
        
        //Push the root node (the given initial node) to the fringe.
        fringe.push(initialNode);
        while (!fringe.isEmpty()) {
            //Pop the element from the fringe.
            Node<T> currentNode = fringe.pop();
                
            /*
            Check if the finalState (which is last node of the path) conforms to the predicate (the goal test).
            If it is, construct a path from the last (leaf) node to the root node.
            If it is not, then check if the depth of the node is max depth, if so, do not expand it.
            Otherwise, expand the node.
            */
            if (evaluator.test(currentNode.getState()))
                return constructPath(currentNode);
            else if (currentNode.getDepth() >= maxDepth)
                continue;
                      
            /**
             * Now for each successor functions in the move set, call the successor function method for the given node and
             * construct new MovePath from the returned Move instance.
             * Note that successor function (or the move) MUST return null if no move is available for THAT move at THAT state of the node, in which case,
             * it is not added to the fringe as that indicates that it is leaf node.
             * 
             * Otherwise the newly constructed move is pushed to the fringe.
             */
            for (Move<T> successor : moveList) {
                T state = successor.objectiveFunction().apply(currentNode.getState());
                if (state == null || !dupSet.add(state)) 
                    continue;
                
                int cost = 0;
                if (A instanceof HeuristicAlgorithm) {
                    if (A == HeuristicAlgorithm.GS)
                        cost = heuristic.applyAsInt(state);
                    else if (A == HeuristicAlgorithm.A_STAR)
                        cost = successor.cost() + heuristic.applyAsInt(state);
                } else if (A instanceof SearchAlgorithm) {
                    cost = successor.cost();
                }
                    
                Node leafNode = new Node(state, currentNode, successor, cost, currentNode.getDepth() + 1);
                fringe.push(leafNode);
            }
        }
        
        return Collections.EMPTY_LIST;
    }
    
    /**
     * Constructs a path which is the solution to the search problem.
     * This method behaves the same way as if<br>
     * {@code TreeEngine.pathTo(Predicate<T>, Algorithm, int, ToIntFunction<T>...)}<br>
     * is called with max depth of {@code Integer.MAX_VALUE}.
     * @param evaluator the predicate whose Predicate.test(State) will be called to check if the state is the goal state
     * @param A the search algorithm
     * @param heuristics varargs heuristics whose length must be (n)one
     * @return immutable List of Nodes which is the path to the solution, empty List if no solution is found with given maximum depth
     */
    public List<Node<T>> pathTo(Predicate<T> evaluator, Algorithm A, ToIntFunction<T>... heuristics) {
        return pathTo(evaluator, A, Integer.MAX_VALUE, heuristics);
    }
    
    /**
     * Constructs a path which is the solution to the search problem.
     * This method behaves the same way as if<br>
     * {@code TreeEngine.pathTo(Predicate<T>, Algorithm, int, ToIntFunction<T>...)}<br>
     * is called with {@code HeuristicAlgorithm.A_STAR} as the algorithm and
     * the max depth of {@code Integer.MAX_VALUE}.
     * @param evaluator the predicate whose Predicate.test(State) will be called to check if the state is the goal state
     * @param heuristics varargs heuristics whose length must be (n)one
     * @return immutable List of Nodes which is the path to the solution, empty List if no solution is found with given maximum depth
     */
    public List<Node<T>> pathTo(Predicate<T> evaluator, ToIntFunction<T>... heuristics) {
        return pathTo(evaluator, HeuristicAlgorithm.A_STAR, Integer.MAX_VALUE,heuristics);
    }
    
    /**
     * Constructs path from given goal Node.
     * Returned path is a List of Nodes and is immutable.
     * @param node goal node
     * @return immutable List of Moves which represent the path to the solution
     */
    private List<Node<T>> constructPath(Node<T> node) {
        List<Node<T>> path = new ArrayList<>();
        while (node.getParent() != null) {
            path.add(node);
            node = node.getParent();
        }
        path.add(node);
        
        Collections.reverse(path);
        return Collections.unmodifiableList(path);
    }
    
}
