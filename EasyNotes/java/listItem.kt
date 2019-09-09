package com.example.memo

class listItem{
    var id: String = ""
    var title: String = ""
    var details: String = ""
    var dateTime: String = ""

    constructor(id:String, title:String, details: String, dateTime: String){
        this.id=id
        this.title=title
        this.details=details
        this.dateTime=dateTime
    }
}