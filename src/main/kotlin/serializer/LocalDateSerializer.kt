package serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

object LocalDateUnixSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDate) {
        val timestamp = value.atStartOfDay(ZoneOffset.UTC).toEpochSecond()
        encoder.encodeLong(timestamp)
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        val timestamp = decoder.decodeLong()
        return Instant.ofEpochSecond(timestamp).atZone(ZoneOffset.UTC).toLocalDate()
    }
}
