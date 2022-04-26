case class Monster(
    name: String,
    description: String,
    toughness: Int
):
    def string: String =
      s"""------ Monster -------
      |name  : $name
      |description : $description
      |toughness : $toughness
      |""".stripMargin
