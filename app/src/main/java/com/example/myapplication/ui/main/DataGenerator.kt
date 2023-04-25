package com.example.myapplication.ui.main

object DataGenerator {
    fun createList(): List<MediaItemDto> = buildList {
        add(
            MediaItemDto(
                1,
                "https://objectstorage.ap-hyderabad-1.oraclecloud.com/n/ax8o1gwvyizc/b/bucket-exoplayer-demo/o/big_buck_bunny_720p_1mb.mp4",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/images/BigBuckBunny.jpg",
                title = "Big Buck Bunny"
            )
        )
        add(
            MediaItemDto(
                2,
                "https://objectstorage.ap-hyderabad-1.oraclecloud.com/n/ax8o1gwvyizc/b/bucket-exoplayer-demo/o/sample_large_3.mp4",
                "https://objectstorage.ap-hyderabad-1.oraclecloud.com/n/ax8o1gwvyizc/b/bucket-exoplayer-demo/o/ocean_surfing_three.png",
                title = "Surfing Large Video"
            )
        )
        add(
            MediaItemDto(
                3,
                "https://objectstorage.ap-hyderabad-1.oraclecloud.com/n/ax8o1gwvyizc/b/bucket-exoplayer-demo/o/file_example_MP4_1280_10MG.mp4",
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
        add(
            MediaItemDto(
                11,
                "https://objectstorage.ap-hyderabad-1.oraclecloud.com/n/ax8o1gwvyizc/b/bucket-exoplayer-demo/o/file_example_MP3_5MG.mp3",
                "https://objectstorage.ap-hyderabad-1.oraclecloud.com/n/ax8o1gwvyizc/b/bucket-exoplayer-demo/o/mp3.jpeg",
                title = "Mp3 file only"
            )
        )
    }


    fun createOfflineList(): List<MediaItemDto> = buildList {
        add(
            MediaItemDto(
                1,
                "android.resource://com.example.myapplication/raw/sample1",
                "https://objectstorage.ap-hyderabad-1.oraclecloud.com/n/ax8o1gwvyizc/b/bucket-exoplayer-demo/o/nature_with_cars_one.png",
                title = "Forest With Cars"
            )
        )
        add(
            MediaItemDto(
                2,
                "android.resource://com.example.myapplication/raw/sampe_video_two",
                "https://objectstorage.ap-hyderabad-1.oraclecloud.com/n/ax8o1gwvyizc/b/bucket-exoplayer-demo/o/earth_two.png",
                title = "Nature Earth"
            )
        )
        add(
            MediaItemDto(
                3,
                "android.resource://com.example.myapplication/raw/sample_large_3",
                "https://objectstorage.ap-hyderabad-1.oraclecloud.com/n/ax8o1gwvyizc/b/bucket-exoplayer-demo/o/ocean_surfing_three.png",
                title = "Ocean Surfing Large"
            )
        )
        add(
            MediaItemDto(
                4,
                "android.resource://com.example.myapplication/raw/sample_four",
                "https://objectstorage.ap-hyderabad-1.oraclecloud.com/n/ax8o1gwvyizc/b/bucket-exoplayer-demo/o/grapes_screen_shot_four.png",
                title = "Beautiful Grapes"
            )
        )

    }
}