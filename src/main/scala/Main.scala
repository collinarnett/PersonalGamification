@main def main(task: String, filename: String) = 
  var me = Player(task)
  me.save(filename)
