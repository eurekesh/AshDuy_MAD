package com.ash.voxelpickerproultra

data class TitleData(var url: String="") {
    fun generateUrl(styleChoice:Int, title:String) {
        val transformedTitle = title.replace(' ', '+');
        var baseUrl="https://flamingtext.com/logo/Design-";
        val endUrl="?_variations=true&text="
        val styleUrl = when (styleChoice) {
            0 -> "Clan"
            1 -> "Fire"
            2 -> "Chrominium"
            else -> "Fire"
        }

        url = baseUrl + styleUrl + endUrl + transformedTitle;
    }
}