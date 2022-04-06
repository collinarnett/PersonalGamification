
object writer{
    def createNeededDir()={
        if(!os.exits(wd /"taskInProgress"))
            os.makeDir(wd/ "taskInProgress")
    }
}