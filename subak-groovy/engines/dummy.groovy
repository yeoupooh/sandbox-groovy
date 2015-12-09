class Track {
    String file
    String track
    String artist
    String size
    String length
    String bitrate
}

def search(keyword) {
    dummyTracks = []
    for (int i = 0; i < 10; i++) {
        dummyTracks.add(new Track(
                file: 'file' + i + String.valueOf(keyword),
                track: 'track' + i + String.valueOf(keyword),
                artist: 'artist' + i + String.valueOf(keyword),
                size: 'size' + i + String.valueOf(keyword),
                length: 'length' + i + String.valueOf(keyword),
                bitrate: 'bitrate' + i + String.valueOf(keyword)
        ))
    }

    return [tracks: dummyTracks]
}

@Override
public String toString() {
    return 'Dummy'
}
