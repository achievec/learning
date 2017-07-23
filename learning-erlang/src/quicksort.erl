-module(quicksort)

%% 快速排序
quicksort([]) -> [];
quicksort([Pivot|T]) ->
    quicksort([X || X <- T,X < Pivot])
    ++ [Pivot] ++
    quicksort([X || X <- T],X >= Pivot).

%% 计算所有的杨辉三角
pythag(N) -> [{A,B,C} ||
        A <- lists:seq(1,N),
        B <- lists:seq(1,N),
        C <- lists:seq(1,N),
        A+B+C =< N,
        A*A + B*B =:= C*C
    ].

%% 回文构词
perms([]) -> [[]];
perms(L) -> [[H|T] || H<-L,T <-perms[L--[H]]].



