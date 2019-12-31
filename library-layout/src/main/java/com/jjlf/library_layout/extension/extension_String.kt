package com.jjlf.library_layout.extension


fun String.makeKeywords() : ArrayList<String> {
    val arrayString = ArrayList<String>()
    if(this.isEmpty()) return arrayString
    var curWord = ""
    this.split("").forEach {
        if(it.isNotEmpty()){
            curWord += it
            arrayString.add(curWord)
        }
    }
    return arrayString
}

fun String.makeABunchKeywords(): ArrayList<String>{
    val arrayString = ArrayList<String>()
    if(this.isEmpty()) return arrayString
    val spW = this.split(" ").filter {
        it.isNotEmpty() && it != " "
    }
    var word: String
    for (num in spW.indices){
        word = spW[num]
        for(i in spW.indices ){
            if(i > num){
                word += " ${spW[i]}"
            }
        }
        arrayString.addAll(word.makeKeywords())
    }
    if(spW.size > 1){
        word = spW.last()
        spW.forEachIndexed{ index, s ->
            if(index != spW.size-1){
                word += " $s"
            }
        }
        arrayString.addAll(word.makeKeywords())
    }

    return arrayString
}

//emoticons string
fun String.parseUnicode16(): String {
    return String(Character.toChars(Integer.parseInt(this, 16)))
}


fun String.removeBlankLines():String{
    return this.replace(Regex("(?m)^\\s+\$"), "")
}
fun String.removeNewLines():String{
    return this.replace("\n"," ")
}

fun String.removeDuplicateWhiteSpaces():String{
    return this.replace(Regex("\\s+"), " ")
}

