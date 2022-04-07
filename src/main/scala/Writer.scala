object writer{
    def createNeededDir()={
        if(!(os.exists(os.pwd / "taskInProgress")))
            os.makeDir(os.pwd / "taskInProgress")
    }
    def seralize(obj: Any)={
        
    }
}