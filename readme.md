## Number guessing game

#### 1. Starting a new game:

Method: 
```
Post
```
URI: 
```
/api/guessing/games
```

Example request body:

```
{
	"maxNumberOfRounds": 5,
	"difficulty": "defult"
}
```

Example response body:

```
{
    "gameId": 3,
    "rangeFrom": 1,
    "rangeTo": 20
}
```

#### 2. Playing a round:

Method: 

```
Post
```

URI: 

```
/api/guessing/rounds
```

Example request body:

```
{
	"gameId":1,
	"trialNumber":1
}
```

Example response body:

```
{
    "roundId": 5,
    "remainingRounds": 3,
    "message": "The number is too low!"
}
```

