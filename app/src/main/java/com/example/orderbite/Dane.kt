package com.example.orderbite

fun getOpisPrecel(precelName: String): String {
    return when (precelName) {
        "Kurczak koperkowo ziolowy" -> "Precel prosto z pieca nadziany serem mozzarella, kurczakiem i sosem koperkowo-ziołowym."
        "Cheese" -> "Precel prosto z pieca nadziany podwójną porcją sera mozzarella."
        "Funghi" -> "Precel prosto z pieca nadziany serem mozzarella i pieczarkami."
        "Supreme" -> "Precel prosto z pieca nadziany serem mozzarella i szynką."
        "Spinacio" -> "Precel prosto z pieca nadziany serem mozzarella, szpinakiem, serkiem sałatkowym i czosnkiem."
        "Salami Hot" -> "Precel prosto z pieca nadziany serem mozzarella, salami i papryką jalapeño."
        "Salami Plus" -> "Precel prosto z pieca nadziany serem mozzarella, salami i pieczarkami."
        else -> ""
    }
}

fun getOpisPizza(pizzaName: String): String {
    return when (pizzaName) {
        "Margherita" -> "Rodzaj pizzy w kuchni włoskiej, pochodzącej z Neapolu, pokrytej sosem pomidorowym, mozarellą, świeżą bazylią i oliwą z oliwek."
        "Pepperoni" -> "Pizza z grubszym ciastem, obficie posypana pikantnymi plasterkami pepperoni (ostrą kiełbasą), sosem pomidorowym i dużą ilością sera."
        "Capricciosa" -> "Rodzaj pizzy w kuchni włoskiej, ze składnikami: ser mozzarella, pieczona szynka, grzyby, karczochy i pomidory."
        "Quattro formaggi" -> "Pizza z czterema rodzajami sera, zawierająca mozzarellę, gorgonzolę, parmezan i ricotta. Idealna dla miłośników serowych doznań."
        "Vegetariana" -> "Pizza wegetariańska z warzywami takimi jak papryka, cebula, pomidory, pieczarki i czarne oliwki. Lekka i pełna świeżości."
        "Hawaiian" -> "Pizza z sosem pomidorowym, szynką i kawałkami ananasa."
        "Diavola" -> "Ostra pizza z sosem pomidorowym, mozzarellą i pikantnymi składnikami, takimi jak salami, papryczki chili i pieprz cayenne. Dla tych, którzy lubią uczucie palącego ognia."
        else -> ""
    }
}