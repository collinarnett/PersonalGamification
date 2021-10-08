# TODO

## Ideas

Should syntax emulate games?:

```scala
class Player(...) =
  def kill(monster: Monster)
    items = monster.dies
    this.items += items
class Monster(items:Seq[Item]...) =
  def dies()
    this.items
class Item(...) =
...
Player kill Dragon
```
