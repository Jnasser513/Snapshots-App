package com.nasser.snapshots.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Snapshots(
    var id: String = "",
    var tittle: String = "",
    var photoUrl: String = "",
    var likeList: Map<String, Boolean> = mutableMapOf()
)
