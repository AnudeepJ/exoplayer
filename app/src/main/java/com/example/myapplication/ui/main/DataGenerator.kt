package com.example.myapplication.ui.main

object DataGenerator {
    fun createList(): List<MediaItemDto> = buildList {
        add(
            MediaItemDto(
                1,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/images/BigBuckBunny.jpg",
                title = "Big Buck Bunny"
            )
        )
        add(
            MediaItemDto(
                2,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ElephantsDream.jpg",
                title = "Elephant Dream"
            )
        )
        add(
            MediaItemDto(
                3,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerBlazes.jpg",
                title = "For Bigger Blazes"
            )
        )
        add(
            MediaItemDto(
                4,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerEscapes.jpg",
                title = "For Bigger Escape"
            )
        )
        add(
            MediaItemDto(
                5,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerFun.jpg",
                title = "For Bigger Fun"
            )
        )
        add(
            MediaItemDto(
                6,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerJoyrides.jpg",
                title = "For Bigger Joyrides"

            )
        )
        add(
            MediaItemDto(
                7,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerMeltdowns.jpg",
                title = "For Bigger Meltdowns"
            )
        )
        add(
            MediaItemDto(
                8,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/images/Sintel.jpg",
                title = "Sintel"
            )
        )
        add(
            MediaItemDto(
                9,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/SubaruOutbackOnStreetAndDirt.jpg",
                title = "Subaru Outback On Street And Dirt"
            )
        )
        add(
            MediaItemDto(
                10,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/TearsOfSteel.jpg",
                title = "Tears of Steel"
            )
        )
    }
}