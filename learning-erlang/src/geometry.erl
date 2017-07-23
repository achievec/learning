-module(geometry).
-export([area/1]).

area({rectangel,Width,Height}) -> Width*Height;
area({square,Side}) -> Side*Side.