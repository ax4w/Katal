# Katal
![img.png](img.png)
an interpreted concatenative programming language with a global stack written in Kotlin.

## Docs

For examples look into `examples`, everything is covered from basic stuff to recursion / `repeatN`

You don't need to specify every parameter, you can leave the first N empty and the values are taken from the stack

### lib
#### std

- `dup`: Duplicate the item on top of the stack
- `swap`: Swap the two items on top of the stack
- `showStack`: Prints the stack (debugging)
- `clearStack`: Clears the stack
- `drop`: Drop the top most element
- `[<fn>] call`: Calls the function / compound object on top of the stack
- `<value> print` / `<value> println` - Print stuff
- `<amount> [<fn>] repeatN` : Repeats the function / compound object (top-1 element on the stack) n-times (top element on the stack)
- `<path> load`: Loads a `.katal`-File from the path
- `nop` : Is just chillin and doing nothing
- `<elements ...> <amount> arrayOf`: Takes top n elements of stack and merges them into an array
- `<array> [<fn>] map`: takes array and compound objects and maps compound object over the array, creating a new array
- `<array> [<fn>] forEach`: takes array and compound objects and calls compound object on each element
- `<array> <index> get`: Get a value from an array at an index
- `<array> <index> <value> set`: Set a value in an array at an index

#### logic
- `and` / `or` / `not` : Your basic logic operators
- `le`: Is just `less than` ( < )
- `ge`: Is just `greater than` ( > )
- `cond`: Basically `if`, the top most element on the stack is the condition, the top-1 element is the true branch, the top-2 element is the false branch

#### math
- `<num> <num> add` / `<num> <num> sub` / `<num> <num> div` / `<num> <num> mul` / ` <num> <num> mod` - Your basic math function
- `<num> <num> saveDiv` - Div but when dividing by 0 returns Nothing, else result wrapped in something
#### Optional
- `<value> something`: Wrap a value in Something
- `<value> nothing`: Just create a Nothing
- `<option> unwrap` : Unbox the value from withing Something, errors if called on Nothing
- `<option> <value> unwrapOr`: Same as unwrap but with default value for Nothing
- 