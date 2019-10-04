fun eachFileRecurse(file: File, action: (File) -> Unit) {
    if(file.isDirectory) file.listFiles()?.forEach {
        eachFileRecurse(it, action)
    } else action(file)
}

fun eachFile(file: File, action: (File) -> Unit) {
    if(file.isDirectory) file.listFiles()?.forEach(action)
}

extra.apply {
    set("eachFileRecurse", ::eachFileRecurse)
    set("eachFile", ::eachFile)
}