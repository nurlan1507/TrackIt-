package com.nurlan1507.trackit.data

import android.graphics.Color
import com.nurlan1507.trackit.R

enum class ProjectBoardBackgrounds(val rgb: Int){
    RED(0xFF0000),
    GREEN(0x00FF00),
    BLUE(0x0000FF),
    FOREST(R.drawable.board_forest_background),
}

class ProjectBackGround(val type:Int, var id:Int):java.io.Serializable{
    constructor():this(1,R.drawable.board_lake_background)
}

public val projectBoardBackgrounds:List<ProjectBackGround> = listOf(
    ProjectBackGround(2,R.drawable.board_lake_background),
    ProjectBackGround(1,Color.rgb(192,192,192)),
    ProjectBackGround(2, R.drawable.board_forest_background)
)
