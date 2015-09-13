import groovy.util.logging.Slf4j
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.audio.mp3.MP3AudioHeader
import org.jaudiotagger.audio.mp3.MP3File
import org.jaudiotagger.tag.id3.AbstractID3v2Tag

import java.util.logging.LogManager

@Grapes([
        @Grab(group = 'org', module = 'jaudiotagger', version = '2.0.3'),
        @Grab(group = 'org.slf4j', module = 'slf4j-api', version = '1.7.12'),
        @Grab(group = 'ch.qos.logback', module = 'logback-classic', version = '1.1.3'),
        @Grab(group = 'ch.qos.logback', module = 'logback-core', version = '1.1.3')
])

@Slf4j
class TagReader {
    static void main(args) {

        MP3File f = (MP3File) AudioFileIO.read(new File(args[0]));
        MP3AudioHeader audioHeader = f.getAudioHeader();

        log.debug("${audioHeader.getTrackLength()}")

        audioHeader.getSampleRateAsNumber();
        audioHeader.getChannels();
        audioHeader.isVariableBitRate();

        log.debug("encodingtype=${audioHeader.getEncodingType()}")

        log.debug("hasId3v1=${f.hasID3v1Tag()}")
        log.debug("hasId3v2=${f.hasID3v2Tag()}")

        if (f.hasID3v2Tag()) {
            AbstractID3v2Tag v2tag = f.getID3v2Tag()

            log.debug("fields=[${v2tag.fields.size()}]")
            v2tag.getFields().eachWithIndex { field, index ->
                log.debug("field[$index].id=[${field.getId()}]")
                if (field.isBinary() == false) {
                    def encoding = field.getProperties().get('encoding')
                    log.debug("encoding=[${encoding}]")
                    log.debug("field[$index][${field.getId()}]=text[${new String(field.getRawContent())}],utf8[${new String(field.getRawContent(), "utf-8")}],encoded-text[${new String(field.getRawContent(), encoding)}]")
                } else {
                    log.debug("field[$index][${field.getId()}]=binary[${field.getRawContent()}]")
                }
                field.getProperties().keySet().each { key ->
                    log.debug("prop[${key}]=[${field.getProperties().get(key)}]")
                }
            }
        }
    }
}

// Disable jaudiotagger logging
LogManager.getLogManager().reset()

TagReader.main(args)