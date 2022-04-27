case class Monster(
    name: String,
    description: String,
    toughness: Int
):
  override def toString(): String =
    s"""" ᕙ༼ ,,ԾܫԾ,, ༽ᕗ
        --Monster--
        | Name :  $name
        | description : $description
        | toughness : $toughness
        ----------------------
        """
