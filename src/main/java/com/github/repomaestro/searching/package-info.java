/**
 * 
 * Provides API that can be used for solving standard search problems.
 * <p>
 * 
 * A standard search problem can be defined as:
 * 
 * <ul>
 * <li>Initial State (or start state).</li>
 * <li>Objective functions and their associated costs.</li>
 * <li>The goal state.</li>
 * <li>Optional set of heuristic functions</li>
 * </ul>
 * 
 * <p>
 * A search problem defined according to these definitions can be solved in various
 * standard methods which have their ups and downs with relative to one another.
 * </p>
 *
 * <p>
 * The main objective of this API is to decouple the problem definition from
 * the methods used to solve it. It provides the core API class {@link TreeEngine}
 * and other classes in order to use this API class. Note that in order to
 * use this API as effective as possible, one must at least know the basics of
 * methods used to solve search/path problems
 * </p>
 * 
 * <p>
 * In order to use this API, one must use the classes and interfaces provided by 
 * this package and define the search problem. Then instantiate a {@code TreeEngine}
 * and invoke its corresponding methods to get the solution to the problem.
 * </p>
 * 
 * General steps involved in defining the problem with this API consists of:
 * 
 * <ol>
 * 
 * <li>
 * Defining an immutable class which extends the State class. This class is pretty much the representation
 * of the state. It contains separate fields which are sub-state of this state. Environmental states
 * (which does not change and give information about the environment) can be defined as class fields
 * (such as dimensions of a 2D problem).<br> 
 * If the state contains data types which are object references (e.g array), then you can also
 * implement {@code Object.equals} and {@code Object.hashCode}, but this is not a necessity.<br>
 * <i>
 * Note: If the defined class does not override {@code Object.equals} and {@code Object.hashCode}, then
 * default implementations defined in {@code State} class are used, which should work for any general 
 * sub-class but a little less performant than a custom implementation.
 * </i>
 * </li>
 * 
 * <li>
 * Creating the initial/start state object using the previously defined State sub-class in the first step.
 * </li>
 * 
 * <li>
 * Creating instances of a generic type {@link Move} which are successor functions
 * and adding them to a list. This involves giving it a name, cost, and an operation which
 * is a UnaryOperator.
 * </li>
 * 
 * <li>
 * Defining condition for the goal state with a generic type {@code Predicate}. It is a <i>condition</i> rather than
 * a <i>state</i> for added flexibility in defining the target/goal condition.
 * </li>
 * 
 * <li>
 * (Optional)<br>
 * Defining heuristic functions used for heuristic algorithms. This is optional step.
 * </li>
 * 
 * </ol>
 * 
 * <p>
 * After the definition of the problem as above, one must create a {@code TreeEngine} instance
 * and giving it the initial state and a list of successor functions. Then call its various versions
 * of {@code TreeEngine.pathTo} method by giving it the target condition (predicate that is defined above) and other optional arguments.
 * </p>
 * 
 * <p>
 * Here is an example of using this API for solving a 2 dimensional search problem where
 * the objective is to get a path from one point in 2D space to another one:
 * </p>
 * 
 * <pre>
 * {@code
        //Definition of an immutable class that represents state of the search problem.
        class GameState extends State {
            //Environmental state (unchanged) as class field.
            public static Dimension dim = new Dimension(10, 10);

            public final int x;
            public final int y;
            
            public GameState(int x, int y) {
                this.x = x;
                this.y = y;
            }
            
            @Override
            public String toString() {
                return String.format("{%d, %d}", x, y);
            }
        }
        
        //The Start/initial state of a search problem.
        GameState initialState = new GameState(0, 0);
        
        //The goals state of the search problem.
        GameState goalState = new GameState(5, 5);
        
        //Successor functions as Move instances.
        //There are four moves in this 2D problem which are going left, right, up, and down.
        Move<GameState> mLeft = new Move<>("LEFT", (gs) -> {
                if (gs.x <= 0)
                    return null;
                
                GameState nextState = new GameState(gs.x - 1, gs.y);
                return nextState;
            }, 1);
        
        Move<GameState> mRight = new Move<>("RIGHT", (gs) -> {
                if (gs.x >= GameState.dim.width - 1)
                    return null;
                
                GameState nextState = new GameState(gs.x + 1, gs.y);
                return nextState;
            }, 1);
                
        Move<GameState> mUp = new Move<>("UP", (gs) -> {
                if (gs.y <= 0)
                    return null;
                
                GameState nextState = new GameState(gs.x, gs.y - 1);
                return nextState;
            }, 1);
        
        Move<GameState> mDown = new Move<>("DOWN", (gs) -> {
                if (gs.y >= GameState.dim.height - 1)
                    return null;
                
                GameState nextState = new GameState(gs.x, gs.y + 1);
                return nextState;
            }, 1);
        
        
        //List of moves (successor functions).
        List<Move<GameState>> moveList = Arrays.asList(mLeft, mRight, mUp, mDown);
                
        //(Optional) Heuristic function.
        ToIntFunction<GameState> heuristics = (gs) -> {
            int xDist = Math.abs(gs.x - goalState.x);
            int yDist = Math.abs(gs.y - goalState.y);
            return xDist + yDist;
        };
        
        //Evaluator which test if present state is the goal.
        //Here it just checks the equality of the present is 'equal' to the goal state but it can be any condition.
        Predicate<GameState> evaluator = (gs) -> gs.equals(goalState);
        
        //TreeEngine instance that is initialized with initial state and successor functions that will be used to solve this problem.
        TreeEngine<GameState> te = new TreeEngine<>(initialState, moveList);
        
        //Getting an immutable path from initial state to the goal with condition, algorithm to be used, max depth and heuristics.
        List<Node<GameState>> path = te.pathTo(evaluator, HeuristicAlgorithm.A_STAR, 1000, heuristics);
        
        //Prints each state in path to standard output.
        path.stream().forEach((n) -> System.out.println(n.getState()));  
 * }
 * </pre>
 * 
 * @see TreeEngine
 */
package com.github.repomaestro.searching;
