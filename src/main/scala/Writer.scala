import better.files._
import better.files.File._

object writer{
    def createNeededDir()={
        val file1:File="./TasksInProgress"
        .toFile
        .createIfNotExists()
        var file2: File="./CompletedTasks"
        .toFile
        .createIfNotExists()
    }
}