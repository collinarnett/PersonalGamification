import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.databind.json.JsonMapper
import java.io.File

@main def completeQuests(task: String) = {
  var me = Player(name = "Trexd")
  val mapper = JsonMapper.builder().addModule(DefaultScalaModule).build()
  // me = mapper.readValue(new File("data.json"), Player.class)
}
