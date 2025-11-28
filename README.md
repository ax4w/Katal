# Katal
![img.png](img.png)
an interpreted concatenative programming language with a global stack written in Kotlin.

## Docs

For examples look into `examples`, everything is covered from basic stuff to recursion / `repeatN`

### lib
#### std

- `dup`: Duplicate the item on top of the stack
- `swap`: Swap the two items on top of the stack
- `showStack`: Prints the stack (debugging)
- `clearStack`: Clears the stack
- `drop`: Drop the top most element
- `call`: Calls the function / compound object on top of the stack
- `print` / `println` - Print stuff
- `repeatN` : Repeats the function / compound object (top-1 element on the stack) n-times (top element on the stack)
- `load`: Loads a `.katal`-File from the path
- `nop` : Is just chillin and doing nothing

#### logic
- `and` / `or` / `not` : Your basic logic operators
- `le`: Is just `less than` ( < )
- `ge`: Is just `greater than` ( > )
- `cond`: Basically `if`, the top most element on the stack is the condition, the top-1 element is the true branch, the top-2 element is the false branch

#### math
- `add` / `sub` / `div` / `mul` / `mod` - Your basic math function
- `saveDiv` - Div but when dividing by 0 returns Nothing, else result wrapped in something
#### Optional
- `something`: Wrap a value in Something
- `nothing`: Just create a Nothing
- `unwrap` : Unbox the value from withing Something, errors if called on Nothing
- `unwrapOr`: Same as unwrap but with default value for Nothing
- 