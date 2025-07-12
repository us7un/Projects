% ustun yilmaz
% 2023400108
% compiling: yes
% complete: yes


:- ['cmpefarm.pro'].
:- init_from_map.




% 1. agents_distance(+Agent1, +Agent2, -Distance)
agents_distance(Agent1, Agent2, Distance) :-
    % Get the X and Y coordinates of both agents
    get_dict(x, Agent1, X1),
    get_dict(y, Agent1, Y1),
    get_dict(x, Agent2, X2),
    get_dict(y, Agent2, Y2),
    % Compute the Manhattan Distance with the given formula on the outline
    Dx is X2 - X1,  AbsDx is abs(Dx),
    Dy is Y2 - Y1,  AbsDy is abs(Dy),
    Distance is AbsDx + AbsDy.

% 2. number_of_agents(+State, -NumberOfAgents)
number_of_agents([Agents, _], NumberOfAgents) :-
    % The aggregate_all/3 rule counts how many times Prolog proves get_dict(...), giving the number of agents
    aggregate_all(count, (get_dict(_, Agents, _Dict)), NumberOfAgents).

% 3. value_of_farm(+State, -Value)
value_of_farm([Agents, Objects], Value) :-
    total_value(Agents, SumAgents),
    total_value(Objects, SumObjects),
    Value is SumAgents + SumObjects.

total_value(Dict, Sum) :-
    % We use aggregate_all/3 again, with the counter being weighted by value of each subtype encountered.
    aggregate_all(sum(Val), (get_dict(_, Dict, Item), get_dict(subtype, Item, Subtype), value(Subtype, Val)), Sum).

% 4. find_food_coordinates(+State, +AgentId, -Coordinates)
find_food_coordinates([Agents, Objects], AgentId, Coordinates) :-
    % Affirm the subtype of the agent
    get_dict(AgentId, Agents, Agent),
    get_dict(subtype, Agent, Subtype),
    get_dict(type, Agent, Type),
    % Herbivore case
    (Type = herbivore ->
        % Get coordinates of all objects a herbivore can eat.
        findall([Ox, Oy],
          (get_dict(_ObjectId, Objects, Object), get_dict(subtype, Object, ObjectSubtype), can_eat(Subtype, ObjectSubtype), get_dict(x, Object, Ox), get_dict(y, Object, Oy)),
          Coordinates);
      Type = carnivore ->
        % Get coordinates of all agents a carnivore can eat
        findall([Px, Py],
          (get_dict(_PreyId, Agents, Prey), get_dict(subtype, Prey, PreySubtype), can_eat(Subtype, PreySubtype), get_dict(x, Prey, Px), get_dict(y, Prey, Py)),
          Coordinates)), Coordinates \= [].  % Fail if no food


% 5. find_nearest_agent(+State, +AgentId, -Coordinates, -NearestAgent)
find_nearest_agent([Agents,_], AgentId, [Ax, Ay], NearestAgent) :-
    get_dict(AgentId, Agents, Agent),
    % Get the agent with the minimum distance from the map
    findall(Distance-OtherId, (get_dict(OtherId, Agents, OtherAgent), OtherId \= AgentId, agents_distance(Agent, OtherAgent, Distance)),
    DistanceIdTuples),
    % False if no other agent found
    DistanceIdTuples \= [],
    % Form a list of all distances and find the minimum of that list
    findall(Distance, member(Distance-_, DistanceIdTuples), Distances),
    min_list(Distances, MinDistance),
    % Get "the" or one of the closest agents
    member(MinDistance-NearestId, DistanceIdTuples),
    get_dict(NearestId, Agents, NearestAgent),
    % Get its coordinates
    get_dict(x, NearestAgent, Ax),
    get_dict(y, NearestAgent, Ay).

% 6. find_nearest_food(+State, +AgentId, -Coordinates, -FoodType, -Distance)
find_nearest_food([Agents, Objects], AgentId, [Fx, Fy], FoodType, Distance) :-
    % Get the agent's details
    get_dict(AgentId, Agents, Agent),
    get_dict(type,    Agent, Type),
    get_dict(subtype, Agent, Subtype),
    % Herbivore case
    (Type = herbivore ->
        % Get the minimum triple of distance-coordinates-foodtype using findall+keysort
        findall(D-[Fx, Fy, FT],
        (get_dict(_, Objects, Object), get_dict(subtype, Object, FT), can_eat(Subtype, FT),
        get_dict(x, Object, Fx), get_dict(y, Object, Fy), get_dict(x, Agent, Ax), get_dict(y, Agent, Ay), 
        DX is abs(Ax - Fx), DY is abs(Ay - Fy), D is DX + DY),
        Pairs), Pairs \= [],
        keysort(Pairs, [Distance-[Fx, Fy, FoodType] | _])
    % Carnivore case
    ; Type = carnivore ->
        % Get the minimum triple of distance-coordinates-foodtype using findall+keysort
        findall(D-[Fx, Fy, FT],
        (get_dict(OtherId, Agents, Other), get_dict(subtype, Other, FT), can_eat(Subtype, FT),
        get_dict(x, Other, Fx), get_dict(y, Other, Fy), get_dict(x, Agent, Ax), get_dict(y, Agent, Ay),
        DX is abs(Ax - Fx), DY is abs(Ay - Fy), D is DX + DY),
        Pairs), Pairs \= [],
        keysort(Pairs, [Distance-[Fx, Fy, FoodType] | _])).


% Define all types of movements utilizing the delta method we are given
delta(move_up, 0, -1).
delta(move_down, 0, 1).
delta(move_left, -1, 0).
delta(move_right, 1, 0).
delta(move_up_right, 1, -1).
delta(move_up_left, -1, -1).
delta(move_down_right, 1, 1).
delta(move_down_left, -1, 1).

% valid_move(+AgentsDict, +AgentId, +Subtype, +X, +Y, +Action, -Nx, -Ny)
% This method checks if the movement made by an agent is valid or not
valid_move(Agents, AgentId, Subtype, X, Y, Action, Nx, Ny) :-
    % Check which move is to be made and if that place is occupied by another agent
    get_dict(AgentId, Agents, Agent),
    get_dict(type, Agent, Type),
    can_move(Subtype, Action),
    delta(Action, Dx, Dy),
    Nx is X + Dx,
    Ny is Y + Dy,
    \+ is_occupied(Type, AgentId, Nx, Ny, Agents).

% move_to_coordinate(+State, +AgentId, +X, +Y, -ActionList, +DepthLimit)
move_to_coordinate([Agents,_], AgentId, X, Y, ActionList, DepthLimit) :-
    % Get details of agent and do breadth-first search
    get_dict(AgentId, Agents, Agent),
    get_dict(subtype, Agent, Subtype),
    get_dict(x, Agent, Ax),
    get_dict(y, Agent, Ay),
    bfs([[[Ax, Ay],[]]], Agents, AgentId, Subtype, [X, Y], [], ActionList, DepthLimit).

% bfs(+Queue, +Agents, +AgentId, +Subtype, +Target, +Visited, -Actions, +DepthLimit)
% This is a method that does breadth-first search for the most optimal route to the given destination, in the depth limit.
% I used StackOverflow to learn how to implement a breadth-first search in Prolog.
% No solution case
bfs([], _, _, _, _, _, _, _) :- fail.

% Found the solution case
bfs([[Position, Actions] | _], _, _, _, Position, _, Actions, _) :- !. 

bfs([[[Currentx, Currenty], ActionsSoFar]|RemainingQueue], Agents, AgentId, Subtype, Target, Visited, ActionList, DepthLimit) :-
    % Search until the depth limit is exceeded
    length(ActionsSoFar, Depth),
    % We iterate at most DepthLimit times
    Depth < DepthLimit,
    % Accumulate all new coordinates with their corresponding actions and next states
    findall([[Nx, Ny], NewActions],
        (valid_move(Agents, AgentId, Subtype, Currentx, Currenty, Acts, Nx, Ny), 
        % We not revisit a coordinate, ensuring no infinite loop
        \+ member([Nx, Ny], Visited),
        \+ member([[Nx, Ny], _], RemainingQueue),
        append(ActionsSoFar, [Acts], NewActions)
        ),
        NextStates),
    % Concatenate the remaining part of the BFS queue and pass it again, i.e. iterate.
    append(RemainingQueue, NextStates, UpdatedQueue),
    bfs(UpdatedQueue, Agents, AgentId, Subtype, Target, [[Currentx, Currenty]|Visited], ActionList, DepthLimit).


% 8. move_to_nearest_food(+State, +AgentId, -ActionList, +DepthLimit)
move_to_nearest_food(State, AgentId, ActionList, DepthLimit) :-
    % Simply look for the nearest food and move to its coordinate.
    find_nearest_food(State, AgentId, [Fx, Fy], _, _),
    move_to_coordinate(State, AgentId, Fx, Fy, ActionList, DepthLimit).


% 9. consume_all(+State, +AgentId, -NumberOfMovements, -Value, -NumberOfChildren, +DepthLimit)
consume_all(State, AgentId, NumberOfMovements, Value, NumberOfChildren, DepthLimit) :-
    % If the loop is done
    consume_all_loop(State, AgentId, DepthLimit, 0, FinalState, NumberOfMovements),
    % Compute the resulting metrics and return them
    value_of_farm(FinalState, Value),
    FinalState = [FinalAgents, _],
    get_dict(AgentId, FinalAgents, FinalAgentDict),
    get_dict(children, FinalAgentDict, NumberOfChildren), !.

% consume_all_loop(+State, +AgentId, +DepthLimit, +AccMoves, -FinalState, -TotalMoves)
% This is the main loop where our agent keeps consuming the food until none are move_left
consume_all_loop(State, AgentId, DepthLimit, AccMoves, FinalState, TotalMoves) :-
    State = [Agents, Objects], % We prefer a different approach here rather than asserting state directly as [Agents, Objects]
    % Get details of agent
    get_dict(AgentId, Agents, Agent),
    get_dict(subtype, Agent, Subtype),
    get_dict(type, Agent, Type),
    get_dict(x, Agent, Ax),
    get_dict(y, Agent, Ay),
    % Herbivore case
    (Type = herbivore ->
        % Get all food coordinates, similar logic as above
        findall(D-[Fx, Fy],
        (get_dict(_, Objects, Object), get_dict(subtype, Object, FoodType), can_eat(Subtype, FoodType),
        get_dict(x, Object, Fx), get_dict(y, Object, Fy),
        Dx is abs(Ax - Fx), Dy is abs(Ay - Fy), D is Dx + Dy),
        Tuples)
    % Carnivore case
    ;Type = carnivore ->
        % Get all food coordinates, similar logic as above
        findall(D-[Fx, Fy],
        (get_dict(OtherId, Agents, Other), get_dict(subtype, Other, FoodType), can_eat(Subtype, FoodType),
        get_dict(x, Other, Fx), get_dict(y, Other, Fy),
        Dx is abs(Ax - Fx), Dy is abs(Ay - Fy), D is Dx + Dy),
        Tuples)),
    % No food remaining case
    (Tuples = [] ->
        FinalState = State,
        TotalMoves = AccMoves;
        % Sort all food by distance and attempt to consume them all one by one
        keysort(Tuples, Sorted),
        consume(Sorted, State, AgentId, DepthLimit, AccMoves, NewState, NewAcc, Found),
        % If we successfully consumed the food, continue consuming the remaining ones
        (Found == true ->
            consume_all_loop(NewState, AgentId, DepthLimit, NewAcc, FinalState, TotalMoves);
            FinalState = State,
            TotalMoves = AccMoves)).

% consume(+SortedTuples, +State, +AgentId, +DepthLimit, +AccMoves, -ResultState, -ResultAccMoves, -Found)
% Will try to consume the food and return true or false depending on its success
% No food case
consume([], State, _AgentId, _DepthLimit, Acc, State, Acc, false).
% Else try to consume each food that is reachable
consume([_D-[Fx, Fy]|Rest], State, AgentId, DepthLimit, AccMoves, ResultState, ResultAcc, Found) :-
    % Reachability determined by move_to_coordinate/6
    (move_to_coordinate(State, AgentId, Fx, Fy, ActionList, DepthLimit) ->
        length(ActionList, Steps),
        make_series_of_actions(ActionList, State, AgentId, MovedState),
        eat(MovedState, AgentId, EatenState),
        % Accumulate the steps taken and update the final state
        ResultAcc is AccMoves + Steps,
        ResultState = EatenState,
        Found = true;
        % Continue with consuming the others
        consume(Rest, State, AgentId, DepthLimit, AccMoves, ResultState, ResultAcc, Found)).


